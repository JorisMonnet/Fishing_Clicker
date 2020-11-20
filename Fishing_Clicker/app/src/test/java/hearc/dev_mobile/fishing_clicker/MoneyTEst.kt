package hearc.dev_mobile.fishing_clicker

import org.junit.Test

import org.junit.Assert.*

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
        assertEquals(valueToString(123456),"123456")
        assertEquals(valueToString(1234567),"1234k")
        assertEquals(valueToString(12345678),"12345k")
        assertEquals(valueToString(123456789),"123456k")
        assertEquals(valueToString(1234567891),"1234M")

    }

    private fun valueToString(value : Long): String{
        val thousandsList : MutableList<String> = mutableListOf("","k","M","B")
        //a to z
        for(element in CharRange('a','z'))
            thousandsList.add(element.toString())
        //aa to zz
        for(firstChar in CharRange('a','z'))
            for(SecondChar in CharRange('a','z'))
                thousandsList.add(firstChar.toString() + SecondChar.toString())
        val stringValue = value.toString()
        if(value < 999999) return stringValue
        var power = 1
        while((stringValue.length-6)-3.0*power>0)power++
        if((stringValue.length-6)%3==0)
            return stringValue.substring(0,6) + thousandsList[power]
        return stringValue.substring(0,stringValue.length-3*power) + thousandsList[power]
    }
}