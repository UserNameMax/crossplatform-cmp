package band.effective.education.crossplatform.demo.d3

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import band.effective.education.crossplatform.platform.platformFacts
import band.effective.education.crossplatform.stand.DemoScaffold
import lection5.shared.generated.resources.Res
import lection5.shared.generated.resources.bind_dumps_title
import lection5.shared.generated.resources.bind_explanation
import lection5.shared.generated.resources.bind_live_title
import lection5.shared.generated.resources.bind_loading
import lection5.shared.generated.resources.bind_toggle
import lection5.shared.generated.resources.bind_toggle_hint
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

/**
 * C1 — гвоздь пары. Артефакт обязателен, и он здесь двойной.
 *
 * **Живая часть**: таргет рассказывает о себе сам, в рантайме. На JVM это
 * рефлексия, которая находит класс `PlatformFacts_jvmKt` и перечисляет его
 * методы. На web — свойства `window`. На Kotlin/Native не находится ничего,
 * и это не поломка демо, а сам факт: перечислять во время работы нечего,
 * потому что перечислять умеет виртуальная машина, а её нет.
 *
 * **Снятая часть**: дампы сборки — таблица импортов wasm-модуля, заголовок
 * сгенерированного `.framework`, вывод `javap`. Сняты заранее командами,
 * записанными в самом дампе, — чтобы на паре не зависеть от инструментов.
 */
@OptIn(ExperimentalResourceApi::class)
@Composable
fun BindingDemo() {
    val facts = remember { platformFacts() }
    var showDumps by remember { mutableStateOf(false) }

    val dumps by produceState(initialValue = null as String?) {
        value = Res.readBytes("files/binding-dumps.txt").decodeToString()
    }

    DemoScaffold(
        explanation = stringResource(Res.string.bind_explanation),
        toggleLabel = stringResource(Res.string.bind_toggle),
        toggleChecked = showDumps,
        onToggle = { showDumps = it },
        toggleHint = stringResource(Res.string.bind_toggle_hint),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(stringResource(Res.string.bind_live_title), style = MaterialTheme.typography.titleMedium)
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(12.dp)) {
                    Text(facts.targetName, style = MaterialTheme.typography.titleSmall)
                    Text(facts.bindingKind, style = MaterialTheme.typography.bodyMedium)
                    facts.selfReport().forEach {
                        Text(it, fontFamily = FontFamily.Monospace, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            if (showDumps) {
                Text(stringResource(Res.string.bind_dumps_title), style = MaterialTheme.typography.titleMedium)
                Card(Modifier.fillMaxWidth()) {
                    Text(
                        text = dumps ?: stringResource(Res.string.bind_loading),
                        fontFamily = FontFamily.Monospace,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(12.dp).horizontalScroll(rememberScrollState()),
                    )
                }
            }
        }
    }
}
