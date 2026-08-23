package band.effective.education.crossplatform.demo.d2

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import band.effective.education.crossplatform.stand.DemoScaffold
import lection1.shared.generated.resources.Res
import lection1.shared.generated.resources.a7_explanation
import lection1.shared.generated.resources.a7_padding_first
import lection1.shared.generated.resources.a7_size_first
import org.jetbrains.compose.resources.stringResource

/**
 * A7 — порядок модификаторов.
 *
 * Показывает: цепочка модификаторов — тот же конвейер ограничений, что и между
 * узлами дерева, только внутри одного узла. Каждый модификатор получает ограничения
 * сверху, может их изменить и передать дальше.
 *
 * Рамка показывает, куда пришли ограничения на каждом шаге.
 */
@Composable
fun ModifierOrderDemo() {
    DemoScaffold(
        explanation = stringResource(Res.string.a7_explanation),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = stringResource(Res.string.a7_padding_first),
                    style = MaterialTheme.typography.bodyMedium,
                )
                Column(
                    modifier = Modifier
                        .border(2.dp, MaterialTheme.colorScheme.error)
                        .padding(24.dp)
                        .size(120.dp)
                        .background(MaterialTheme.colorScheme.primary),
                ) {}
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = stringResource(Res.string.a7_size_first),
                    style = MaterialTheme.typography.bodyMedium,
                )
                Column(
                    modifier = Modifier
                        .border(2.dp, MaterialTheme.colorScheme.error)
                        .size(120.dp)
                        .padding(24.dp)
                        .background(MaterialTheme.colorScheme.primary),
                ) {}
            }
        }
    }
}
