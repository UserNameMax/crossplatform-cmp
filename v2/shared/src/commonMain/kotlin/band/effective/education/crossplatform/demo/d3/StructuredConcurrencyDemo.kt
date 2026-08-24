package band.effective.education.crossplatform.demo.d3

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import band.effective.education.crossplatform.data.pokeapi.PokeApiClient
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import lection4.shared.generated.resources.Res
import lection4.shared.generated.resources.sc_back
import lection4.shared.generated.resources.sc_detail_loading
import lection4.shared.generated.resources.sc_in_flight
import lection4.shared.generated.resources.sc_leak_label
import lection4.shared.generated.resources.sc_log_arrived_dead
import lection4.shared.generated.resources.sc_log_arrived_ontime
import lection4.shared.generated.resources.sc_log_empty
import lection4.shared.generated.resources.sc_log_title
import lection4.shared.generated.resources.sc_open_detail
import lection4.shared.generated.resources.sc_structured_label
import org.jetbrains.compose.resources.stringResource

@OptIn(DelicateCoroutinesApi::class)
@Composable
fun StructuredConcurrencyDemo() {
    val client = remember { PokeApiClient() }
    var useGlobalScope by remember { mutableStateOf(true) } // старт «сломанный» — это вопрос, который вешает демо
    var showDetail by remember { mutableStateOf(false) }
    var inFlight by remember { mutableStateOf(0) }
    val log = remember { mutableStateListOf<String>() }
    val arrivedDead = stringResource(Res.string.sc_log_arrived_dead)
    val arrivedOnTime = stringResource(Res.string.sc_log_arrived_ontime)

    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                stringResource(if (useGlobalScope) Res.string.sc_leak_label else Res.string.sc_structured_label),
                modifier = Modifier.weight(1f),
            )
            Switch(checked = !useGlobalScope, onCheckedChange = { useGlobalScope = !it })
        }

        Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
            Text(stringResource(Res.string.sc_in_flight, inFlight), style = MaterialTheme.typography.titleMedium)
        }

        if (!showDetail) {
            Button(onClick = { showDetail = true }) { Text(stringResource(Res.string.sc_open_detail)) }
        } else {
            DetailScreen(
                client = client,
                useGlobalScope = useGlobalScope,
                arrivedDeadMessage = arrivedDead,
                arrivedOnTimeMessage = arrivedOnTime,
                onBack = { showDetail = false },
                onRequestStart = { inFlight++ },
                onRequestEnd = { inFlight-- },
                onLog = { entry -> log.add(0, entry) },
            )
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(stringResource(Res.string.sc_log_title), style = MaterialTheme.typography.titleSmall)
                if (log.isEmpty()) Text(stringResource(Res.string.sc_log_empty))
                log.take(6).forEach { Text(it, style = MaterialTheme.typography.bodySmall) }
            }
        }
    }
}

@Composable
private fun DetailScreen(
    client: PokeApiClient,
    useGlobalScope: Boolean,
    arrivedDeadMessage: String,
    arrivedOnTimeMessage: String,
    onBack: () -> Unit,
    onRequestStart: () -> Unit,
    onRequestEnd: () -> Unit,
    onLog: (String) -> Unit,
) {
    LaunchedEffect(Unit) {
        onRequestStart()
        suspend fun fetchDetail() {
            try {
                delay(2500) // делаем окно ухода воспроизводимым руками
                client.list(limit = 1, offset = 0) // настоящий сетевой запрос
                onLog(if (useGlobalScope) arrivedDeadMessage else arrivedOnTimeMessage)
            } finally {
                onRequestEnd()
            }
        }
        if (useGlobalScope) {
            GlobalScope.launch { fetchDetail() }
        } else {
            fetchDetail()
        }
    }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(stringResource(Res.string.sc_detail_loading))
        Button(onClick = onBack) { Text(stringResource(Res.string.sc_back)) }
    }
}
