package band.effective.education.crossplatform.stand

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import lection1.shared.generated.resources.Res
import lection1.shared.generated.resources.stand_back
import lection1.shared.generated.resources.stand_locale_note
import lection1.shared.generated.resources.stand_not_found
import lection1.shared.generated.resources.stand_subtitle
import lection1.shared.generated.resources.stand_theme_dark
import lection1.shared.generated.resources.stand_theme_light
import lection1.shared.generated.resources.stand_title
import org.jetbrains.compose.resources.stringResource

/** Длительность перехода между экранами. Одна на оба направления. */
private const val TRANSITION_MS = 300

/**
 * Экран стенда — объект, а не строка-адрес. Ровно то, что показано в блоке D1:
 * аргумент лежит полем, опечатку в нём ловит компилятор.
 */
sealed interface StandScreen {
    data object List : StandScreen
    data class Demo(val id: String) : StandScreen
}

/**
 * Оболочка стенда.
 *
 * Помимо своей прямой работы сам служит иллюстрацией к блоку D1: тут есть навигация
 * со стеком, тема из токенов и строки из ресурсов. Когда на паре заходит речь про
 * тему — переключатель уже на экране, достаточно нажать.
 *
 * Навигация — Navigation 3, как в колоде и в шаблоне ПЗ1: стек — обычный список
 * объектов-экранов в состоянии композиции, графа маршрутов нет. До 14.09.2026 здесь
 * стоял Navigation Compose со строковыми маршрутами — то самое устройство, от которого
 * колода D1 отталкивается, поэтому заменено.
 */
@Composable
fun Stand() {
    // Системная настройка — входное значение, дальше тема переключается тумблером.
    val systemDark = isSystemInDarkTheme()
    var dark by remember { mutableStateOf(systemDark) }
    val backStack = remember { mutableStateListOf<StandScreen>(StandScreen.List) }
    // Корень не снимается: пустой стек NavDisplay не принимает и роняет приложение.
    // Проверено 14.09.2026 — двойное нажатие «Назад» на web ловило ровно это.
    val back: () -> Unit = { if (backStack.size > 1) backStack.removeLastOrNull() }

    StandTheme(dark = dark) {
        NavDisplay(
            backStack = backStack,
            onBack = back,                                                    // назад — убрать последний
            // Все три спецификации задаются явно: значения по умолчанию на не-Android
            // таргетах — заглушки, которые бросают NotImplementedError (грабля ПЗ1).
            transitionSpec = { slide(SlideDirection.Start) },
            popTransitionSpec = { slide(SlideDirection.End) },
            predictivePopTransitionSpec = { slide(SlideDirection.End) },
            entryProvider = entryProvider {
                entry<StandScreen.List> {
                    DemoListScreen(
                        dark = dark,
                        onThemeChange = { dark = it },
                        onOpen = { demo -> backStack.add(StandScreen.Demo(demo.id)) }, // открыть — добавить в конец
                    )
                }
                entry<StandScreen.Demo> { key ->
                    DemoScreen(
                        demo = demoById(key.id),
                        dark = dark,
                        onThemeChange = { dark = it },
                        onBack = back,
                    )
                }
            },
        )
    }
}

private fun AnimatedContentTransitionScope<*>.slide(direction: SlideDirection): ContentTransform =
    (slideIntoContainer(direction, tween(TRANSITION_MS)) + fadeIn(tween(TRANSITION_MS)))
        .togetherWith(
            slideOutOfContainer(direction, tween(TRANSITION_MS)) + fadeOut(tween(TRANSITION_MS)),
        )

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DemoListScreen(
    dark: Boolean,
    onThemeChange: (Boolean) -> Unit,
    onOpen: (Demo) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.stand_title)) },
                actions = { ThemeSwitch(dark = dark, onThemeChange = onThemeChange) },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = stringResource(Res.string.stand_subtitle),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = stringResource(Res.string.stand_locale_note),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            HorizontalDivider()

            Depth.entries.forEach { depth ->
                val ofDepth = demos.filter { it.depth == depth }
                if (ofDepth.isNotEmpty()) {
                    Text(
                        text = stringResource(depth.title),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 8.dp),
                    )
                    ofDepth.forEach { demo ->
                        DemoRow(demo = demo, onClick = { onOpen(demo) })
                    }
                }
            }
        }
    }
}

@Composable
private fun DemoRow(demo: Demo, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = stringResource(demo.title), style = MaterialTheme.typography.titleMedium)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DemoScreen(
    demo: Demo?,
    dark: Boolean,
    onThemeChange: (Boolean) -> Unit,
    onBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (demo != null) {
                        Column {
                            Text(
                                text = stringResource(demo.title),
                                style = MaterialTheme.typography.titleLarge,
                            )
                            Text(
                                text = stringResource(demo.depth.title),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text(stringResource(Res.string.stand_back))
                    }
                },
                actions = { ThemeSwitch(dark = dark, onThemeChange = onThemeChange) },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState()),
        ) {
            if (demo == null) {
                Text(
                    text = stringResource(Res.string.stand_not_found),
                    modifier = Modifier.padding(16.dp),
                )
            } else {
                demo.content()
            }
        }
    }
}

@Composable
private fun ThemeSwitch(dark: Boolean, onThemeChange: (Boolean) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(end = 12.dp),
    ) {
        Text(
            text = stringResource(
                if (dark) Res.string.stand_theme_dark else Res.string.stand_theme_light,
            ),
            style = MaterialTheme.typography.bodyMedium,
        )
        Switch(checked = dark, onCheckedChange = onThemeChange)
    }
}
