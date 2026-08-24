package band.effective.education.crossplatform.data.pokeapi

import kotlinx.serialization.Serializable

@Serializable
data class PokeListResponseDto(
    val count: Int,
    val next: String?,
    val results: List<PokeListItemDto>,
)

@Serializable
data class PokeListItemDto(
    val name: String,
    val url: String,
)

/** Схема ответа сервера «до миграции»: картинка приходит полем image_url. */
@Serializable
data class PokemonCardDtoV1(
    val id: Int,
    val name: String,
    val image_url: String,
)

/** Та же сущность «после миграции» сервера: поле переименовано в sprite. */
@Serializable
data class PokemonCardDtoV2(
    val id: Int,
    val name: String,
    val sprite: String,
)
