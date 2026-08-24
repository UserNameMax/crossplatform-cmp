package band.effective.education.crossplatform

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        // заголовок окна - тоже подпись интерфейса, значит живёт в ресурсах
        title = windowTitle(),
    ) {
        App()
    }
}
