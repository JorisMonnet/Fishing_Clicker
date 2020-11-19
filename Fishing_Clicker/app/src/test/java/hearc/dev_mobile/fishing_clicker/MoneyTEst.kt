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
        for(i in thousandsList.size+1..thousands) {
            if (i - 4 > alphabet.length) {
                var power = 1
                while(i-4-alphabet.length*power>alphabet.length)power++
                thousandsList.add(alphabet[power-1].toString()+alphabet[i - 5 - alphabet.length*power].toString())
            } else {
                thousandsList.add(alphabet[i-5].toString())
            }
        }
        assertEquals(thousandsList[0],"")
        assertEquals(thousandsList[2],"M")
        assertEquals(thousandsList[5],"b")
        assertEquals(thousandsList[27+3], "aa")
        assertEquals(thousandsList[54+3], "bb")
    }
}