package band.effective.education.crossplatform

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import band.effective.education.crossplatform.stand.Stand
import lection5.shared.generated.resources.Res
import lection5.shared.generated.resources.window_title
import org.jetbrains.compose.resources.stringResource

@Composable
@Preview
fun App() {
    Stand()
}

/**
 * Заголовок окна - тоже подпись интерфейса, значит живёт в ресурсах. Отдаётся
 * отсюда, а не читается точкой входа: сгенерированный Res внутренний для
 * модуля, и открывать его наружу ради одной строки незачем.
 */
@Composable
fun windowTitle(): String = stringResource(Res.string.window_title)
