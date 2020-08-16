package karate

import com.intuit.karate.junit5.Karate

class KarateTests {
    @Karate.Test
    fun runAllTests() = Karate.run().relativeTo(javaClass)

}