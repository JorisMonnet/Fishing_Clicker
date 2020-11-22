package hearc.dev_mobile.fishing_clicker

import org.junit.Test
import java.math.BigInteger
import org.junit.Assert.*

/**
 * A test on how works BigInteger with negative numbers
 */
class BigIntegerTest {
    @Test
    fun add(){
        val a = BigInteger("-45")
        val b = BigInteger("50")
        val c = BigInteger("45")
        assertEquals(b.add(a),b.add(c.negate()))
        assertEquals(b.add(a),BigInteger("5")) //check if add work with negative value
    }
}