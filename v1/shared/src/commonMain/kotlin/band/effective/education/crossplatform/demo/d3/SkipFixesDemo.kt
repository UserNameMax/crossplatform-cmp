package band.effective.education.crossplatform.demo.d3

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import band.effective.education.crossplatform.data.DemoItem
import band.effective.education.crossplatform.data.demoItems
import band.effective.education.crossplatform.stand.DemoScaffold
import band.effective.education.crossplatform.stand.RecompositionBadge
import band.effective.education.crossplatform.stand.rememberRecompositionCount
import lection1.shared.generated.resources.Res
import lection1.shared.generated.resources.a16_explanation
import lection1.shared.generated.resources.a16_redrawn
import lection1.shared.generated.resources.a16_search
import org.jetbrains.compose.resources.stringResource

/**
 * A16 — три способа починить.
 *
 * Продолжение A9 и слайд D3 «Три способа починить». Обёртка из A9 пересоздаётся
 * в теле — счётчик бежит. Три починки в порядке предпочтительности:
 *  1. посчитать выше — экземпляр создан вне области, которая читает query;
 *  2. remember с ключом — считаем здесь, но пересчитываем только при смене source;
 *  3. @Stable — сравнение возвращается к equals(). Класс обязан быть data class:
 *     аннотация меняет способ сравнения, но equals() у обычного класса — та же ссылка.
 */
private enum class Fix(val code: String) {
    Broken("ByWrapper(ScreenState(source))"),
    Upstream("ByWrapper(upstream)"),
    RememberKey("ByWrapper(remember(source) { ScreenState(source) })"),
    Stable("ByStableWrapper(StableScreenState(source))"),
}

/** Обещание компилятору: equals() не меняется со временем. Его надо держать. */
@Stable
data class StableScreenState(val items: List<DemoItem>)

@Composable
fun SkipFixesDemo() {
    var query by remember { mutableStateOf("") }
    var fix by remember { mutableStateOf(Fix.Broken) }

    val source = remember { demoItems.filter { it.visible }.take(4) }
    val upstream = remember { ScreenState(source) }       // 1. посчитано выше

    DemoScaffold(
        explanation = stringResource(Res.string.a16_explanation),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Fix.entries.forEach { option ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(selected = fix == option, onClick = { fix = option }),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    RadioButton(selected = fix == option, onClick = { fix = option })
                    // листинг кода, не подпись интерфейса
                    Text(option.code, fontFamily = FontFamily.Monospace, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }

        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text(stringResource(Res.string.a16_search)) },
            modifier = Modifier.fillMaxWidth(),
        )

        // Всё ниже — в той же области рекомпозиции, где читается query.
        when (fix) {
            Fix.Broken -> ByWrapperCounter(state = ScreenState(source))
            Fix.Upstream -> ByWrapperCounter(state = upstream)
            Fix.RememberKey -> ByWrapperCounter(state = remember(source) { ScreenState(source) })   // 2.
            Fix.Stable -> ByStableWrapperCounter(state = StableScreenState(source))                // 3.
        }
    }
}

@Composable
private fun ByWrapperCounter(state: ScreenState) {
    Counter(count = rememberRecompositionCount(), items = state.items)
}

@Composable
private fun ByStableWrapperCounter(state: StableScreenState) {
    Counter(count = rememberRecompositionCount(), items = state.items)
}

@Composable
private fun Counter(count: Int, items: List<DemoItem>) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        RecompositionBadge(stringResource(Res.string.a16_redrawn), count, emphasised = true)
        Text(items.joinToString { it.title }, style = MaterialTheme.typography.bodyLarge)
    }
}
