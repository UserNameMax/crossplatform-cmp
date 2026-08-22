package band.effective.education.crossplatform.demo.d3

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import lection1.shared.generated.resources.a9_explanation
import lection1.shared.generated.resources.a9_wrapper_kept
import lection1.shared.generated.resources.a9_wrapper_recreated
import lection1.shared.generated.resources.a9_hint_kept
import lection1.shared.generated.resources.a9_hint_recreated
import lection1.shared.generated.resources.a9_search
import lection1.shared.generated.resources.a9_by_list
import lection1.shared.generated.resources.a9_by_list_note
import lection1.shared.generated.resources.a9_by_wrapper
import lection1.shared.generated.resources.a9_by_wrapper_note
import lection1.shared.generated.resources.a9_redrawn
import org.jetbrains.compose.resources.stringResource
import band.effective.education.crossplatform.stand.RecompositionBadge
import band.effective.education.crossplatform.stand.rememberRecompositionCount

/**
 * A9 — главный артефакт лекции. Проходит через всю пару: ставит вопрос на входе,
 * отвечает на него в D3, закрывает лекцию на выходе.
 *
 * ВАЖНО. Демо построено на замере, а не на общем месте из интернета.
 * Проверено 22.08.2026 на Kotlin 2.4.10 / CMP 1.11.1 со strong skipping:
 *
 *  - `List` сравнивается ПО СОДЕРЖИМОМУ. Пересозданный, но равный список пропуск
 *    не ломает — вопреки половине статей про стабильность.
 *  - А вот собственный класс-обёртка с нестабильным полем сравнивается ПО ССЫЛКЕ.
 *    Пересоздание такой обёртки в теле composable ломает пропуск на каждом кадре.
 *
 * Отсюда вывод лекции: дело не в том, «пересоздал ты объект или нет», а в том,
 * чем именно тебя сравнивают. Это решает стабильность типа.
 */

/**
 * Обёртка вокруг списка — ровно то, что студенты напишут в В2, когда заведут
 * состояние экрана. Поле нестабильного типа делает нестабильным весь класс.
 */
class ScreenState(val items: List<DemoItem>)

@Composable
fun SearchRecompositionDemo() {
    var query by remember { mutableStateOf("") }
    var keepWrapper by remember { mutableStateOf(false) }

    val source = remember { demoItems.filter { it.visible }.take(4) }
    val rememberedState = remember { ScreenState(source) }

    DemoScaffold(
        explanation = stringResource(Res.string.a9_explanation),
        toggleLabel = stringResource(
            if (keepWrapper) Res.string.a9_wrapper_kept else Res.string.a9_wrapper_recreated,
        ),
        toggleChecked = keepWrapper,
        onToggle = { keepWrapper = it },
        toggleHint = stringResource(
            if (keepWrapper) Res.string.a9_hint_kept else Res.string.a9_hint_recreated,
        ),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                label = { Text(stringResource(Res.string.a9_search)) },
                modifier = Modifier.fillMaxWidth(),
            )

            // Оба значения считаются здесь же, где читается query — в одной с ним
            // области рекомпозиции. Иначе перевызова не будет и демо ничего не покажет.
            val recreatedList = source.map { it }
            val state = if (keepWrapper) rememberedState else ScreenState(source)

            ByList(items = recreatedList)
            ByWrapper(state = state)
        }
    }
}

/**
 * Получает список, который пересоздаётся на каждой рекомпозиции.
 * Счётчик стоит на месте: `List` сравнивают по содержимому, содержимое то же.
 */
@Composable
private fun ByList(items: List<DemoItem>) {
    Section(
        title = stringResource(Res.string.a9_by_list),
        note = stringResource(Res.string.a9_by_list_note),
        count = rememberRecompositionCount(),
        emphasised = false,
        items = items,
    )
}

/**
 * Получает обёртку. Когда её пересоздают, счётчик бежит: класс нестабилен,
 * значит сравнение идёт по ссылке, а ссылка каждый раз новая.
 */
@Composable
private fun ByWrapper(state: ScreenState) {
    Section(
        title = stringResource(Res.string.a9_by_wrapper),
        note = stringResource(Res.string.a9_by_wrapper_note),
        count = rememberRecompositionCount(),
        emphasised = true,
        items = state.items,
    )
}

@Composable
private fun Section(
    title: String,
    note: String,
    count: Int,
    emphasised: Boolean,
    items: List<DemoItem>,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            RecompositionBadge(
                label = stringResource(Res.string.a9_redrawn),
                count = count,
                emphasised = emphasised,
            )
        }
        Text(
            text = note,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        items.forEach { item ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(item.title, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}
