package band.effective.education.crossplatform

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.ImageComposeScene
import androidx.compose.ui.unit.Density
import org.jetbrains.skia.EncodedImageFormat
import java.io.File
import javax.swing.SwingUtilities

/**
 * Снимок стенда без окна: страховка к паре (SPEC §2, «скриншоты до/после»)
 * и способ прогнать каждое демо руками, не открывая окно.
 *
 * ./gradlew :desktopApp:renderCheck --args="1200"
 */
@OptIn(ExperimentalComposeUiApi::class)
fun main(args: Array<String>) {
    val width = args.firstOrNull()?.toIntOrNull() ?: 1200
    val outDir = File("build/render").apply { mkdirs() }
    // Пустой список демо = снимок оболочки; иначе каждое демо отдельным файлом.
    val targets: List<String?> = if (args.size > 1) args.drop(1) else listOf(null)
    for (demoId in targets) {
        val demo = demoId?.let { band.effective.education.crossplatform.stand.demoById(it) }
        lateinit var scene: ImageComposeScene
        SwingUtilities.invokeAndWait {
            scene = ImageComposeScene(width = width, height = 900, density = Density(1f)) {
                if (demo == null) {
                    App()
                } else {
                    band.effective.education.crossplatform.stand.StandTheme(dark = false) {
                        demo.content()
                    }
                }
            }
        }
        var bytes: ByteArray? = null
        repeat(40) { i ->
            Thread.sleep(200)
            SwingUtilities.invokeAndWait {
                bytes = scene.render(i * 200_000_000L).encodeToData(EncodedImageFormat.PNG)!!.bytes
            }
        }
        val out = File(outDir, "stand-" + (demoId ?: "list") + ".png")
        out.writeBytes(bytes!!)
        println("-> " + out.path)
        SwingUtilities.invokeAndWait { scene.close() }
    }
}
