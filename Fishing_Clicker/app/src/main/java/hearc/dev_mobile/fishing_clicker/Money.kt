package hearc.dev_mobile.fishing_clicker

import java.math.BigInteger

class Money (private val digitDisplayed:Int){
       //power of ten
    var value : BigInteger = BigInteger.valueOf(0)
    private val mthousandsList : MutableList<String> = generateThousandsList()

    override fun toString(): String{
        val stringValue = value.toString()
        if(value < BigInteger.valueOf(999999)) return stringValue
        var power = 1
        while((stringValue.length-digitDisplayed)-3.0*power>0)power++
        if((stringValue.length-digitDisplayed)%3==0)
            return stringValue.substring(0,digitDisplayed) + mthousandsList[power]
        return stringValue.substring(0,stringValue.length-3*power) + mthousandsList[power]
    }

    private fun generateThousandsList() : MutableList<String>{
        val thousandsList : MutableList<String> = mutableListOf("","k","M","B")
        //a to z
        for(element in CharRange('a','z'))
            thousandsList.add(element.toString())
        //aa to zz
        for(firstChar in CharRange('a','z'))
            for(SecondChar in CharRange('a','z'))
                thousandsList.add(firstChar.toString() + SecondChar.toString())
        return thousandsList
    }


}