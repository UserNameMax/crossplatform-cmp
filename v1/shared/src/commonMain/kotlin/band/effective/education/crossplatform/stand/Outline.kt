package band.effective.education.crossplatform.stand

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

/**
 * Служебная разметка для демо про модификаторы: пунктир — границы узла,
 * сплошная рамка — содержимое. Рисуется поверх, размер не меняет.
 */
fun Modifier.outline(color: Color, dashed: Boolean): Modifier = drawWithContent {
    drawContent()
    val stroke = 2.dp.toPx()
    drawRect(
        color = color,
        topLeft = Offset(stroke / 2, stroke / 2),
        size = Size(size.width - stroke, size.height - stroke),
        style = Stroke(
            width = stroke,
            pathEffect = if (dashed) PathEffect.dashPathEffect(floatArrayOf(8.dp.toPx(), 6.dp.toPx())) else null,
        ),
    )
}
