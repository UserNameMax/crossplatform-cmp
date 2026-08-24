package band.effective.education.crossplatform.stand

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Общая рамка демо: пояснение, тумблер «сломано / починено» и содержимое.
 *
 * Тумблер здесь не для красоты. Правка кода с пересборкой на живой онлайн-паре —
 * это полторы минуты тишины, за которые аудитория расходится (SPEC 2.1).
 */
@Composable
fun DemoScaffold(
    explanation: String,
    toggleLabel: String? = null,
    toggleChecked: Boolean = false,
    onToggle: (Boolean) -> Unit = {},
    toggleHint: String? = null,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = explanation,
            style = MaterialTheme.typography.bodyLarge,
        )
        if (toggleLabel != null) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Switch(checked = toggleChecked, onCheckedChange = onToggle)
                Column {
                    Text(
                        text = toggleLabel,
                        style = MaterialTheme.typography.titleMedium,
                    )
                    if (toggleHint != null) {
                        Text(
                            text = toggleHint,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }
        HorizontalDivider()
        content()
    }
}
