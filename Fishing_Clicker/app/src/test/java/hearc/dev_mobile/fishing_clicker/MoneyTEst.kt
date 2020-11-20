package hearc.dev_mobile.fishing_clicker

import org.junit.Test

import org.junit.Assert.*
import java.math.BigInteger
import kotlin.math.pow

/**
 *
 */
class MoneyTEst {
    @Test
    fun generateThousandsList(){
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
    @Test
    fun testVtStr(){
        assertEquals(valueToString(BigInteger.valueOf(123456)),"123456")
        assertEquals(valueToString(BigInteger.valueOf(1234567)),"1234k")
        assertEquals(valueToString(BigInteger.valueOf(12345678)),"12345k")
        assertEquals(valueToString(BigInteger.valueOf(123456789)),"123456k")
        assertEquals(valueToString(BigInteger.valueOf(1234567891)),"1234M")
        assertEquals(valueToString(BigInteger("100000000000000000000000000000000000000000000000000")),"100000l")
    }

    private fun valueToString(value : BigInteger): String{
        val thousandsList : MutableList<String> = mutableListOf("","k","M","B")
        //a to z
        for(element in CharRange('a','z'))
            thousandsList.add(element.toString())
        //aa to zz
        for(firstChar in CharRange('a','z'))
            for(SecondChar in CharRange('a','z'))
                thousandsList.add(firstChar.toString() + SecondChar.toString())
        val digitDisplayed =6
        val stringValue = value.toString()
        if(value < BigInteger.valueOf(999999)) return stringValue
        var power = 1
        while((stringValue.length-digitDisplayed)-3.0*power>0)power++
        if((stringValue.length-digitDisplayed)%3==0)
            return stringValue.substring(0,digitDisplayed) + thousandsList[power]
        return stringValue.substring(0,stringValue.length-3*power) + thousandsList[power]
    }
}