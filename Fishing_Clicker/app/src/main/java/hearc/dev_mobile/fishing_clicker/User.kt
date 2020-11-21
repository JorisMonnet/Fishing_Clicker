package hearc.dev_mobile.fishing_clicker

import android.content.SharedPreferences
import java.math.BigInteger
import java.util.*
import kotlin.math.pow

class User(sharedPref: SharedPreferences) {
    var level: Int = 0
    private var click = BigInteger.ONE
    var money: Money = Money()

    init {
        val moneyPref = sharedPref.getString("GeneralMoney", "0")
        money.value = if(moneyPref!=null) BigInteger(moneyPref) else BigInteger.ZERO
        val clickPref = sharedPref.getString("Click", "1")
        money.value = if(clickPref!=null) BigInteger(clickPref) else BigInteger.ONE
        level = sharedPref.getInt("Level", 0)
    }


    fun getClickValue(): BigInteger {
        return click.multiply(BigInteger.valueOf(10.0.pow(level).toLong()))
    }

    fun saveData(){
        
    }
}