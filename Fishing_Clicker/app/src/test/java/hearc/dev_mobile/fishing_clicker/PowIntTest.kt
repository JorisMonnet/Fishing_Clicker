package hearc.dev_mobile.fishing_clicker


import org.junit.Test

import org.junit.Assert.*
import java.math.BigInteger
import kotlin.math.pow

/**
 *
 */
class PowIntTest {
    @Test
    fun powIntTest() {
        val startTime =System.currentTimeMillis()
        powInt(1L, (10.0.pow(876 * 3)).toLong())
        val elapsedTime = System.currentTimeMillis()-startTime
    }

    private fun powInt(n: Long, exp: Long): Long {
        var temp = 1L
        for (i in 1..exp) {
            temp *= n
        }
        return temp
    }
}