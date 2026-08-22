package band.effective.education.crossplatform.demo.d1

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import band.effective.education.crossplatform.stand.DemoScaffold
import lection1.shared.generated.resources.Res
import lection1.shared.generated.resources.a5_explanation
import lection1.shared.generated.resources.a5_from_tokens
import lection1.shared.generated.resources.a5_hardcoded
import org.jetbrains.compose.resources.stringResource

/**
 * A5 — тема и захардкоженный цвет.
 *
 * Показывает: цвет, взятый мимо контекста темы, в переключении не участвует —
 * его просто никто не читает из контекста.
 *
 * Переключатель темы живёт в шапке стенда, отдельного тумблера тут не нужно.
 */
@Composable
fun ThemeTokensDemo() {
    DemoScaffold(
        explanation = stringResource(Res.string.a5_explanation),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(16.dp),
            ) {
                Text(
                    text = stringResource(Res.string.a5_from_tokens),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
                Text(
                    text = "MaterialTheme.colorScheme.primaryContainer",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFB3E5FC))
                    .padding(16.dp),
            ) {
                Text(
                    text = stringResource(Res.string.a5_hardcoded),
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF01579B),
                )
                Text(
                    text = "Color(0xFFB3E5FC)",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF01579B),
                )
            }
        }
    }
}
