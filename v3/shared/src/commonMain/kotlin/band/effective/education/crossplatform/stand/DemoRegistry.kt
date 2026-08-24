package band.effective.education.crossplatform.stand

import androidx.compose.runtime.Composable
import band.effective.education.crossplatform.demo.d1.AdaptiveDemo
import band.effective.education.crossplatform.demo.d1.ExpectActualDemo
import band.effective.education.crossplatform.demo.d1.ResourcesDemo
import band.effective.education.crossplatform.demo.d2.SourceOfTruthDemo
import band.effective.education.crossplatform.demo.d2.StackAddressDemo
import band.effective.education.crossplatform.demo.d3.BindingDemo
import lection5.shared.generated.resources.Res
import lection5.shared.generated.resources.demo_adaptive
import lection5.shared.generated.resources.demo_binding
import lection5.shared.generated.resources.demo_expect_actual
import lection5.shared.generated.resources.demo_resources
import lection5.shared.generated.resources.demo_source_of_truth
import lection5.shared.generated.resources.demo_stack_address
import lection5.shared.generated.resources.depth_internals
import lection5.shared.generated.resources.depth_mechanism
import lection5.shared.generated.resources.depth_usage
import org.jetbrains.compose.resources.StringResource

/**
 * Реестр демо стенда Л5 (В3, CMP). Движок унаследован от `artifacts/v1` и `v2` —
 * стенд заводится один на веху, а не на весь курс (решено 24.08.2026).
 *
 * Порядок в списке = порядок показа на паре. Соответствие кодам из SPEC.md:
 * A1 expect-actual · A2 resources · A3 adaptive ·
 * B1 stack-address · B2 source-of-truth · C1 binding (гвоздь).
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
    Demo("expect-actual", Res.string.demo_expect_actual, Depth.Usage) { ExpectActualDemo() },
    Demo("resources", Res.string.demo_resources, Depth.Usage) { ResourcesDemo() },
    Demo("adaptive", Res.string.demo_adaptive, Depth.Usage) { AdaptiveDemo() },

    // Как устроено
    Demo("stack-address", Res.string.demo_stack_address, Depth.Mechanism) { StackAddressDemo() },
    Demo("source-of-truth", Res.string.demo_source_of_truth, Depth.Mechanism) { SourceOfTruthDemo() },

    // Под капотом — гвоздь пары
    Demo("binding", Res.string.demo_binding, Depth.Internals) { BindingDemo() },
)

fun demoById(id: String?): Demo? = demos.firstOrNull { it.id == id }
