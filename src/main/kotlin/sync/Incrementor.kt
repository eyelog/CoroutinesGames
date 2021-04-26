package sync

import kotlinx.coroutines.*

class Incrementor {
    var sharedCounter: Int = 0
        private set

    fun updateCounterIfNecessary(shouldIActuallyIncrement: Boolean) {
        if (shouldIActuallyIncrement) {
            synchronized(this) {
                sharedCounter++
            }
        }
    }
}

fun main() = runBlocking {
    val incrementor = Incrementor()
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
    println("The number of shared counter is ${incrementor.sharedCounter}")
}