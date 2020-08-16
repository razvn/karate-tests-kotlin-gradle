package karate.user

import com.intuit.karate.junit5.Karate

class UserRunner {
    @Karate.Test
    fun runUserTests() = Karate.run("classpath:karate/user/user.feature")

    // @Karate.Test
    // fun runUserTests() = Karate.run().relativeTo(javaClass)

}