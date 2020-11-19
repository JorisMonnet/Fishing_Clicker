package hearc.dev_mobile.fishing_clicker

import java.lang.StringBuilder
import kotlin.math.pow

class Money {
    private val NUMBER_OF_DIGIT_DISPLAYED=6
    private var value : Int = 0
    private var thousands : Int = 0
    private val thousandsList : MutableList<String> = mutableListOf("","k","M","B")
    private val alphabet : String = "abcdefghijklmnopqrstuvwxyz"

    fun verifyAndShowValue(): String{
        if(value < 0) throw Exception("Error on money value")
        var testValue = value/(10.0.pow(thousands))
        return (if(testValue>10.0.pow(NUMBER_OF_DIGIT_DISPLAYED)) testValue.toString() else value.toString())+thousandsList[getThousands()]
    }
    private fun getThousands() : Int{
        thousands++
        if(thousandsList.size<thousands){
            generateThousandsList()
        }
        return thousands
    }

    private fun generateThousandsList(){
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
    }
}