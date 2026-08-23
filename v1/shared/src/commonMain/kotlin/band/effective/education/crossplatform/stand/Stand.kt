package band.effective.education.crossplatform.stand

import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.savedstate.read
import lection1.shared.generated.resources.Res
import lection1.shared.generated.resources.stand_back
import lection1.shared.generated.resources.stand_locale_note
import lection1.shared.generated.resources.stand_not_found
import lection1.shared.generated.resources.stand_subtitle
import lection1.shared.generated.resources.stand_theme_dark
import lection1.shared.generated.resources.stand_theme_light
import lection1.shared.generated.resources.stand_title
import org.jetbrains.compose.resources.stringResource

private const val ROUTE_LIST = "list"
private const val ROUTE_DEMO = "demo"
private const val ARG_ID = "demoId"

/**
 * Оболочка стенда.
 *
 * Помимо своей прямой работы сам служит иллюстрацией к блоку D1: тут есть навигация
 * со стеком, тема из токенов и строки из ресурсов. Когда на паре заходит речь про
 * тему — переключатель уже на экране, достаточно нажать.
 */
@Composable
fun Stand() {
    var dark by remember { mutableStateOf(false) }
    val navController = rememberNavController()

    StandTheme(dark = dark) {
        NavHost(navController = navController, startDestination = ROUTE_LIST) {
            composable(ROUTE_LIST) {
                DemoListScreen(
                    dark = dark,
                    onThemeChange = { dark = it },
                    onOpen = { demo -> navController.navigate("$ROUTE_DEMO/${demo.id}") },
                )
            }
            composable("$ROUTE_DEMO/{$ARG_ID}") { entry ->
                val demo = demoById(entry.arguments?.read { getStringOrNull(ARG_ID) })
                DemoScreen(
                    demo = demo,
                    dark = dark,
                    onThemeChange = { dark = it },
                    onBack = { navController.popBackStack() },
                )
            }
        }
    }
}

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
