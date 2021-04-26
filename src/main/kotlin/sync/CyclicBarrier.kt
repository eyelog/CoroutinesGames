package sync

import java.util.*
import java.util.concurrent.CyclicBarrier

fun main() {
    val createdValues = mutableListOf<Int>()
    /**
     *  create a cyclic barrier that waits for 5 threads to finish their jobs, and after that,
     *  prints the sum of all the values.
     */
    val cyclicBarrier = CyclicBarrier(3) {
        println("Sum of all values is ${createdValues.sum()}")
    }

    val threads = 1.rangeTo(3).map { number ->
        Thread {
            Thread.sleep(Random().nextInt(500).toLong())
            createdValues.add(number)
            cyclicBarrier.await()
            println("I am thread ${Thread.currentThread().name} and I finished my Job!")
        }.apply {
            start()
        }
    }
    threads.forEach { thread ->
        thread.join()
    }
}