package band.effective.education.crossplatform.demo.d1

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import band.effective.education.crossplatform.stand.DemoScaffold
import lection1.shared.generated.resources.Res
import lection1.shared.generated.resources.a2_explanation
import lection1.shared.generated.resources.a2_branch_a
import lection1.shared.generated.resources.a2_branch_b
import lection1.shared.generated.resources.a2_hint
import lection1.shared.generated.resources.a2_act1_title
import lection1.shared.generated.resources.a2_act1_note
import lection1.shared.generated.resources.a2_act2_title
import lection1.shared.generated.resources.a2_act2_note
import lection1.shared.generated.resources.a2_first_call
import lection1.shared.generated.resources.a2_second_call
import lection1.shared.generated.resources.a2_inside_a
import lection1.shared.generated.resources.a2_inside_b
import lection1.shared.generated.resources.a2_clicks
import org.jetbrains.compose.resources.stringResource
import kotlin.random.Random

/**
 * A2 — память по позиции вызова.
 *
 * Демо из двух действий.
 *
 * Действие 1: одна и та же функция вызвана дважды — и у каждого вызова своя память.
 * Значит память привязана не к функции и не к имени переменной, а к месту вызова.
 *
 * Действие 2: та же функция в двух ветках `if`. ВАЖНО, проверено 22.08.2026:
 * при уходе с ветки её слоты выбрасываются, поэтому возврат назад даёт НОВОЕ число,
 * а не прежнее. Первоначальная формулировка демо («переключи обратно — вернётся
 * старое») оказалась неверной, и в конспекте она исправлена.
 */
@Composable
fun PositionalMemoryDemo() {
    var branchA by remember { mutableStateOf(true) }

    DemoScaffold(
        explanation = stringResource(Res.string.a2_explanation),
        toggleLabel = stringResource(if (branchA) Res.string.a2_branch_a else Res.string.a2_branch_b),
        toggleChecked = branchA,
        onToggle = { branchA = it },
        toggleHint = stringResource(Res.string.a2_hint),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = stringResource(Res.string.a2_act1_title),
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = stringResource(Res.string.a2_act1_note),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Slot(label = stringResource(Res.string.a2_first_call))
                Slot(label = stringResource(Res.string.a2_second_call))
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = stringResource(Res.string.a2_act2_title),
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = stringResource(Res.string.a2_act2_note),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                if (branchA) {
                    Slot(label = stringResource(Res.string.a2_inside_a))
                } else {
                    Slot(label = stringResource(Res.string.a2_inside_b))
                }
            }
        }
    }
}

/**
 * Обычная функция с обычным `remember`. Никакой разницы между вызовами в коде нет —
 * вся разница в том, из какого места её вызвали.
 */
@Composable
private fun Slot(label: String) {
    val id = remember { Random.nextInt(100, 999) }
    var clicks by remember { mutableStateOf(0) }

    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Column(modifier = Modifier.fillMaxWidth(0.5f)) {
                Text(label, style = MaterialTheme.typography.bodyLarge)
                Text(
                    text = stringResource(Res.string.a2_clicks, clicks),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Text(
                text = id.toString(),
                style = MaterialTheme.typography.headlineMedium,
            )
            Button(onClick = { clicks++ }) {
                Text("+1")
            }
        }
    }
}
