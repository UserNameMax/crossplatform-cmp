package band.effective.education.crossplatform.stand

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue

/**
 * Служебный инструмент стенда для демо «Один проход измерения».
 *
 * Числа публикуются одной записью в конце прохода измерения. Одна запись, а не
 * инкременты по ходу: множественные записи в snapshot-state во время layout могли бы
 * гонять лишние проходы. После первого прохода значение перестаёт меняться, запись
 * тем же значением инвалидацию не вызывает — процесс сходится.
 */
class MeasureLog {
    var measureCalls: Int by mutableIntStateOf(0)
        private set

    var intrinsicQueries: Int by mutableIntStateOf(0)
        private set

    var childCount: Int by mutableIntStateOf(0)
        private set

    fun publish(measures: Int, intrinsics: Int, children: Int) {
        measureCalls = measures
        intrinsicQueries = intrinsics
        childCount = children
    }
}
