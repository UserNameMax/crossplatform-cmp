package band.effective.education.crossplatform.stand

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

/**
 * Тема стенда. Токены кладутся в неявный контекст композиции на корне —
 * ровно тот механизм, который разбирается в блоке D1.
 */
@Composable
fun StandTheme(
    dark: Boolean,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (dark) darkColorScheme() else lightColorScheme(),
        content = content,
    )
}
