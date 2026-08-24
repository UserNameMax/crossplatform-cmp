package band.effective.education.crossplatform.stand

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape

/**
 * Служебный инструмент стенда. Студентам с экрана не показывается — см. SPEC 2.2.
 *
 * Считает, сколько раз композиция прошла через это место. Счётчик живёт в обычном
 * объекте, а не в snapshot-state: запись в snapshot-state во время композиции
 * зациклила бы рекомпозицию, а обычное поле её не триггерит.
 *
 * Если функция была пропущена, инкремента не происходит — ровно это и показывают демо.
 */
private class Count {
    var value: Int = 0
}

@Composable
fun rememberRecompositionCount(): Int {
    val holder = remember { Count() }
    holder.value++
    return holder.value
}

/** Крупная плашка со счётчиком. Крупная намеренно: пара идёт в трансляции. */
@Composable
fun RecompositionBadge(
    label: String,
    count: Int,
    modifier: Modifier = Modifier,
    emphasised: Boolean = false,
) {
    val background = if (emphasised) {
        MaterialTheme.colorScheme.errorContainer
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }
    val foreground = if (emphasised) {
        MaterialTheme.colorScheme.onErrorContainer
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }
    Text(
        text = "$label: $count",
        color = foreground,
        fontWeight = if (emphasised) FontWeight.Bold else FontWeight.Medium,
        style = if (emphasised) MaterialTheme.typography.titleLarge else MaterialTheme.typography.bodyLarge,
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(background)
            .padding(horizontal = 12.dp, vertical = 6.dp),
    )
}
