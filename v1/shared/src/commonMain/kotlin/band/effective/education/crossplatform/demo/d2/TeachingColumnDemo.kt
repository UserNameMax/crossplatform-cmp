package band.effective.education.crossplatform.demo.d2

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.dp
import band.effective.education.crossplatform.stand.DemoScaffold
import band.effective.education.crossplatform.stand.outline
import lection1.shared.generated.resources.Res
import lection1.shared.generated.resources.a14_explanation
import lection1.shared.generated.resources.a14_hint_real
import lection1.shared.generated.resources.a14_hint_teaching
import lection1.shared.generated.resources.a14_real
import lection1.shared.generated.resources.a14_teaching
import org.jetbrains.compose.resources.stringResource

/**
 * A14 — учебная колонка и настоящий Column.
 *
 * TeachingColumn — дословно код со слайда D2 «Три шага в коде». В рамке высотой 260
 * с fillMaxSize() у неё рамки min = max = 260, и слайд «Где учебная колонка проще»
 * предсказывает две вещи сразу: каждый ребёнок растянется на всю высоту (нет
 * copy(minHeight = 0)), а сумма высот больше рамок — Compose урежет размер и
 * отцентрирует содержимое (нет constrainHeight). В окне окажется середина: второй ребёнок.
 */
@Composable
fun TeachingColumnDemo() {
    var real by remember { mutableStateOf(false) }

    DemoScaffold(
        explanation = stringResource(Res.string.a14_explanation),
        toggleLabel = stringResource(if (real) Res.string.a14_real else Res.string.a14_teaching),
        toggleChecked = real,
        onToggle = { real = it },
        toggleHint = stringResource(if (real) Res.string.a14_hint_real else Res.string.a14_hint_teaching),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .clipToBounds()
                .outline(MaterialTheme.colorScheme.error, dashed = true),
        ) {
            if (real) {
                Column(Modifier.fillMaxSize()) { Cells() }
            } else {
                TeachingColumn(Modifier.fillMaxSize()) { Cells() }
            }
        }
    }
}

@Composable
private fun TeachingColumn(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Layout(content = content, modifier = modifier) { measurables, constraints ->
        // 1. рамки вниз: каждого ребёнка меряем ровно один раз, второй measure — исключение
        val placeables = measurables.map { it.measure(constraints) }
        // 2. размер вверх: решаем свой размер, его увидит наш родитель
        val width = constraints.maxWidth
        val height = placeables.sumOf { it.height }
        // 3. позиции: ребёнок узнаёт, где стоит, только здесь
        layout(width, height) {
            var y = 0
            placeables.forEach { placeable ->
                placeable.placeRelative(x = 0, y = y)
                y += placeable.height
            }
        }
    }
}

@Composable
private fun Cells() {
    // имена — данные, а не подписи интерфейса
    Cell("1 · Bulbasaur", Color(0xFF7AC74C))
    Cell("2 · Ivysaur", Color(0xFF5AA0E6))
    Cell("3 · Venusaur", Color(0xFFE6A23C))
}

@Composable
private fun Cell(text: String, color: Color) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge,
        color = Color.Black,
        modifier = Modifier.fillMaxWidth().background(color).padding(16.dp),
    )
}
