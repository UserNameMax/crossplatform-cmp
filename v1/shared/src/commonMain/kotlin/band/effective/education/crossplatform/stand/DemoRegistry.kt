package band.effective.education.crossplatform.stand

import androidx.compose.runtime.Composable
import band.effective.education.crossplatform.demo.d1.PositionalMemoryDemo
import band.effective.education.crossplatform.demo.d1.RememberInputDemo
import band.effective.education.crossplatform.demo.d1.RouteArgumentDemo
import band.effective.education.crossplatform.demo.d1.StateHoistingDemo
import band.effective.education.crossplatform.demo.d1.ThemeTokensDemo
import band.effective.education.crossplatform.demo.d2.InfiniteHeightDemo
import band.effective.education.crossplatform.demo.d2.ModifierOrderDemo
import band.effective.education.crossplatform.demo.d2.OffsetPhaseDemo
import band.effective.education.crossplatform.demo.d2.PhasesDemo
import band.effective.education.crossplatform.demo.d2.SinglePassDemo
import band.effective.education.crossplatform.demo.d2.TeachingColumnDemo
import band.effective.education.crossplatform.demo.d3.ListKeysDemo
import band.effective.education.crossplatform.demo.d3.SearchRecompositionDemo
import band.effective.education.crossplatform.demo.d3.SkipFixesDemo
import band.effective.education.crossplatform.demo.d3.StateReadDemo
import lection1.shared.generated.resources.Res
import lection1.shared.generated.resources.demo_infinite_height
import lection1.shared.generated.resources.demo_list_keys
import lection1.shared.generated.resources.demo_teaching_column
import lection1.shared.generated.resources.demo_state_read
import lection1.shared.generated.resources.demo_skip_fixes
import lection1.shared.generated.resources.demo_route_argument
import lection1.shared.generated.resources.demo_phases
import lection1.shared.generated.resources.demo_offset_phase
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
 * Реестр демо. Спроектирован расширяемым — новые демо этой же вехи (В1) добавляются
 * сюда же. Решено 24.08.2026: Л4 (В2) и Л5 (В3) получают свои стенды — `artifacts/v2`,
 * `artifacts/v3` — а не дописываются в этот проект.
 *
 * Порядок в списке = порядок показа на паре. Внутри каждого уровня демо идут так же,
 * как соответствующие им куски конспекта.
 *
 * Соответствие кодам из SPEC.md, для сверки при правках спецификации:
 * A2 positional-memory · A3 remember-input · A4 state-hoisting · A5 theme-tokens
 * A6 single-pass · A7 modifier-order · A8 infinite-height · A9 search-recomposition
 * A10 list-keys · A11 route-argument · A12 phases · A13 offset-phase · A14 teaching-column
 * A15 state-read · A16 skip-fixes. A11–A16 добавлены 14.09.2026 по сверке с колодами Л1:
 * у слайдов были утверждения, которые на стенде нечем было показать. На экране коды не показываются — зрителю они ничего не говорят.
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
    // Как пользоваться — порядок колоды D1: состояние, тема, навигация
    Demo("remember-input", Res.string.demo_remember_input, Depth.Usage) { RememberInputDemo() },
    Demo("state-hoisting", Res.string.demo_state_hoisting, Depth.Usage) { StateHoistingDemo() },
    Demo("theme-tokens", Res.string.demo_theme_tokens, Depth.Usage) { ThemeTokensDemo() },
    Demo("route-argument", Res.string.demo_route_argument, Depth.Usage) { RouteArgumentDemo() },

    // Как устроено — порядок колоды D2: фазы, правило в коде, модификаторы, один проход, что ломается
    Demo("phases", Res.string.demo_phases, Depth.Mechanism) { PhasesDemo() },
    Demo("teaching-column", Res.string.demo_teaching_column, Depth.Mechanism) { TeachingColumnDemo() },
    Demo("offset-phase", Res.string.demo_offset_phase, Depth.Mechanism) { OffsetPhaseDemo() },
    Demo("modifier-order", Res.string.demo_modifier_order, Depth.Mechanism) { ModifierOrderDemo() },
    Demo("single-pass", Res.string.demo_single_pass, Depth.Mechanism) { SinglePassDemo() },
    Demo("infinite-height", Res.string.demo_infinite_height, Depth.Mechanism) { InfiniteHeightDemo() },

    // Под капот — порядок колоды D3: память, State, ключи, пропуск, починка
    Demo("positional-memory", Res.string.demo_positional_memory, Depth.Internals) { PositionalMemoryDemo() },
    Demo("state-read", Res.string.demo_state_read, Depth.Internals) { StateReadDemo() },
    Demo("list-keys", Res.string.demo_list_keys, Depth.Internals) { ListKeysDemo() },
    Demo("search-recomposition", Res.string.demo_search, Depth.Internals) { SearchRecompositionDemo() },
    Demo("skip-fixes", Res.string.demo_skip_fixes, Depth.Internals) { SkipFixesDemo() },
)

fun demoById(id: String?): Demo? = demos.firstOrNull { it.id == id }
