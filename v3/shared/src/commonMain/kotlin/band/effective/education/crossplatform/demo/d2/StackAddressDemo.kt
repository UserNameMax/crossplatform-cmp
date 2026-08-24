package band.effective.education.crossplatform.demo.d2

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.padding
import band.effective.education.crossplatform.platform.platformFacts
import band.effective.education.crossplatform.stand.DemoScaffold
import lection5.shared.generated.resources.Res
import lection5.shared.generated.resources.sa_address
import lection5.shared.generated.resources.sa_explanation
import lection5.shared.generated.resources.sa_no_address
import lection5.shared.generated.resources.sa_pop
import lection5.shared.generated.resources.sa_push
import lection5.shared.generated.resources.sa_stack_title
import lection5.shared.generated.resources.sa_note
import org.jetbrains.compose.resources.stringResource

/**
 * B1. Расхождение вехи, показанное двумя колонками.
 *
 * Слева — стек Navigation 3: список **объектов**, каждый со своим типом и
 * полями. Справа — строка адреса, которую из этого списка собрали. Между ними
 * нет ничего, кроме пятнадцати строк кода: библиотека такой связи не даёт,
 * потому что у неё нет ни маршрутов, ни графа.
 *
 * У go_router всё наоборот: стек **и есть** список строк-путей, поэтому
 * адресная строка работает сама, а типизированного аргумента нет.
 */
@Composable
fun StackAddressDemo() {
    val facts = remember { platformFacts() }
    val stack = remember { mutableStateListOf<Int>() }

    DemoScaffold(explanation = stringResource(Res.string.sa_explanation)) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { stack.add((stack.size + 1) * 7) }) {
                    Text(stringResource(Res.string.sa_push))
                }
                Button(
                    onClick = { stack.removeLastOrNull() },
                    enabled = stack.isNotEmpty(),
                ) { Text(stringResource(Res.string.sa_pop)) }
            }

            Text(stringResource(Res.string.sa_stack_title), style = MaterialTheme.typography.titleMedium)
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(12.dp)) {
                    Text("Screen.List")
                    stack.forEach { id -> Text("Screen.Detail(id = $id)") }
                }
            }

            val address = if (stack.isEmpty()) "/" else "/record/" + stack.joinToString("/")
            Text(
                text = if (facts.hasPageAddress) {
                    stringResource(Res.string.sa_address, address)
                } else {
                    stringResource(Res.string.sa_no_address, address)
                },
                style = MaterialTheme.typography.titleMedium,
            )
            Text(stringResource(Res.string.sa_note))
        }
    }
}
