package band.effective.education.crossplatform.data

/**
 * Единственное место стенда, где живут предметные данные.
 *
 * С 14.09.2026 — покемоны, как в шаблоне ПЗ1 и в примерах колод Л1 («saur» в поиске,
 * bulbasaur → venusaur): студент видит на лекции те же данные, что через четыре дня
 * соберёт руками. Имена и номера — данные, а не подписи интерфейса, в ресурсы не идут.
 */
data class DemoItem(
    val id: Int,
    val title: String,
    val subtitle: String,
    val visible: Boolean = true,
)

private val pokemons = listOf(
    1 to "Bulbasaur", 2 to "Ivysaur", 3 to "Venusaur", 4 to "Charmander",
    5 to "Charmeleon", 6 to "Charizard", 7 to "Squirtle", 8 to "Wartortle",
    9 to "Blastoise", 25 to "Pikachu", 26 to "Raichu", 35 to "Clefairy",
    39 to "Jigglypuff", 52 to "Meowth", 54 to "Psyduck", 63 to "Abra",
    74 to "Geodude", 92 to "Gastly", 94 to "Gengar", 129 to "Magikarp",
    130 to "Gyarados", 133 to "Eevee", 143 to "Snorlax", 150 to "Mewtwo",
)

val demoItems: List<DemoItem> = pokemons.mapIndexed { index, (number, name) ->
    DemoItem(
        id = number,
        title = name,
        subtitle = "#" + number.toString().padStart(3, '0'),
        visible = index % 7 != 6,
    )
}
