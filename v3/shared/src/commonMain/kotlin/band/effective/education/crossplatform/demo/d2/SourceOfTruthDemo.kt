package band.effective.education.crossplatform.demo.d2

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import band.effective.education.crossplatform.stand.DemoScaffold
import band.effective.education.crossplatform.store.createKeyValueStore
import lection5.shared.generated.resources.Res
import lection5.shared.generated.resources.sot_add
import lection5.shared.generated.resources.sot_explanation
import lection5.shared.generated.resources.sot_favorites_empty
import lection5.shared.generated.resources.sot_favorites_title
import lection5.shared.generated.resources.sot_offline
import lection5.shared.generated.resources.sot_restart
import lection5.shared.generated.resources.sot_toggle
import lection5.shared.generated.resources.sot_toggle_hint
import org.jetbrains.compose.resources.stringResource

/**
 * B2. Ловушка «избранное = множество идентификаторов», показанная тумблером.
 *
 * Слева от тумблера в сторе лежат только id, справа — id вместе с именем.
 * Пока сеть есть, разницы не видно: имена всё равно приезжают. Выключаем сеть
 * («офлайн» — второй тумблер) — и первый вариант превращается в список чисел.
 *
 * Мысль блока: **офлайн — это не кэш, а вопрос о том, что считается источником
 * правды.** Пока источник — сеть, стор украшение, и без сети он бесполезен.
 */
@Composable
fun SourceOfTruthDemo() {
    val store = remember { createKeyValueStore() }
    var idsOnly by remember { mutableStateOf(true) }
    var offline by remember { mutableStateOf(false) }
    var saved by remember { mutableStateOf(store.getString(KEY).orEmpty()) }
    var shown by remember { mutableStateOf(listOf<String>()) }

    DemoScaffold(
        explanation = stringResource(Res.string.sot_explanation),
        toggleLabel = stringResource(Res.string.sot_toggle),
        toggleChecked = idsOnly,
        onToggle = { idsOnly = it; store.remove(KEY); saved = ""; shown = emptyList() },
        toggleHint = stringResource(Res.string.sot_toggle_hint),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = {
                val next = NAMES[saved.split(";").filter { it.isNotBlank() }.size % NAMES.size]
                val entry = if (idsOnly) next.first.toString() else "${next.first}|${next.second}"
                saved = (saved.split(";").filter { it.isNotBlank() } + entry).joinToString(";")
                store.putString(KEY, saved)
            }) { Text(stringResource(Res.string.sot_add)) }

            Button(onClick = {
                // «Перезапуск»: читаем только то, что лежит в сторе.
                val rows = store.getString(KEY).orEmpty().split(";").filter { it.isNotBlank() }
                shown = rows.map { row ->
                    val parts = row.split("|")
                    when {
                        parts.size > 1 -> parts[1]
                        offline -> "#" + parts[0]
                        else -> NAMES.first { it.first.toString() == parts[0] }.second
                    }
                }
            }) { Text(stringResource(Res.string.sot_restart)) }

            Button(onClick = { offline = !offline }) {
                Text(stringResource(Res.string.sot_offline, offline.toString()))
            }

            Text(stringResource(Res.string.sot_favorites_title), style = MaterialTheme.typography.titleMedium)
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(12.dp)) {
                    if (shown.isEmpty()) {
                        Text(stringResource(Res.string.sot_favorites_empty))
                    } else {
                        shown.forEach { Text(it) }
                    }
                }
            }
        }
    }
}

private const val KEY = "sot-favorites"

private val NAMES = listOf(
    1 to "bulbasaur",
    4 to "charmander",
    7 to "squirtle",
    25 to "pikachu",
)
