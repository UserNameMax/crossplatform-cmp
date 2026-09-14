package band.effective.education.crossplatform.stand

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFFB3261E),
    surface = Color(0xFFFFFBFE),
    background = Color(0xFFFFFBFE),
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFF2B8B5),
    surface = Color(0xFF1C1B1F),
    background = Color(0xFF141218),
)

/**
 * Тема стенда. Токены кладутся в неявный контекст композиции на корне —
 * ровно тот механизм, который разбирается в блоке D1.
 *
 * Схемы те же, что в ui/AppTheme.kt шаблона ПЗ1 и на слайде «Тема в коде»: цвета
 * объявлены здесь и только здесь.
 */
@Composable
fun StandTheme(
    dark: Boolean,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (dark) DarkColors else LightColors,
        content = content,
    )
}
