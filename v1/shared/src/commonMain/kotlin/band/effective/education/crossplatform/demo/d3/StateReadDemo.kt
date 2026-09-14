package band.effective.education.crossplatform.demo.d3

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import band.effective.education.crossplatform.stand.DemoScaffold
import band.effective.education.crossplatform.stand.RecompositionBadge
import band.effective.education.crossplatform.stand.rememberRecompositionCount
import lection1.shared.generated.resources.Res
import lection1.shared.generated.resources.a15_child
import lection1.shared.generated.resources.a15_explanation
import lection1.shared.generated.resources.a15_hint_child
import lection1.shared.generated.resources.a15_hint_parent
import lection1.shared.generated.resources.a15_increment
import lection1.shared.generated.resources.a15_parent
import lection1.shared.generated.resources.a15_read_in_child
import lection1.shared.generated.resources.a15_read_in_parent
import lection1.shared.generated.resources.a15_same_value
import lection1.shared.generated.resources.a15_sibling
import lection1.shared.generated.resources.a15_value
import org.jetbrains.compose.resources.stringResource

/**
 * A15 — чтение записывает читателя, запись будит записанных.
 *
 * Слайды D3 «State изнутри» и D1 «Что такое состояние»: перезапустится ровно та группа,
 * где стоит чтение. Одно и то же значение читается либо в родителе (и передаётся
 * ребёнку числом), либо в самом ребёнке (ему передаётся State). Кнопка «записать то же»
 * проверяет вторую половину слайда: запись равного значения не будит никого.
 */
@Composable
fun StateReadDemo() {
    var readInChild by remember { mutableStateOf(false) }
    val count = remember { mutableIntStateOf(0) }

    DemoScaffold(
        explanation = stringResource(Res.string.a15_explanation),
        toggleLabel = stringResource(if (readInChild) Res.string.a15_read_in_child else Res.string.a15_read_in_parent),
        toggleChecked = readInChild,
        onToggle = { readInChild = it },
        toggleHint = stringResource(if (readInChild) Res.string.a15_hint_child else Res.string.a15_hint_parent),
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = { count.intValue++ }) {
                Text(stringResource(Res.string.a15_increment))
            }
            OutlinedButton(onClick = { count.intValue = count.intValue }) {
                Text(stringResource(Res.string.a15_same_value))
            }
        }
        if (readInChild) ParentPassesState(count) else ParentReads(count)
    }
}

@Composable
private fun ParentReads(count: MutableIntState) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        RecompositionBadge(stringResource(Res.string.a15_parent), rememberRecompositionCount(), emphasised = true)
        ChildGetsValue(value = count.intValue)                  // чтение здесь — в родителе
        Sibling()
    }
}

@Composable
private fun ParentPassesState(count: MutableIntState) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        RecompositionBadge(stringResource(Res.string.a15_parent), rememberRecompositionCount(), emphasised = true)
        ChildReads(count = count)                               // передали State, не значение
        Sibling()
    }
}

@Composable
private fun ChildGetsValue(value: Int) {
    Column {
        RecompositionBadge(stringResource(Res.string.a15_child), rememberRecompositionCount())
        Text(stringResource(Res.string.a15_value, value), style = MaterialTheme.typography.headlineSmall)
    }
}

@Composable
private fun ChildReads(count: MutableIntState) {
    Column {
        RecompositionBadge(stringResource(Res.string.a15_child), rememberRecompositionCount())
        Text(stringResource(Res.string.a15_value, count.intValue), style = MaterialTheme.typography.headlineSmall) // чтение здесь
    }
}

@Composable
private fun Sibling() {
    RecompositionBadge(stringResource(Res.string.a15_sibling), rememberRecompositionCount())
}
