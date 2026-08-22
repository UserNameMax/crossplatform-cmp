package band.effective.education.crossplatform.demo.d1

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import lection1.shared.generated.resources.a3_explanation
import lection1.shared.generated.resources.a3_with_remember
import lection1.shared.generated.resources.a3_without_remember
import lection1.shared.generated.resources.a3_hint_with
import lection1.shared.generated.resources.a3_hint_without
import lection1.shared.generated.resources.a3_input_label
import lection1.shared.generated.resources.a3_state_content
import org.jetbrains.compose.resources.stringResource

/**
 * A3 — remember и ввод.
 *
 * Показывает: функция прогоняется заново целиком, значит всё, созданное в её теле,
 * создаётся снова. Без remember состояние поля пересоздаётся пустым на каждой
 * рекомпозиции, и ввод физически невозможен.
 *
 * Эффект виден с первого нажатия клавиши и не требует пояснений.
 */
@Composable
fun RememberInputDemo() {
    var useRemember by remember { mutableStateOf(true) }

    DemoScaffold(
        explanation = stringResource(Res.string.a3_explanation),
        toggleLabel = stringResource(
            if (useRemember) Res.string.a3_with_remember else Res.string.a3_without_remember,
        ),
        toggleChecked = useRemember,
        onToggle = { useRemember = it },
        toggleHint = stringResource(
            if (useRemember) Res.string.a3_hint_with else Res.string.a3_hint_without,
        ),
    ) {
        InputField(useRemember = useRemember)
    }
}

/**
 * Вынесено в отдельную функцию не для красоты: состояние должно создаваться в той же
 * области рекомпозиции, в которой оно читается. Иначе перевызова не произойдёт и
 * вариант «без remember» будет вести себя как вариант «с remember».
 */
@Composable
private fun InputField(useRemember: Boolean) {
    val remembered = remember { mutableStateOf("") }
    val recreated = mutableStateOf("")

    val field = if (useRemember) remembered else recreated

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        OutlinedTextField(
            value = field.value,
            onValueChange = { field.value = it },
            label = { Text(stringResource(Res.string.a3_input_label)) },
            modifier = Modifier.fillMaxWidth(),
        )
        Text(
            text = stringResource(Res.string.a3_state_content, field.value),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
