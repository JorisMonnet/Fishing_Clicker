package hearc.dev_mobile.fishing_clicker

import android.content.SharedPreferences
import java.math.BigInteger
import java.util.*
import kotlin.math.pow

class User() {
    var level: Int = 0
    private var click = BigInteger.ONE
    var money: Money = Money()

    fun getClickValue(): BigInteger {
        return click.multiply(BigInteger.valueOf(10.0.pow(level).toLong()))
    }

    fun createValuesFromPref(sharedPrefUser : SharedPreferences){
        val moneyPref = sharedPrefUser.getString("GeneralMoney", "0")
        money.value = if(moneyPref!=null) BigInteger(moneyPref) else BigInteger.ZERO
        val clickPref = sharedPrefUser.getString("Click", "1")
        click = if(clickPref!=null) BigInteger(clickPref) else BigInteger.ONE
        level = sharedPrefUser.getInt("Level", 0)
        money.value=money.value.add(BigInteger.valueOf(50000000000000000))//TODO TOREMOVE
    }

    fun saveData(sharedPrefUser : SharedPreferences){
        with(sharedPrefUser.edit()){
            putInt("Level",level)
            putString("Click",click.toString())
            putString("GeneralMoney",money.value.toString())
            apply()
        }
    }
}