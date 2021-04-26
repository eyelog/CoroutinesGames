package sync

import kotlinx.coroutines.*

@Volatile private var sharedCounter: Int = 0
fun main() = runBlocking {

    val scope = CoroutineScope(newFixedThreadPoolContext(4, "synchronizationPool"))
    scope.launch {
        val coroutines = 1.rangeTo(1000).map {
            launch {
                for (i in 1..1000) {
                    sharedCounter++
                }
            }
        }

        coroutines.forEach { coroutine ->
            coroutine.join()
        }
    }.join()

    println("The number of shared counter is $sharedCounter")
}