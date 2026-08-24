package band.effective.education.crossplatform.demo.d1

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import band.effective.education.crossplatform.data.pokeapi.PokemonCardDtoV1
import band.effective.education.crossplatform.data.pokeapi.PokemonCardDtoV2
import kotlinx.serialization.json.Json
import lection4.shared.generated.resources.Res
import lection4.shared.generated.resources.mapper_note
import lection4.shared.generated.resources.mapper_screen_got
import lection4.shared.generated.resources.mapper_toggle_label
import lection4.shared.generated.resources.mapper_using_v1
import lection4.shared.generated.resources.mapper_using_v2
import org.jetbrains.compose.resources.stringResource

data class PokemonCard(val id: Int, val name: String, val imageUrl: String)

fun PokemonCardDtoV1.toDomain() = PokemonCard(id = id, name = name, imageUrl = image_url)
fun PokemonCardDtoV2.toDomain() = PokemonCard(id = id, name = name, imageUrl = sprite)

private val jsonV1 = """{"id":25,"name":"pikachu","image_url":"https://.../pikachu.png"}"""
private val jsonV2 = """{"id":25,"name":"pikachu","sprite":"https://.../pikachu.png"}"""

@Composable
fun MapperDemo() {
    var serverMigrated by remember { mutableStateOf(false) }

    val card = remember(serverMigrated) {
        if (serverMigrated) {
            Json.decodeFromString<PokemonCardDtoV2>(jsonV2).toDomain()
        } else {
            Json.decodeFromString<PokemonCardDtoV1>(jsonV1).toDomain()
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
            Text(stringResource(Res.string.mapper_toggle_label), modifier = Modifier.weight(1f))
            Switch(checked = serverMigrated, onCheckedChange = { serverMigrated = it })
        }

        Text(
            text = stringResource(if (serverMigrated) Res.string.mapper_using_v2 else Res.string.mapper_using_v1),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(stringResource(Res.string.mapper_screen_got))
                Text("#${card.id} ${card.name}", style = MaterialTheme.typography.titleMedium)
                Text(card.imageUrl, style = MaterialTheme.typography.bodySmall)
            }
        }

        Text(
            text = stringResource(Res.string.mapper_note),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
