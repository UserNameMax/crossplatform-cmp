package band.effective.education.crossplatform.demo.d2

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.dp
import band.effective.education.crossplatform.stand.DemoScaffold
import band.effective.education.crossplatform.stand.PhaseBadges
import band.effective.education.crossplatform.stand.PhaseLog
import band.effective.education.crossplatform.stand.countPhases
import band.effective.education.crossplatform.stand.rememberRecompositionCount
import lection1.shared.generated.resources.Res
import lection1.shared.generated.resources.a12_explanation
import lection1.shared.generated.resources.a12_query
import lection1.shared.generated.resources.a12_search_field
import lection1.shared.generated.resources.a12_title
import lection1.shared.generated.resources.a12_top_bar
import lection1.shared.generated.resources.a12_width
import org.jetbrains.compose.resources.stringResource

/**
 * A12 — рекомпозиция и переизмерение — разное.
 *
 * Дерево со слайда D2 «Разные вещи»: TopBar без параметров и SearchField с query.
 * Набор буквы перезапускает композицию того, кто получил новый query; TopBar пропущен.
 * Ползунок сужает «окно»: описание то же, композиции нечего пересобирать — меняются
 * только layout и отрисовка.
 *
 * Ширина «окна» читается в лямбде layout, а не в теле: иначе ползунок перезапускал бы
 * композицию самого демо, и было бы не «окно сузили», а «поменяли состояние».
 */
@Composable
fun PhasesDemo() {
    var query by remember { mutableStateOf("sau") }
    var width by remember { mutableFloatStateOf(1f) }

    DemoScaffold(
        explanation = stringResource(Res.string.a12_explanation),
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text(stringResource(Res.string.a12_query)) },
            modifier = Modifier.fillMaxWidth(),
        )
        Text(stringResource(Res.string.a12_width), style = MaterialTheme.typography.titleMedium)
        Slider(value = width, onValueChange = { width = it }, valueRange = 0.4f..1f)

        Column(
            modifier = Modifier.windowWidth { width },
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            TopBar()
            SearchField(query = query)
        }
    }
}

@Composable
private fun TopBar() {
    val log = remember { PhaseLog() }
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        PhaseBadges(compositions = rememberRecompositionCount(), log = log)
        Text(
            text = stringResource(Res.string.a12_top_bar),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = stringResource(Res.string.a12_title),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.countPhases(log),
        )
    }
}

@Composable
private fun SearchField(query: String) {
    val log = remember { PhaseLog() }
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        PhaseBadges(compositions = rememberRecompositionCount(), log = log)
        Text(
            text = stringResource(Res.string.a12_search_field),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = query,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.fillMaxWidth().countPhases(log),
        )
    }
}

/** «Окно» заданной доли ширины. Доля читается в layout, композицию не трогает. */
private fun Modifier.windowWidth(fraction: () -> Float): Modifier = layout { measurable, constraints ->
    val w = (constraints.maxWidth * fraction()).toInt()
    val placeable = measurable.measure(constraints.copy(minWidth = w, maxWidth = w))
    layout(placeable.width, placeable.height) { placeable.place(0, 0) }
}
