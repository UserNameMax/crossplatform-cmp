package band.effective.education.crossplatform.demo.d2

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import lection4.shared.generated.resources.Res
import lection4.shared.generated.resources.ser_descriptor_title
import lection4.shared.generated.resources.ser_explanation
import lection4.shared.generated.resources.ser_field
import lection4.shared.generated.resources.ser_note
import lection4.shared.generated.resources.ser_question_title
import lection4.shared.generated.resources.ser_real_exception
import lection4.shared.generated.resources.ser_serial_name
import lection4.shared.generated.resources.ser_try_button
import lection4.shared.generated.resources.ser_unexpectedly_worked
import org.jetbrains.compose.resources.stringResource

@Serializable
private data class DemoDto(val id: Int, val name: String, val imageUrl: String)

/**
 * Намеренно без @Serializable — постоянный экспонат для кнопки «попробовать» ниже.
 * Не восстанавливать аннотацию: без неё демо и держится.
 */
private data class NotAnnotated(val id: Int, val name: String)

@Composable
fun SerializationDemo() {
    val descriptor = remember { DemoDto.serializer().descriptor }
    var errorText by remember { mutableStateOf<String?>(null) }
    val unexpectedlyWorked = stringResource(Res.string.ser_unexpectedly_worked)

    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(stringResource(Res.string.ser_descriptor_title), style = MaterialTheme.typography.titleMedium)
                Text(stringResource(Res.string.ser_serial_name, descriptor.serialName))
                for (i in 0 until descriptor.elementsCount) {
                    Text(
                        stringResource(
                            Res.string.ser_field,
                            descriptor.getElementName(i),
                            descriptor.getElementDescriptor(i).serialName,
                        ),
                    )
                }
                Text(
                    stringResource(Res.string.ser_note),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(stringResource(Res.string.ser_question_title), style = MaterialTheme.typography.titleMedium)
                Text(
                    stringResource(Res.string.ser_explanation),
                    style = MaterialTheme.typography.bodySmall,
                )
                Button(onClick = {
                    errorText = try {
                        Json.decodeFromString<NotAnnotated>("""{"id":1,"name":"x"}""")
                        unexpectedlyWorked
                    } catch (e: SerializationException) {
                        e.message
                    }
                }) {
                    Text(stringResource(Res.string.ser_try_button))
                }
                errorText?.let {
                    Text(
                        stringResource(Res.string.ser_real_exception, it),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
        }
    }
}
