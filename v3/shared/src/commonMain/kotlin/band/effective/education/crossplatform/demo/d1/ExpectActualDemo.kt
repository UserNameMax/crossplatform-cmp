package band.effective.education.crossplatform.demo.d1

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
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
import band.effective.education.crossplatform.platform.platformFacts
import band.effective.education.crossplatform.stand.DemoScaffold
import band.effective.education.crossplatform.store.createKeyValueStore
import lection5.shared.generated.resources.Res
import lection5.shared.generated.resources.ea_binding
import lection5.shared.generated.resources.ea_explanation
import lection5.shared.generated.resources.ea_key_label
import lection5.shared.generated.resources.ea_note
import lection5.shared.generated.resources.ea_read
import lection5.shared.generated.resources.ea_read_result
import lection5.shared.generated.resources.ea_read_empty
import lection5.shared.generated.resources.ea_storage
import lection5.shared.generated.resources.ea_target
import lection5.shared.generated.resources.ea_write
import org.jetbrains.compose.resources.stringResource

/**
 * A1. Одно и то же демо на трёх таргетах отвечает тремя разными строками —
 * и это единственное, что нужно понять про `expect/actual` на уровне «как
 * пользоваться».
 */
@Composable
fun ExpectActualDemo() {
    val facts = remember { platformFacts() }
    val store = remember { createKeyValueStore() }
    var value by remember { mutableStateOf("") }
    var readBack by remember { mutableStateOf<String?>(null) }

    DemoScaffold(explanation = stringResource(Res.string.ea_explanation)) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(stringResource(Res.string.ea_target, facts.targetName), style = MaterialTheme.typography.titleMedium)
            Text(stringResource(Res.string.ea_storage, facts.storageKind))
            Text(stringResource(Res.string.ea_binding, facts.bindingKind))

            OutlinedTextField(
                value = value,
                onValueChange = { value = it },
                label = { Text(stringResource(Res.string.ea_key_label)) },
                modifier = Modifier.fillMaxWidth(),
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { store.putString("demo", value) }) {
                    Text(stringResource(Res.string.ea_write))
                }
                Button(onClick = { readBack = store.getString("demo") }) {
                    Text(stringResource(Res.string.ea_read))
                }
            }
            Text(
                readBack?.let { stringResource(Res.string.ea_read_result, it) }
                    ?: stringResource(Res.string.ea_read_empty),
                style = MaterialTheme.typography.titleMedium,
            )
            Text(stringResource(Res.string.ea_note), style = MaterialTheme.typography.bodyMedium)
        }
    }
}
