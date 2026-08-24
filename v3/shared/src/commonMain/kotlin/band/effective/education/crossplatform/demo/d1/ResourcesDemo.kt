package band.effective.education.crossplatform.demo.d1

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import band.effective.education.crossplatform.stand.DemoScaffold
import lection5.shared.generated.resources.Res
import lection5.shared.generated.resources.ic_star_filled
import lection5.shared.generated.resources.res_explanation
import lection5.shared.generated.resources.res_locale_note
import lection5.shared.generated.resources.res_ready_after
import lection5.shared.generated.resources.res_sample
import lection5.shared.generated.resources.res_waiting
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/**
 * A2. Ресурсы одинаковы в исходнике и разные в сборке.
 *
 * Главное, что показывает демо, — **не** «строки лежат в xml», а то, что на
 * web они приезжают асинхронно. На desktop и Android ресурс уже в бинарнике,
 * на web его надо скачать, и первый кадр может успеть отрисоваться раньше.
 * Счётчик внизу измеряет это прямо на паре.
 */
@Composable
fun ResourcesDemo() {
    var frames by remember { mutableStateOf(0) }
    var readyAt by remember { mutableStateOf<Int?>(null) }
    val sample = stringResource(Res.string.res_sample)

    // Считаем кадры до момента, когда строка перестала быть пустой.
    LaunchedEffect(sample) {
        if (readyAt == null && sample.isNotEmpty()) readyAt = frames
    }
    LaunchedEffect(Unit) {
        while (readyAt == null && frames < 600) {
            frames++
            kotlinx.coroutines.delay(16)
        }
    }

    DemoScaffold(explanation = stringResource(Res.string.res_explanation)) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
            ) {
                Image(
                    painter = painterResource(Res.drawable.ic_star_filled),
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                )
                Text(sample, style = MaterialTheme.typography.headlineSmall)
            }
            Text(
                text = readyAt?.let { stringResource(Res.string.res_ready_after, it) }
                    ?: stringResource(Res.string.res_waiting),
                style = MaterialTheme.typography.titleMedium,
            )
            Text(stringResource(Res.string.res_locale_note))
        }
    }
}
