package band.effective.education.crossplatform.stand

import androidx.compose.runtime.Composable
import band.effective.education.crossplatform.demo.d1.PositionalMemoryDemo
import band.effective.education.crossplatform.demo.d1.RememberInputDemo
import band.effective.education.crossplatform.demo.d1.StateHoistingDemo
import band.effective.education.crossplatform.demo.d1.ThemeTokensDemo
import band.effective.education.crossplatform.demo.d2.InfiniteHeightDemo
import band.effective.education.crossplatform.demo.d2.ModifierOrderDemo
import band.effective.education.crossplatform.demo.d2.SinglePassDemo
import band.effective.education.crossplatform.demo.d3.ListKeysDemo
import band.effective.education.crossplatform.demo.d3.SearchRecompositionDemo
import lection1.shared.generated.resources.Res
import lection1.shared.generated.resources.demo_infinite_height
import lection1.shared.generated.resources.demo_list_keys
import lection1.shared.generated.resources.demo_modifier_order
import lection1.shared.generated.resources.demo_positional_memory
import lection1.shared.generated.resources.demo_remember_input
import lection1.shared.generated.resources.demo_search
import lection1.shared.generated.resources.demo_single_pass
import lection1.shared.generated.resources.demo_state_hoisting
import lection1.shared.generated.resources.demo_theme_tokens
import lection1.shared.generated.resources.depth_internals
import lection1.shared.generated.resources.depth_mechanism
import lection1.shared.generated.resources.depth_usage
import org.jetbrains.compose.resources.StringResource

/**
 * Реестр демо. Спроектирован расширяемым: Л4 и Л5 доложатся сюда же,
 * заводить под них отдельный проект не придётся.
 *
 * Порядок в списке = порядок показа на паре. Внутри каждого уровня демо идут так же,
 * как соответствующие им куски конспекта.
 *
 * Соответствие кодам из SPEC.md, для сверки при правках спецификации:
 * A2 positional-memory · A3 remember-input · A4 state-hoisting · A5 theme-tokens
 * A6 single-pass · A7 modifier-order · A8 infinite-height · A9 search-recomposition
 * A10 list-keys. На экране коды не показываются — зрителю они ничего не говорят.
 */
enum class Depth(val title: StringResource) {
    /** D1 по шкале глубины: студент после этого может написать так же. */
    Usage(Res.string.depth_usage),

    /** D2: студент может объяснить, почему код тормозит. */
    Mechanism(Res.string.depth_mechanism),

    /** D3: студент может осознанно оптимизировать. */
    Internals(Res.string.depth_internals),
}

class Demo(
    val id: String,
    val title: StringResource,
    val depth: Depth,
    val content: @Composable () -> Unit,
)

val demos: List<Demo> = listOf(
    // Как пользоваться — порядок конспекта: сначала состояние, потом тема
    Demo("remember-input", Res.string.demo_remember_input, Depth.Usage) { RememberInputDemo() },
    Demo("state-hoisting", Res.string.demo_state_hoisting, Depth.Usage) { StateHoistingDemo() },
    Demo("theme-tokens", Res.string.demo_theme_tokens, Depth.Usage) { ThemeTokensDemo() },

    // Как устроено — ограничения, один проход, что ломается
    Demo("modifier-order", Res.string.demo_modifier_order, Depth.Mechanism) { ModifierOrderDemo() },
    Demo("single-pass", Res.string.demo_single_pass, Depth.Mechanism) { SinglePassDemo() },
    Demo("infinite-height", Res.string.demo_infinite_height, Depth.Mechanism) { InfiniteHeightDemo() },

    // Под капот — slot table, ключи, пропуск
    Demo("positional-memory", Res.string.demo_positional_memory, Depth.Internals) { PositionalMemoryDemo() },
    Demo("list-keys", Res.string.demo_list_keys, Depth.Internals) { ListKeysDemo() },
    Demo("search-recomposition", Res.string.demo_search, Depth.Internals) { SearchRecompositionDemo() },
)

fun demoById(id: String?): Demo? = demos.firstOrNull { it.id == id }
