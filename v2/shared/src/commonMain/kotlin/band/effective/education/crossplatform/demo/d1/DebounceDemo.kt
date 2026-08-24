package band.effective.education.crossplatform.demo.d1

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import lection4.shared.generated.resources.Res
import lection4.shared.generated.resources.db_changes
import lection4.shared.generated.resources.db_label
import lection4.shared.generated.resources.db_requests
import lection4.shared.generated.resources.db_toggle
import org.jetbrains.compose.resources.stringResource

@OptIn(FlowPreview::class)
@Composable
fun DebounceDemo() {
    var query by remember { mutableStateOf("") }
    var debounceOn by remember { mutableStateOf(false) }
    var charsTyped by remember { mutableStateOf(0) }
    var requestsSent by remember { mutableStateOf(0) }

    LaunchedEffect(debounceOn) {
        requestsSent = 0
        val base = snapshotFlow { query }.drop(1).distinctUntilChanged()
        val source = if (debounceOn) base.debounce(400) else base
        source.collect { requestsSent++ }
    }

    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(stringResource(Res.string.db_toggle), modifier = Modifier.weight(1f))
            Switch(checked = debounceOn, onCheckedChange = {
                debounceOn = it
                charsTyped = 0
            })
        }
        OutlinedTextField(
            value = query,
            onValueChange = {
                query = it
                charsTyped++
            },
            label = { Text(stringResource(Res.string.db_label)) },
            modifier = Modifier.fillMaxWidth(),
        )
        Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
            Text(stringResource(Res.string.db_changes, charsTyped), style = MaterialTheme.typography.titleMedium)
            Text(stringResource(Res.string.db_requests, requestsSent), style = MaterialTheme.typography.titleMedium)
        }
    }
}
