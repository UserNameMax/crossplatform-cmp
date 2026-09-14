package band.effective.education.crossplatform.stand

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.dp
import lection1.shared.generated.resources.Res
import lection1.shared.generated.resources.phase_composition
import lection1.shared.generated.resources.phase_draw
import lection1.shared.generated.resources.phase_layout
import org.jetbrains.compose.resources.stringResource

/**
 * Служебный инструмент стенда: сколько раз узел прошёл layout и отрисовку.
 * Студентам с экрана не показывается — см. SPEC 2.2.
 *
 * Счёт идёт в обычных полях, наружу публикуется в snapshot-state. Читает его только
 * [PhaseBadges], поэтому публикация перезапускает плашки, а не измеряемый узел.
 */
class PhaseLog {
    private var layoutPasses = 0
    private var drawPasses = 0

    var layouts: Int by mutableIntStateOf(0)
        private set

    var draws: Int by mutableIntStateOf(0)
        private set

    internal fun onLayout() {
        layoutPasses++
        layouts = layoutPasses
    }

    internal fun onDraw() {
        drawPasses++
        draws = drawPasses
    }
}

/**
 * Считает проходы узла: измерение и размещение — в «layout», отрисовку — в «отрисовку».
 *
 * graphicsLayer здесь обязателен. Без своего слоя узел перерисовывается вместе со всем
 * слоем родителя — в том числе когда перерисовались сами плашки со счётчиком, и счётчик
 * гонял бы сам себя.
 */
fun Modifier.countPhases(log: PhaseLog): Modifier = this
    .layout { measurable, constraints ->
        log.onLayout()
        val placeable = measurable.measure(constraints)
        layout(placeable.width, placeable.height) {
            placeable.place(0, 0)
        }
    }
    .graphicsLayer()
    .drawBehind { log.onDraw() }

/**
 * Три плашки: композиция, layout, отрисовка. Ширина фиксирована: плашка, выросшая
 * с «9» до «10», не должна переизмерять соседей и портить им счёт.
 */
@Composable
fun PhaseBadges(compositions: Int, log: PhaseLog, modifier: Modifier = Modifier) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        RecompositionBadge(
            label = stringResource(Res.string.phase_composition),
            count = compositions,
            modifier = Modifier.width(150.dp),
        )
        RecompositionBadge(
            label = stringResource(Res.string.phase_layout),
            count = log.layouts,
            modifier = Modifier.width(150.dp),
        )
        RecompositionBadge(
            label = stringResource(Res.string.phase_draw),
            count = log.draws,
            modifier = Modifier.width(150.dp),
        )
    }
}
