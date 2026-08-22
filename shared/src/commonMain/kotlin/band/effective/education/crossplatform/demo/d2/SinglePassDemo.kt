package band.effective.education.crossplatform.demo.d2

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.layout.Layout
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import band.effective.education.crossplatform.stand.DemoScaffold
import lection1.shared.generated.resources.Res
import lection1.shared.generated.resources.a6_explanation
import lection1.shared.generated.resources.a6_intrinsics_on
import lection1.shared.generated.resources.a6_intrinsics_off
import lection1.shared.generated.resources.a6_hint
import lection1.shared.generated.resources.a6_measure_calls
import lection1.shared.generated.resources.a6_intrinsic_queries
import lection1.shared.generated.resources.a6_cell_short
import lection1.shared.generated.resources.a6_cell_medium
import lection1.shared.generated.resources.a6_cell_another
import org.jetbrains.compose.resources.stringResource
import band.effective.education.crossplatform.stand.MeasureLog
import band.effective.education.crossplatform.stand.RecompositionBadge

/**
 * A6 — один проход измерения.
 *
 * Показывает: каждый ребёнок измеряется ровно один раз за проход. Интринсики этого
 * не нарушают — сначала детей спрашивают об интринсиках, потом родитель считает
 * ограничения, и только потом идёт единственное измерение.
 *
 * Это единственный способ показать утверждение «интринсики не измеряют детей дважды»
 * иначе как на слово.
 */
@Composable
fun SinglePassDemo() {
    var useIntrinsics by remember { mutableStateOf(false) }
    val log = remember { MeasureLog() }

    DemoScaffold(
        explanation = stringResource(Res.string.a6_explanation),
        toggleLabel = stringResource(
            if (useIntrinsics) Res.string.a6_intrinsics_on else Res.string.a6_intrinsics_off,
        ),
        toggleChecked = useIntrinsics,
        onToggle = { useIntrinsics = it },
        toggleHint = stringResource(Res.string.a6_hint),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            RecompositionBadge(
                label = stringResource(Res.string.a6_measure_calls, log.childCount),
                count = log.measureCalls,
                emphasised = true,
            )
            RecompositionBadge(
                label = stringResource(Res.string.a6_intrinsic_queries),
                count = log.intrinsicQueries,
            )

            EqualHeightRow(log = log, useIntrinsics = useIntrinsics) {
                Cell(stringResource(Res.string.a6_cell_short), lines = 1)
                Cell(stringResource(Res.string.a6_cell_medium), lines = 3)
                Cell(stringResource(Res.string.a6_cell_another), lines = 2)
            }
        }
    }
}

@Composable
private fun EqualHeightRow(
    log: MeasureLog,
    useIntrinsics: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Layout(content = content, modifier = modifier) { measurables, constraints ->
        var measures = 0
        var intrinsics = 0

        val childWidth = if (measurables.isEmpty()) 0 else constraints.maxWidth / measurables.size

        // Шаг 1: если нужен общий размер — спрашиваем детей об интринсиках.
        // Это опрос, а не измерение.
        val commonHeight = if (useIntrinsics) {
            measurables.maxOf { measurable ->
                intrinsics++
                measurable.minIntrinsicHeight(childWidth)
            }
        } else {
            0
        }

        // Шаг 2: измеряем каждого ребёнка. Ровно один раз.
        val placeables = measurables.map { measurable ->
            measures++
            measurable.measure(
                Constraints(
                    minWidth = childWidth,
                    maxWidth = childWidth,
                    minHeight = commonHeight,
                    maxHeight = constraints.maxHeight,
                ),
            )
        }

        log.publish(measures = measures, intrinsics = intrinsics, children = measurables.size)

        val height = placeables.maxOfOrNull { it.height } ?: 0

        // Шаг 3: ставим позиции.
        layout(constraints.maxWidth, height) {
            var x = 0
            placeables.forEach { placeable ->
                placeable.placeRelative(x = x, y = 0)
                x += placeable.width
            }
        }
    }
}

@Composable
private fun Cell(text: String, lines: Int) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(8.dp),
    ) {
        repeat(lines) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
