package band.effective.education.crossplatform.data

/**
 * Единственное место стенда, где живут предметные данные.
 *
 * Сейчас это нейтральные заглушки: ТЗ курса ещё не выбрано. Когда оно появится,
 * меняется только этот файл — сами демо к предметной области не привязаны.
 */
data class DemoItem(
    val id: Int,
    val title: String,
    val subtitle: String,
    val visible: Boolean = true,
)

private val titles = listOf(
    "Альфа", "Бета", "Гамма", "Дельта", "Эпсилон", "Дзета",
    "Эта", "Тета", "Йота", "Каппа", "Лямбда", "Мю",
    "Ню", "Кси", "Омикрон", "Пи", "Ро", "Сигма",
    "Тау", "Ипсилон", "Фи", "Хи", "Пси", "Омега",
)

val demoItems: List<DemoItem> = titles.mapIndexed { index, title ->
    DemoItem(
        id = index,
        title = title,
        subtitle = "Позиция ${index + 1}",
        visible = index % 7 != 6,
    )
}
