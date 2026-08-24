package band.effective.education.crossplatform.demo.d1

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import band.effective.education.crossplatform.stand.DemoScaffold
import lection5.shared.generated.resources.Res
import lection5.shared.generated.resources.ad_explanation
import lection5.shared.generated.resources.ad_layout_single
import lection5.shared.generated.resources.ad_layout_two
import lection5.shared.generated.resources.ad_pane_detail
import lection5.shared.generated.resources.ad_pane_list
import lection5.shared.generated.resources.ad_toggle
import lection5.shared.generated.resources.ad_toggle_hint
import lection5.shared.generated.resources.ad_width
import org.jetbrains.compose.resources.stringResource

private val Breakpoint = 840.dp

/**
 * A3. Ширина, класс раскладки и сама раскладка — на одном экране.
 *
 * Тумблер переключает не «телефон/планшет», а **источник решения**: порог по
 * доступной ширине против порога, прибитого к таргету. На узком окне desktop
 * второй вариант честно ломается — desktop же, значит «широкий».
 */
@Composable
fun AdaptiveDemo() {
    var byTarget by remember { mutableStateOf(false) }

    DemoScaffold(
        explanation = stringResource(Res.string.ad_explanation),
        toggleLabel = stringResource(Res.string.ad_toggle),
        toggleChecked = byTarget,
        onToggle = { byTarget = it },
        toggleHint = stringResource(Res.string.ad_toggle_hint),
    ) {
        BoxWithConstraints(Modifier.fillMaxWidth()) {
            // maxWidth снимается здесь, а не внутри Column: у Column свой
            // scope, и неявный получатель BoxWithConstraints там уже не виден.
            val width = maxWidth
            val twoPane = if (byTarget) true else width >= Breakpoint
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    stringResource(Res.string.ad_width, width.value.toInt()),
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    stringResource(
                        if (twoPane) Res.string.ad_layout_two else Res.string.ad_layout_single,
                    ),
                    style = MaterialTheme.typography.titleMedium,
                )
                if (twoPane) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Pane(stringResource(Res.string.ad_pane_list), Modifier.weight(1f))
                        Pane(stringResource(Res.string.ad_pane_detail), Modifier.weight(1.6f))
                    }
                } else {
                    Pane(stringResource(Res.string.ad_pane_list), Modifier.fillMaxWidth())
                }
            }
        }
    }
}

@Composable
private fun Pane(title: String, modifier: Modifier = Modifier) {
    Card(modifier) {
        Text(title, modifier = Modifier.padding(24.dp), style = MaterialTheme.typography.titleMedium)
    }
}
