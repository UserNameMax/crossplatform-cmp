package band.effective.education.crossplatform.demo.d1

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import band.effective.education.crossplatform.data.DemoItem
import band.effective.education.crossplatform.data.demoItems
import band.effective.education.crossplatform.stand.DemoScaffold
import lection1.shared.generated.resources.Res
import lection1.shared.generated.resources.a11_back
import lection1.shared.generated.resources.a11_by_id
import lection1.shared.generated.resources.a11_by_model
import lection1.shared.generated.resources.a11_evolve
import lection1.shared.generated.resources.a11_explanation
import lection1.shared.generated.resources.a11_hint_id
import lection1.shared.generated.resources.a11_hint_model
import lection1.shared.generated.resources.a11_in_source
import lection1.shared.generated.resources.a11_on_screen
import lection1.shared.generated.resources.a11_stack
import org.jetbrains.compose.resources.stringResource

/**
 * A11 — модель в маршруте устаревает.
 *
 * Слайд D1 «Навигация: где ломается». Стек — обычный список объектов, как в стенде
 * и в шаблоне ПЗ1. В одном режиме в объект маршрута кладём id, в другом — модель
 * целиком. На экране детали меняем данные в источнике: экран по id показывает новое,
 * экран с копией — старое. Приложение при этом не падает, в том и неприятность.
 *
 * Внутри демо стек рисуется через when, а не через NavDisplay: вложенный NavDisplay
 * перехватывал бы «назад» у стенда. Устройство стека от этого не меняется.
 */
private sealed interface Route {
    data object List : Route
    data class DetailById(val id: Int) : Route
    data class DetailByModel(val item: DemoItem) : Route
}

@Composable
fun RouteArgumentDemo() {
    var byId by remember { mutableStateOf(true) }
    val source = remember { mutableStateListOf(*demoItems.take(4).toTypedArray()) }
    val backStack = remember { mutableStateListOf<Route>(Route.List) }

    DemoScaffold(
        explanation = stringResource(Res.string.a11_explanation),
        toggleLabel = stringResource(if (byId) Res.string.a11_by_id else Res.string.a11_by_model),
        toggleChecked = byId,
        onToggle = { byId = it },
        toggleHint = stringResource(if (byId) Res.string.a11_hint_id else Res.string.a11_hint_model),
    ) {
        Text(
            // листинг стека — код, не подпись интерфейса
            text = stringResource(Res.string.a11_stack, backStack.joinToString(", ") { it.toString() }),
            fontFamily = FontFamily.Monospace,
            style = MaterialTheme.typography.bodyMedium,
        )

        when (val top = backStack.lastOrNull() ?: Route.List) {
            Route.List -> source.forEach { item ->
                Card(
                    modifier = Modifier.fillMaxWidth().clickable {
                        backStack.add(if (byId) Route.DetailById(item.id) else Route.DetailByModel(item))
                    },
                ) {
                    Text(item.title, style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(16.dp))
                }
            }

            is Route.DetailById -> Detail(
                shown = source.first { it.id == top.id },   // данные достаёт сам экран
                current = source.first { it.id == top.id },
                onEvolve = { source.evolve(top.id) },
                onBack = { if (backStack.size > 1) backStack.removeLastOrNull() },
            )

            is Route.DetailByModel -> Detail(
                shown = top.item,                            // копия, снятая в момент перехода
                current = source.first { it.id == top.item.id },
                onEvolve = { source.evolve(top.item.id) },
                onBack = { if (backStack.size > 1) backStack.removeLastOrNull() },
            )
        }
    }
}

/**
 * Данные обновились в одном месте: к имени в источнике дописывается версия, v2, v3…
 * ASCII намеренно: у шрифта web-сборки нет глифов вроде ★ и →.
 */
private fun MutableList<DemoItem>.evolve(id: Int) {
    val index = indexOfFirst { it.id == id }
    val item = this[index]
    val name = item.title.substringBefore(" v")
    val version = item.title.substringAfter(" v", "1").toInt() + 1
    this[index] = item.copy(title = "$name v$version")
}

@Composable
private fun Detail(
    shown: DemoItem,
    current: DemoItem,
    onEvolve: () -> Unit,
    onBack: () -> Unit,
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = stringResource(Res.string.a11_on_screen, shown.title),
                style = MaterialTheme.typography.headlineSmall,
            )
            Text(
                text = stringResource(Res.string.a11_in_source, current.title),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(onClick = onBack) { Text(stringResource(Res.string.a11_back)) }
                Button(onClick = onEvolve) { Text(stringResource(Res.string.a11_evolve)) }
            }
        }
    }
}
