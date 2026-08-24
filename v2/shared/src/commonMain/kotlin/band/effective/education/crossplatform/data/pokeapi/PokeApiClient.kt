package band.effective.education.crossplatform.data.pokeapi

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

private const val BASE_URL = "https://pokeapi.co/api/v2"

fun pokeApiHttpClient(): HttpClient = HttpClient {
    install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true })
    }
}

class PokeApiClient(private val http: HttpClient = pokeApiHttpClient()) {

    suspend fun list(limit: Int, offset: Int): PokeListResponseDto =
        http.get("$BASE_URL/pokemon") {
            parameter("limit", limit)
            parameter("offset", offset)
        }.body()
}
