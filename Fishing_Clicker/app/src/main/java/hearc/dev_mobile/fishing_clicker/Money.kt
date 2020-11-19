package hearc.dev_mobile.fishing_clicker

import kotlin.math.pow

class Money {
    private val NUMBER_OF_DIGIT_DISPLAYED=6
    private var value : Long = 0
    private var thousands : Int = 0
    private val mthousandsList : MutableList<String> = generateThousandsList()

    fun verifyAndShowValue(): String{
        if(value < 0) throw Exception("Error on money value")
        val testValue = value/(10.0.pow(thousands))
        return (if(testValue>10.0.pow(NUMBER_OF_DIGIT_DISPLAYED)) testValue.toString() else value.toString())+mthousandsList[++thousands]
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

    fun payUpgrade(value : Int ){

    }
}