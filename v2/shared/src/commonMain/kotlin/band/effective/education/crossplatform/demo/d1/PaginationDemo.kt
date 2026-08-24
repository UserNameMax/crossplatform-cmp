package band.effective.education.crossplatform.demo.d1

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import band.effective.education.crossplatform.data.pokeapi.PokeApiClient
import band.effective.education.crossplatform.data.pokeapi.PokeListItemDto
import lection4.shared.generated.resources.Res
import lection4.shared.generated.resources.pg_end
import lection4.shared.generated.resources.pg_error
import lection4.shared.generated.resources.pg_error_fallback
import lection4.shared.generated.resources.pg_loaded
import lection4.shared.generated.resources.pg_loading_next
import lection4.shared.generated.resources.pg_requests
import org.jetbrains.compose.resources.stringResource

private const val PAGE_SIZE = 20

@Composable
fun PaginationDemo() {
    val client = remember { PokeApiClient() }
    val listState = rememberLazyListState()

    var entries by remember { mutableStateOf<List<PokeListItemDto>>(emptyList()) }
    var requestCount by remember { mutableStateOf(0) }
    var loading by remember { mutableStateOf(false) }
    var endReached by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    val shouldLoadMore by remember {
        derivedStateOf {
            val last = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            last >= entries.size - 3
        }
    }

    val networkErrorFallback = stringResource(Res.string.pg_error_fallback)

    suspend fun loadNextPage() {
        if (loading || endReached) return
        loading = true
        error = null
        try {
            val response = client.list(limit = PAGE_SIZE, offset = entries.size)
            val existingNames = entries.mapTo(HashSet()) { it.name }
            entries = entries + response.results.filter { it.name !in existingNames }
            requestCount++
            if (response.next == null) endReached = true
        } catch (e: Exception) {
            error = e.message ?: networkErrorFallback
        } finally {
            loading = false
        }
    }

    LaunchedEffect(Unit) { loadNextPage() }
    LaunchedEffect(shouldLoadMore) { if (shouldLoadMore) loadNextPage() }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
            Text(stringResource(Res.string.pg_loaded, entries.size), style = MaterialTheme.typography.titleMedium)
            Text(stringResource(Res.string.pg_requests, requestCount), style = MaterialTheme.typography.titleMedium)
        }
        error?.let {
            Text(stringResource(Res.string.pg_error, it), color = MaterialTheme.colorScheme.error)
        }
        LazyColumn(state = listState, modifier = Modifier.fillMaxWidth().height(360.dp)) {
            items(entries, key = { it.name }) { item ->
                Text(item.name, modifier = Modifier.padding(vertical = 8.dp))
                HorizontalDivider()
            }
            if (loading) {
                item {
                    Row(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                        CircularProgressIndicator()
                        Text(stringResource(Res.string.pg_loading_next), modifier = Modifier.padding(start = 8.dp))
                    }
                }
            }
            if (endReached) {
                item { Text(stringResource(Res.string.pg_end), modifier = Modifier.padding(16.dp)) }
            }
        }
    }
}
