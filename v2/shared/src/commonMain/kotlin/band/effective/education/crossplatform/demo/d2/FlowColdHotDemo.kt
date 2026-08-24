package band.effective.education.crossplatform.demo.d2

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flow
import kotlin.random.Random
import lection4.shared.generated.resources.Res
import lection4.shared.generated.resources.conflation_emitted
import lection4.shared.generated.resources.conflation_note_off
import lection4.shared.generated.resources.conflation_note_on
import lection4.shared.generated.resources.conflation_received
import lection4.shared.generated.resources.conflation_title
import lection4.shared.generated.resources.conflation_toggle
import lection4.shared.generated.resources.flow_add_subscriber
import lection4.shared.generated.resources.flow_cold_label
import lection4.shared.generated.resources.flow_hot_label
import lection4.shared.generated.resources.flow_requests
import lection4.shared.generated.resources.flow_reset
import lection4.shared.generated.resources.flow_subscriber_pending
import lection4.shared.generated.resources.flow_subscriber_value
import lection4.shared.generated.resources.flow_subscribers
import lection4.shared.generated.resources.flow_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun FlowColdHotDemo() {
    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        ColdVsHotSection()
        ConflationSection()
    }
}

@Composable
private fun ColdVsHotSection() {
    var hot by remember { mutableStateOf(false) }
    var requestCount by remember { mutableStateOf(0) }
    var subscriberCount by remember { mutableStateOf(1) }

    val coldSource = remember {
        flow {
            requestCount++
            delay(30)
            emit(Random.nextInt(1, 1000))
        }
    }
    val hotSource = remember { MutableStateFlow(0) }
    var hotStarted by remember { mutableStateOf(false) }
    LaunchedEffect(hot) {
        // Источник заводится лениво — только когда его действительно выбрали,
        // иначе счётчик считает фоновую инициализацию, даже когда виден cold.
        if (hot && !hotStarted) {
            hotStarted = true
            requestCount++ // ровно один запрос на весь hot-источник, независимо от подписчиков
            delay(30)
            hotSource.value = Random.nextInt(1, 1000)
        }
    }

    val pendingLabel = stringResource(Res.string.flow_subscriber_pending)

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(stringResource(Res.string.flow_title), style = MaterialTheme.typography.titleMedium)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    stringResource(if (hot) Res.string.flow_hot_label else Res.string.flow_cold_label),
                    modifier = Modifier.weight(1f),
                )
                Switch(checked = hot, onCheckedChange = { hot = it })
            }
            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                Text(stringResource(Res.string.flow_subscribers, subscriberCount))
                Text(stringResource(Res.string.flow_requests, requestCount), style = MaterialTheme.typography.titleMedium)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                androidx.compose.material3.TextButton(onClick = { subscriberCount++ }) {
                    Text(stringResource(Res.string.flow_add_subscriber))
                }
                androidx.compose.material3.TextButton(onClick = {
                    subscriberCount = 1
                    requestCount = 0
                    hotStarted = false
                    hot = false
                }) {
                    Text(stringResource(Res.string.flow_reset))
                }
            }
            repeat(subscriberCount) { index ->
                val source: Flow<Int> = if (hot) hotSource else coldSource
                var value by remember(hot, index) { mutableStateOf<Int?>(null) }
                LaunchedEffect(hot, index) { source.collect { value = it } }
                Text(stringResource(Res.string.flow_subscriber_value, index + 1, value?.toString() ?: pendingLabel))
            }
        }
    }
}

@Composable
private fun ConflationSection() {
    var conflateOn by remember { mutableStateOf(false) }
    var emitted by remember { mutableStateOf(0) }
    var received by remember { mutableStateOf(0) }

    LaunchedEffect(conflateOn) {
        emitted = 0
        received = 0
        val fast = flow {
            repeat(60) {
                emit(it)
                emitted++
                delay(5) // быстрый источник
            }
        }
        val toCollect = if (conflateOn) fast.conflate() else fast
        toCollect.collect {
            delay(50) // медленный подписчик
            received++
        }
    }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(stringResource(Res.string.conflation_title), style = MaterialTheme.typography.titleMedium)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(stringResource(Res.string.conflation_toggle), modifier = Modifier.weight(1f))
                Switch(checked = conflateOn, onCheckedChange = { conflateOn = it })
            }
            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                Text(stringResource(Res.string.conflation_emitted, emitted))
                Text(stringResource(Res.string.conflation_received, received))
            }
            Text(
                stringResource(if (conflateOn) Res.string.conflation_note_on else Res.string.conflation_note_off),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
