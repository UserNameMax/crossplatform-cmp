package band.effective.education.crossplatform.demo.d2

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import lection1.shared.generated.resources.Res
import lection1.shared.generated.resources.a8_explanation
import lection1.shared.generated.resources.a8_crash_on
import lection1.shared.generated.resources.a8_crash_off
import lection1.shared.generated.resources.a8_hint
import lection1.shared.generated.resources.a8_code_title
import lection1.shared.generated.resources.a8_exception_title
import org.jetbrains.compose.resources.stringResource

/**
 * A8 — бесконечная высота.
 *
 * Отступление от SPEC, сознательное. В Compose нет error boundary: исключение в layout
 * роняет приложение целиком, «перехватить и продолжить» не выйдет. Поэтому на паре
 * показывается код и настоящий текст исключения, снятый заранее.
 *
 * Тумблер «уронить» существует ровно для того, чтобы этот текст снять — один раз,
 * вне пары. По умолчанию выключен.
 */
/**
 * Единственная строка стенда, которой НЕ место в ресурсах: сюда вставляется дословный
 * текст исключения из консоли. Переводить его нельзя — на паре читается оригинал.
 */
private const val CAPTURED_MESSAGE = "ТЕКСТ ИСКЛЮЧЕНИЯ ПОКА НЕ СНЯТ.\n\n" +
    "Как снять: включить тумблер ниже, запустить на desktop, скопировать сообщение " +
    "из консоли целиком и вставить сюда, в CAPTURED_MESSAGE.\n\n" +
    "Пересказывать своими словами нельзя — на паре читается настоящий текст."

@Composable
fun InfiniteHeightDemo() {
    var crash by remember { mutableStateOf(false) }

    DemoScaffold(
        explanation = stringResource(Res.string.a8_explanation),
        toggleLabel = stringResource(
            if (crash) Res.string.a8_crash_on else Res.string.a8_crash_off,
        ),
        toggleChecked = crash,
        onToggle = { crash = it },
        toggleHint = stringResource(Res.string.a8_hint),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = stringResource(Res.string.a8_code_title),
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = "Column(Modifier.verticalScroll(state)) {\n" +
                    "    LazyColumn {\n" +
                    "        items(50) { Text(\"item \$it\") }\n" +
                    "    }\n" +
                    "}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(12.dp),
            )

            Text(
                text = stringResource(Res.string.a8_exception_title),
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = CAPTURED_MESSAGE,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.errorContainer)
                    .padding(12.dp),
                color = MaterialTheme.colorScheme.onErrorContainer,
            )

            if (crash) {
                // Прокручиваемый внутри прокручиваемого того же направления.
                // Внешний отдаёт бесконечную максимальную высоту, внутренний на это
                // ругается и роняет приложение. Именно этот случай накроет их на ЛР1.
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                ) {
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        items(50) { index ->
                            // листинг кода, не подпись интерфейса
                            Text("item $index")
                        }
                    }
                }
            }
        }
    }
}
