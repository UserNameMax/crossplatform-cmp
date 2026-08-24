package band.effective.education.crossplatform.verify

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Проверка фактуры для гвоздя Л4, до написания конспекта: отмена родительского Job
 * рекурсивно отменяет все дочерние корутины, включая незавершённые «сетевые запросы»,
 * без единой строчки ручной отмены на каждой из них.
 *
 * Проверено на kotlinx.coroutines 1.11.0, Kotlin 2.4.10, 24.08.2026.
 */
class StructuredConcurrencyVerification {

    @Test
    fun `cancelling parent job cancels all in-flight children`() = runTest {
        var started = 0
        var completed = 0
        val parentJob = Job()
        val parentScope = CoroutineScope(coroutineContext + parentJob)

        val children = List(3) {
            parentScope.launch {
                started++
                delay(1000) // имитация сетевого запроса
                completed++ // не должно достигаться ни для одной из трёх
            }
        }

        advanceTimeBy(200) // все три успели стартовать и уйти в delay
        assertEquals(3, started, "все три ребёнка должны были стартовать")
        assertEquals(0, completed, "никто не должен был успеть завершиться за 200мс из 1000мс")

        parentScope.cancel() // отменяем ТОЛЬКО родителя, ни один child.cancel() не вызывается руками

        advanceTimeBy(1000) // даём достаточно виртуального времени, чтобы все успели бы завершиться

        assertEquals(0, completed, "отмена родителя должна была остановить всех детей до завершения")
        assertTrue(children.all { it.isCancelled }, "у каждого ребёнка Job помечен отменённым")
        assertTrue(parentJob.children.toList().isEmpty() || parentJob.isCancelled, "родительский Job тоже отменён")
    }

    /**
     * Контраст — «где ломается». `GlobalScope.launch` не подчинён никакому родителю,
     * поэтому отмена родительской области его не касается: утечка запроса — то, что
     * бывает по умолчанию, если явно не привязаться к жизненному циклу экрана.
     */
    @OptIn(DelicateCoroutinesApi::class)
    @Test
    fun `cancelling parent job does NOT cancel a GlobalScope child`() = runBlocking {
        var globalCompleted = false
        val parentJob = Job()
        val parentScope = CoroutineScope(coroutineContext + parentJob)

        // ребёнок стартован через GlobalScope изнутри блока parentScope — частая ошибка
        parentScope.launch {
            GlobalScope.launch {
                delay(50)
                globalCompleted = true
            }
        }

        delay(10) // даём успеть стартовать
        parentScope.cancel() // родитель отменён

        delay(100) // реальное время: достаточно, чтобы GlobalScope-ребёнок успел бы завершиться

        assertTrue(globalCompleted, "GlobalScope-корутина не подчиняется родителю и завершается несмотря на отмену")
    }
}
