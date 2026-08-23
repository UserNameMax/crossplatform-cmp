package band.effective.education.crossplatform.demo.d1

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.padding
import band.effective.education.crossplatform.stand.DemoScaffold
import lection1.shared.generated.resources.Res
import lection1.shared.generated.resources.a4_explanation
import lection1.shared.generated.resources.a4_first_field
import lection1.shared.generated.resources.a4_second_field
import lection1.shared.generated.resources.a4_state_lives_here
import lection1.shared.generated.resources.a4_first_value
import lection1.shared.generated.resources.a4_second_value
import org.jetbrains.compose.resources.stringResource

/**
 * A4 — подъём состояния.
 *
 * Показывает: компонент без собственного состояния переиспользуется в двух местах,
 * состояния не мешают друг другу, а лежат в одном месте у родителя.
 *
 * Правило в две строки: состояние вверх, события вниз.
 */
@Composable
fun StateHoistingDemo() {
    var first by remember { mutableStateOf("") }
    var second by remember { mutableStateOf("") }

    DemoScaffold(
        explanation = stringResource(Res.string.a4_explanation),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            LabeledField(
                label = stringResource(Res.string.a4_first_field),
                value = first,
                onValueChange = { first = it },
            )
            LabeledField(
                label = stringResource(Res.string.a4_second_field),
                value = second,
                onValueChange = { second = it },
            )

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(
                        text = stringResource(Res.string.a4_state_lives_here),
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Text(
                        text = stringResource(Res.string.a4_first_value, first),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    Text(
                        text = stringResource(Res.string.a4_second_value, second),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }
        }
    }
}

@Composable
private fun LabeledField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
    )
}
