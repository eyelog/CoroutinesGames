package sync

import kotlinx.coroutines.*
import java.util.concurrent.Semaphore

class IncrementorSemaphore {
    private val sharedCounterLock = Semaphore(1)
    var sharedCounter: Int = 0
        private set

    fun updateCounterIfNecessary(shouldIActuallyIncrement: Boolean) {
        if (shouldIActuallyIncrement) {
            try {
                sharedCounterLock.acquire()
                sharedCounter++
            } finally {
                sharedCounterLock.release()
            }
        }
    }
}

fun main() = runBlocking {
    val incrementor = IncrementorSemaphore()
    val scope = CoroutineScope(newFixedThreadPoolContext(4, "synchronizationPool"))
    scope.launch {
        val coroutines = 1.rangeTo(1000).map {
            launch {
                for (i in 1..1000) {
                    incrementor.updateCounterIfNecessary(it % 2 == 0)
                }
            }
        }

        coroutines.forEach { corotuine ->
            corotuine.join()
        }
    }.join()

    println("The number of shared counter is ${incrementor.sharedCounter}")
}