package band.effective.education.crossplatform.demo.d1

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import band.effective.education.crossplatform.domain.ScreenState
import lection4.shared.generated.resources.Res
import lection4.shared.generated.resources.ss_boolean_title
import lection4.shared.generated.resources.ss_data_placeholder
import lection4.shared.generated.resources.ss_data_value
import lection4.shared.generated.resources.ss_empty
import lection4.shared.generated.resources.ss_error_loading
import lection4.shared.generated.resources.ss_error_value
import lection4.shared.generated.resources.ss_has_data
import lection4.shared.generated.resources.ss_illegal_warning
import lection4.shared.generated.resources.ss_intro
import lection4.shared.generated.resources.ss_is_error
import lection4.shared.generated.resources.ss_is_loading
import lection4.shared.generated.resources.ss_label_content
import lection4.shared.generated.resources.ss_label_empty
import lection4.shared.generated.resources.ss_label_error
import lection4.shared.generated.resources.ss_label_loading
import lection4.shared.generated.resources.ss_no_network
import lection4.shared.generated.resources.ss_sealed_title
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun ScreenStateDemo() {
    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(stringResource(Res.string.ss_intro))

        var isLoading by remember { mutableStateOf(false) }
        var isError by remember { mutableStateOf(false) }
        var hasData by remember { mutableStateOf(false) }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(stringResource(Res.string.ss_boolean_title), style = MaterialTheme.typography.titleMedium)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = isLoading, onCheckedChange = { isLoading = it })
                    Text(stringResource(Res.string.ss_is_loading))
                    Checkbox(checked = isError, onCheckedChange = { isError = it })
                    Text(stringResource(Res.string.ss_is_error))
                    Checkbox(checked = hasData, onCheckedChange = { hasData = it })
                    Text(stringResource(Res.string.ss_has_data))
                }
                BooleanScreen(isLoading, isError, hasData)
            }
        }

        var sealedIndex by remember { mutableStateOf(0) }
        val noNetwork = stringResource(Res.string.ss_no_network)
        val dataPlaceholder = stringResource(Res.string.ss_data_placeholder)
        val sealedState: ScreenState<String> = remember(sealedIndex, dataPlaceholder, noNetwork) {
            when (sealedIndex) {
                0 -> ScreenState.Loading
                1 -> ScreenState.Content(dataPlaceholder)
                2 -> ScreenState.Empty
                else -> ScreenState.Error(noNetwork)
            }
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(stringResource(Res.string.ss_sealed_title), style = MaterialTheme.typography.titleMedium)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    val labels: List<StringResource> = listOf(
                        Res.string.ss_label_loading,
                        Res.string.ss_label_content,
                        Res.string.ss_label_empty,
                        Res.string.ss_label_error,
                    )
                    labels.forEachIndexed { i, label ->
                        androidx.compose.material3.TextButton(onClick = { sealedIndex = i }) {
                            Text(stringResource(label))
                        }
                    }
                }
                SealedScreen(sealedState)
            }
        }
    }
}

@Composable
private fun BooleanScreen(isLoading: Boolean, isError: Boolean, hasData: Boolean) {
    val illegal = isLoading && isError
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .background(if (illegal) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.surfaceVariant)
            .padding(12.dp),
    ) {
        if (isLoading) CircularProgressIndicator(modifier = Modifier.padding(end = 8.dp))
        Column {
            if (isError) Text(stringResource(Res.string.ss_error_loading), color = MaterialTheme.colorScheme.error)
            if (hasData) Text(stringResource(Res.string.ss_data_placeholder))
            if (!isLoading && !isError && !hasData) Text(stringResource(Res.string.ss_empty))
            if (illegal) {
                Text(
                    stringResource(Res.string.ss_illegal_warning),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    }
}

@Composable
private fun SealedScreen(state: ScreenState<String>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(12.dp),
    ) {
        when (state) {
            is ScreenState.Loading -> CircularProgressIndicator()
            is ScreenState.Content -> Text(stringResource(Res.string.ss_data_value, state.value))
            is ScreenState.Empty -> Text(stringResource(Res.string.ss_empty))
            is ScreenState.Error -> Text(
                stringResource(Res.string.ss_error_value, state.message),
                color = MaterialTheme.colorScheme.error,
            )
        }
    }
}
