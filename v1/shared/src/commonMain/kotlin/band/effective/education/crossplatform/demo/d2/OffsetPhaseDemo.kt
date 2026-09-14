package band.effective.education.crossplatform.demo.d2

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import band.effective.education.crossplatform.stand.DemoScaffold
import band.effective.education.crossplatform.stand.RecompositionBadge
import band.effective.education.crossplatform.stand.rememberRecompositionCount
import lection1.shared.generated.resources.Res
import lection1.shared.generated.resources.a13_compositions
import lection1.shared.generated.resources.a13_explanation
import lection1.shared.generated.resources.a13_hint_composition
import lection1.shared.generated.resources.a13_hint_layout
import lection1.shared.generated.resources.a13_in_composition
import lection1.shared.generated.resources.a13_in_layout
import lection1.shared.generated.resources.a13_shift
import org.jetbrains.compose.resources.stringResource

/**
 * A13 — модификаторы по фазам: offset значением и offset лямбдой.
 *
 * Слайд D2 «Модификаторы по фазам»: лямбда откладывает чтение состояния в ту фазу,
 * где оно нужно. Двигаем ползунок — квадрат едет в обоих режимах, но счётчик
 * композиции у Mover бежит только когда shift прочитан в теле функции.
 *
 * Счётчиков layout и отрисовки здесь сознательно нет: offset лямбдой перезапускает
 * только своё размещение, а слой квадрата при сдвиге не перерисовывается — счётчики
 * стояли бы на месте, пока квадрат едет, и уводили бы от одной мысли экрана (SPEC 2.3).
 */
@Composable
fun OffsetPhaseDemo() {
    var deferred by remember { mutableStateOf(false) }
    val shift = remember { mutableIntStateOf(0) }

    DemoScaffold(
        explanation = stringResource(Res.string.a13_explanation),
        toggleLabel = stringResource(if (deferred) Res.string.a13_in_layout else Res.string.a13_in_composition),
        toggleChecked = deferred,
        onToggle = { deferred = it },
        toggleHint = stringResource(if (deferred) Res.string.a13_hint_layout else Res.string.a13_hint_composition),
    ) {
        Text(stringResource(Res.string.a13_shift), style = MaterialTheme.typography.titleMedium)
        Slider(
            value = shift.intValue.toFloat(),
            onValueChange = { shift.intValue = it.toInt() },
            valueRange = 0f..120f,
        )
        if (deferred) {
            Text("Modifier.offset { IntOffset(0, shift) }", fontFamily = FontFamily.Monospace)
            DeferredMover(shift = shift)
        } else {
            Text("Modifier.offset(y = shift.dp)", fontFamily = FontFamily.Monospace)
            Mover(shift = shift)
        }
    }
}

@Composable
private fun Mover(shift: MutableIntState) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        RecompositionBadge(stringResource(Res.string.a13_compositions), rememberRecompositionCount(), emphasised = true)
        Box(Modifier.fillMaxWidth().height(200.dp)) {
            Square(
                modifier = Modifier.offset(y = shift.intValue.dp),   // shift прочитан в композиции
            )
        }
    }
}

@Composable
private fun DeferredMover(shift: MutableIntState) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        RecompositionBadge(stringResource(Res.string.a13_compositions), rememberRecompositionCount(), emphasised = true)
        Box(Modifier.fillMaxWidth().height(200.dp)) {
            Square(
                modifier = Modifier.offset { IntOffset(0, shift.intValue.dp.roundToPx()) }, // shift прочитан в layout
            )
        }
    }
}

@Composable
private fun Square(modifier: Modifier) {
    Box(
        modifier = modifier
            .size(72.dp)
            .background(MaterialTheme.colorScheme.primary),
    )
}
