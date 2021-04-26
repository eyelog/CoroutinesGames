package sync

data class Human(val name:String) {
    @Synchronized fun sayHi(to: Human){
        println("$name saying hi to ${to.name}")
        Thread.sleep(500)
        to.sayHiBack(this)

    }
    @Synchronized fun sayHiBack(to: Human){
        println("$name saying hi back to ${to.name}")
    }

}

fun main() {
    val bob = Human("Bob")
    val jhon = Human("Jhon")
    val bobThread = Thread {
        bob.sayHi(jhon)
    }.apply {
        start()
    }

    val jhonThread = Thread {
        jhon.sayHi(bob)
    }.apply {
        start()
    }
    bobThread.join()
    jhonThread.join()
}