package hearc.dev_mobile.fishing_clicker

import org.junit.Test

import org.junit.Assert.*
import kotlin.math.pow

/**
 *
 */
class MoneyTEst {
    private var thousands : Int = 60
    private val thousandsList : MutableList<String> = mutableListOf("","k","M","B")
    private val alphabet : String = "abcdefghijklmnopqrstuvwxyz"

    @Test
    fun generateList() {
        var i = 1
        for(x in thousandsList.size..thousands) {
            if (i > alphabet.length) {
                var j  = i++
                var power = 1
                while(j-alphabet.length*power>alphabet.length)power++
                j -= alphabet.length*power
                thousandsList.add(alphabet[--power].toString()+alphabet[j-1].toString())
            } else {
                thousandsList.add(alphabet[i++-1].toString())
            }
        }
        assertEquals(thousandsList[0],"")
        assertEquals(thousandsList[2],"M")
        assertEquals(thousandsList[5],"b")
        assertEquals(thousandsList[27+3], "aa")
        assertEquals(thousandsList[54+3], "bb")
    }
}