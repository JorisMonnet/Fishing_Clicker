package hearc.dev_mobile.fishing_clicker

import org.junit.Test

import org.junit.Assert.*
import kotlin.math.pow

/**
 *
 */
class MoneyTEst {
    @Test
    fun generateList() {
        val thousandsList : MutableList<String> = mutableListOf("","k","M","B")
        //a to z
        for(element in CharRange('a','z'))
            thousandsList.add(element.toString())
        //aa to zz
        for(firstChar in CharRange('a','z'))
            for(SecondChar in CharRange('a','z'))
                thousandsList.add(firstChar.toString() + SecondChar.toString())
        assertEquals(thousandsList[0],"")
        assertEquals(thousandsList[2],"M")
        assertEquals(thousandsList[5],"b")
        assertEquals(thousandsList[26+4], "aa") //value after one time alphabet with only one char and the 4 first values
        assertEquals(thousandsList[52+5], "bb") //value after one time alphabet with 'a' as first character and the 30 before
        assertEquals(thousandsList[thousandsList.size-1],"zz")
    }
}