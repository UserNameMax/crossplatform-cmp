package band.effective.education.crossplatform.demo.d3

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import band.effective.education.crossplatform.data.DemoItem
import band.effective.education.crossplatform.data.demoItems
import band.effective.education.crossplatform.stand.DemoScaffold
import lection1.shared.generated.resources.Res
import lection1.shared.generated.resources.a10_explanation
import lection1.shared.generated.resources.a10_with_key
import lection1.shared.generated.resources.a10_without_key
import lection1.shared.generated.resources.a10_hint_with
import lection1.shared.generated.resources.a10_hint_without
import lection1.shared.generated.resources.a10_insert
import lection1.shared.generated.resources.a10_new_item
import lection1.shared.generated.resources.a10_inserted_first
import lection1.shared.generated.resources.a10_expand
import lection1.shared.generated.resources.a10_collapse
import lection1.shared.generated.resources.a10_expanded_note
import org.jetbrains.compose.resources.stringResource

/**
 * A10 — ключи в списке.
 *
 * Показывает: без ключа идентичность узла берётся из позиции вызова, поэтому вставка
 * в начало сдвигает позиции — и локальное состояние карточек уезжает вместе с ними.
 *
 * На этом демо держится мостик к Л2 Саши: Element-дерево во Flutter решает ту же
 * задачу «это тот же узел?» другим способом, но ключи нужны по одной причине.
 */
@Composable
fun ListKeysDemo() {
    var useKeys by remember { mutableStateOf(false) }
    var list by remember { mutableStateOf(demoItems.take(6)) }
    var inserted by remember { mutableStateOf(0) }

    DemoScaffold(
        explanation = stringResource(Res.string.a10_explanation),
        toggleLabel = stringResource(if (useKeys) Res.string.a10_with_key else Res.string.a10_without_key),
        toggleChecked = useKeys,
        onToggle = { useKeys = it },
        toggleHint = stringResource(
            if (useKeys) Res.string.a10_hint_with else Res.string.a10_hint_without,
        ),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            val newItemTitle = stringResource(Res.string.a10_new_item, inserted + 1)
            val insertedNote = stringResource(Res.string.a10_inserted_first)
            Button(
                onClick = {
                    inserted++
                    list = listOf(
                        DemoItem(
                            id = 1000 + inserted,
                            title = newItemTitle,
                            subtitle = insertedNote,
                        ),
                    ) + list
                },
            ) {
                Text(stringResource(Res.string.a10_insert))
            }

            LazyColumn(
                modifier = Modifier.fillMaxWidth().height(360.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                if (useKeys) {
                    items(items = list, key = { it.id }) { item ->
                        ExpandableCard(item)
                    }
                } else {
                    items(items = list) { item ->
                        ExpandableCard(item)
                    }
                }
            }
        }
    }
}

@Composable
private fun ExpandableCard(item: DemoItem) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = if (expanded) {
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        } else {
            CardDefaults.cardColors()
        },
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column {
                    Text(item.title, style = MaterialTheme.typography.titleMedium)
                    Text(item.subtitle, style = MaterialTheme.typography.bodyMedium)
                }
                Button(onClick = { expanded = !expanded }) {
                    Text(stringResource(if (expanded) Res.string.a10_collapse else Res.string.a10_expand))
                }
            }
            if (expanded) {
                Text(
                    text = stringResource(Res.string.a10_expanded_note),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp),
                )
            }
        }
    }
}
