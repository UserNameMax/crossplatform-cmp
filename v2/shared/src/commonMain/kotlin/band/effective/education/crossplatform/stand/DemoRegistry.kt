package band.effective.education.crossplatform.stand

import androidx.compose.runtime.Composable
import band.effective.education.crossplatform.demo.d1.DebounceDemo
import band.effective.education.crossplatform.demo.d1.MapperDemo
import band.effective.education.crossplatform.demo.d1.PaginationDemo
import band.effective.education.crossplatform.demo.d1.ScreenStateDemo
import band.effective.education.crossplatform.demo.d2.FlowColdHotDemo
import band.effective.education.crossplatform.demo.d2.SerializationDemo
import band.effective.education.crossplatform.demo.d3.StructuredConcurrencyDemo
import lection4.shared.generated.resources.Res
import lection4.shared.generated.resources.demo_debounce
import lection4.shared.generated.resources.demo_flow_cold_hot
import lection4.shared.generated.resources.demo_mapper
import lection4.shared.generated.resources.demo_pagination
import lection4.shared.generated.resources.demo_screen_state
import lection4.shared.generated.resources.demo_serialization
import lection4.shared.generated.resources.demo_structured_concurrency
import lection4.shared.generated.resources.depth_internals
import lection4.shared.generated.resources.depth_mechanism
import lection4.shared.generated.resources.depth_usage
import org.jetbrains.compose.resources.StringResource

/**
 * Реестр демо стенда Л4 (В2, CMP). Движок скопирован из `artifacts/v1` — стенд заводится
 * один на веху, а не на весь курс (решено 24.08.2026).
 *
 * Порядок в списке = порядок показа на паре. Соответствие кодам из SPEC.md:
 * A2 mapper · A3 screen-state · A4 pagination · A5 debounce ·
 * B1 flow-cold-hot · B2 serialization · C1 structured-concurrency (гвоздь).
 */
enum class Depth(val title: StringResource) {
    Usage(Res.string.depth_usage),
    Mechanism(Res.string.depth_mechanism),
    Internals(Res.string.depth_internals),
}

class Demo(
    val id: String,
    val title: StringResource,
    val depth: Depth,
    val content: @Composable () -> Unit,
)

val demos: List<Demo> = listOf(
    // Как пользоваться
    Demo("mapper", Res.string.demo_mapper, Depth.Usage) { MapperDemo() },
    Demo("screen-state", Res.string.demo_screen_state, Depth.Usage) { ScreenStateDemo() },
    Demo("pagination", Res.string.demo_pagination, Depth.Usage) { PaginationDemo() },
    Demo("debounce", Res.string.demo_debounce, Depth.Usage) { DebounceDemo() },

    // Как устроено
    Demo("flow-cold-hot", Res.string.demo_flow_cold_hot, Depth.Mechanism) { FlowColdHotDemo() },
    Demo("serialization", Res.string.demo_serialization, Depth.Mechanism) { SerializationDemo() },

    // Под капот — гвоздь пары
    Demo("structured-concurrency", Res.string.demo_structured_concurrency, Depth.Internals) { StructuredConcurrencyDemo() },
)

fun demoById(id: String?): Demo? = demos.firstOrNull { it.id == id }
