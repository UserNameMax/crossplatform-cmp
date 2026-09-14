package band.effective.education.crossplatform.demo.d1

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import band.effective.education.crossplatform.stand.DemoScaffold
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import lection1.shared.generated.resources.Res
import lection1.shared.generated.resources.a4_clear
import lection1.shared.generated.resources.a4_explanation
import lection1.shared.generated.resources.a4_field_header
import lection1.shared.generated.resources.a4_field_results
import lection1.shared.generated.resources.a4_hint_hoisted
import lection1.shared.generated.resources.a4_hint_stateful
import lection1.shared.generated.resources.a4_hoisted
import lection1.shared.generated.resources.a4_stateful
import lection1.shared.generated.resources.a4_vm_value
import org.jetbrains.compose.resources.stringResource

/**
 * A4 — подъём состояния.
 *
 * Правило: состояние вниз, события вверх. Код повторяет слайды D1 «Подъём состояния»
 * и «Зачем поднимать»: SearchScreen — единственная, кто знает про ViewModel;
 * SearchField получает значение параметром и отдаёт событие колбэком.
 *
 * Тумблер возвращает SearchField его собственный remember. Поле по-прежнему печатает,
 * но ViewModel о нём не знает: сброс из ViewModel ничего не сбрасывает, а два места
 * вызова разъезжаются — у каждого своя память.
 */
@Composable
fun StateHoistingDemo() {
    var hoisted by remember { mutableStateOf(true) }

    DemoScaffold(
        explanation = stringResource(Res.string.a4_explanation),
        toggleLabel = stringResource(if (hoisted) Res.string.a4_hoisted else Res.string.a4_stateful),
        toggleChecked = hoisted,
        onToggle = { hoisted = it },
        toggleHint = stringResource(if (hoisted) Res.string.a4_hint_hoisted else Res.string.a4_hint_stateful),
    ) {
        SearchScreen(vm = viewModel { SearchViewModel() }, hoisted = hoisted)
    }
}

data class SearchState(val query: String = "")

class SearchViewModel : ViewModel() {
    private val _state = MutableStateFlow(SearchState())
    val state: StateFlow<SearchState> = _state.asStateFlow()

    fun onQueryChange(query: String) = _state.update { it.copy(query = query) }

    fun onClear() = _state.update { it.copy(query = "") }
}

@Composable
private fun SearchScreen(vm: SearchViewModel, hoisted: Boolean) {
    val state by vm.state.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        // Одно и то же поле в двух местах экрана.
        if (hoisted) {
            SearchField(
                label = stringResource(Res.string.a4_field_header),
                query = state.query,                      // ↓ состояние
                onQueryChange = { vm.onQueryChange(it) }, // ↑ событие
            )
            SearchField(
                label = stringResource(Res.string.a4_field_results),
                query = state.query,
                onQueryChange = { vm.onQueryChange(it) },
            )
        } else {
            StatefulSearchField(label = stringResource(Res.string.a4_field_header))
            StatefulSearchField(label = stringResource(Res.string.a4_field_results))
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = stringResource(Res.string.a4_vm_value, state.query),
                    style = MaterialTheme.typography.titleMedium,
                )
                Button(onClick = { vm.onClear() }) {
                    Text(stringResource(Res.string.a4_clear))
                }
            }
        }
    }
}

/** Без состояния: рисует ровно query — и ничего больше. */
@Composable
private fun SearchField(
    label: String,
    query: String,
    onQueryChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
    )
}

/** Со своим состоянием: вызывается без значения, а рисует то пустую строку, то «saur». */
@Composable
private fun StatefulSearchField(label: String) {
    var query by remember { mutableStateOf("") }
    OutlinedTextField(
        value = query,
        onValueChange = { query = it },
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
    )
}
