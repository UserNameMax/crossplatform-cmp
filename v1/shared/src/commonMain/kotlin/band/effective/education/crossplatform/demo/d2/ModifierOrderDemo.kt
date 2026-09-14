package band.effective.education.crossplatform.demo.d2

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import band.effective.education.crossplatform.stand.DemoScaffold
import band.effective.education.crossplatform.stand.outline
import lection1.shared.generated.resources.Res
import lection1.shared.generated.resources.a7_content_size
import lection1.shared.generated.resources.a7_explanation
import lection1.shared.generated.resources.a7_legend
import lection1.shared.generated.resources.a7_node_size
import org.jetbrains.compose.resources.stringResource

/**
 * A7 — порядок модификаторов.
 *
 * Четыре цепочки со слайда D2 «Порядок модификаторов»: пара padding/size и пара
 * background/padding. Звено отдаёт следующему уже изменённые рамки и рисует в своих
 * границах, поэтому левое звено всегда снаружи правого.
 *
 * Размеры под каждой цепочкой не написаны руками, а сняты с узла после измерения —
 * расхождение со слайдом было бы видно сразу. Пунктир — границы узла, сплошная
 * рамка — содержимое.
 */
@Composable
fun ModifierOrderDemo() {
    val fill = MaterialTheme.colorScheme.primary
    val bg = MaterialTheme.colorScheme.tertiaryContainer

    DemoScaffold(
        explanation = stringResource(Res.string.a7_explanation),
    ) {
        Text(
            text = stringResource(Res.string.a7_legend),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            // листинги кода в первом аргументе — не подписи интерфейса
            Variant("Modifier\n    .padding(16.dp)\n    .size(100.dp)", Modifier.padding(16.dp).size(100.dp)) {
                Box(Modifier.fillMaxSize().background(fill))
            }
            Variant("Modifier\n    .size(100.dp)\n    .padding(16.dp)", Modifier.size(100.dp).padding(16.dp)) {
                Box(Modifier.fillMaxSize().background(fill))
            }
            Variant("Modifier\n    .background(bg)\n    .padding(16.dp)", Modifier.background(bg).padding(16.dp)) {
                Box(Modifier.size(100.dp))
            }
            Variant("Modifier\n    .padding(16.dp)\n    .background(bg)", Modifier.padding(16.dp).background(bg)) {
                Box(Modifier.size(100.dp))
            }
        }
    }
}

@Composable
private fun Variant(
    code: String,
    chain: Modifier,
    content: @Composable () -> Unit,
) {
    var node by remember { mutableStateOf(IntSize.Zero) }
    var inner by remember { mutableStateOf(IntSize.Zero) }
    val density = LocalDensity.current
    val nodeColor = MaterialTheme.colorScheme.error
    val contentColor = MaterialTheme.colorScheme.onSurface

    Column(
        modifier = Modifier.width(220.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(code, fontFamily = FontFamily.Monospace, style = MaterialTheme.typography.bodyLarge)
        Box(
            modifier = Modifier
                .onSizeChanged { node = it }
                .outline(nodeColor, dashed = true)
                .then(chain)
                .onSizeChanged { inner = it }
                .outline(contentColor, dashed = false),
        ) {
            content()
        }
        with(density) {
            Text(
                text = stringResource(Res.string.a7_node_size, node.width.toDp().value.toInt(), node.height.toDp().value.toInt()),
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = stringResource(Res.string.a7_content_size, inner.width.toDp().value.toInt(), inner.height.toDp().value.toInt()),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
