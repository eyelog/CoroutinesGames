package sync

import kotlinx.coroutines.*
import java.util.concurrent.atomic.AtomicInteger

class IncrementorAtomic() {
    private val sharedCounter: AtomicInteger = AtomicInteger(0)

    fun updateCounterIfNecessary(shouldIActuallyIncrement: Boolean) {
        if (shouldIActuallyIncrement) {
            sharedCounter.incrementAndGet()
        }
    }

    fun getSharedCounter():Int {
        return sharedCounter.get()
    }
}

fun main() = runBlocking {
    val incrementor = IncrementorAtomic()
    val scope = CoroutineScope(newFixedThreadPoolContext(4, "synchronizationPool"))
    scope.launch {
        val coroutines = 1.rangeTo(1000).map {
            launch {
                for (i in 1..1000) {
                    incrementor.updateCounterIfNecessary(it % 2 == 0)
                }
            }
        }

        coroutines.forEach { coroutine ->
            coroutine.join()
        }
    }.join()

    println("The number of shared counter is ${incrementor.getSharedCounter()}")
}