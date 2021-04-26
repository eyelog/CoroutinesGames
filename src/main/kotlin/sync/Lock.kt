package sync

import kotlinx.coroutines.*
import java.util.concurrent.locks.ReentrantLock

class IncrementorLock {
    private val sharedCounterLock = ReentrantLock()
    var sharedCounter: Int = 0
        private set

    fun updateCounterIfNecessary(shouldIActuallyIncrement: Boolean) {
        if (shouldIActuallyIncrement) {
            try {
                sharedCounterLock.lock()
                sharedCounter++
            } finally {
                sharedCounterLock.unlock()
            }
        }
    }
}

fun main() = runBlocking {
    val incrementor = IncrementorLock()
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