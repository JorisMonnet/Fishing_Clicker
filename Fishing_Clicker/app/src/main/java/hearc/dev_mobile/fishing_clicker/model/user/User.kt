package hearc.dev_mobile.fishing_clicker.model.user

import android.content.SharedPreferences
import hearc.dev_mobile.fishing_clicker.model.money.Money
import java.math.BigInteger

class User {
    var level: Int = 0
    private var click = BigInteger.ONE
    var money: Money = Money(BigInteger.ZERO)

    /**
     * Function to get value of the click with the given user level
     * @return the value of the click
     */
    fun getClickValue(): BigInteger {
        return click.multiply(BigInteger.TEN.pow(level.toInt() * 3))
    }

    /**
     * Get the value of the user from the shared Preferences when game is quit and restart
     * @param sharedPrefUser the SharedPreferences
     */
    fun createValuesFromPref(sharedPrefUser : SharedPreferences){
        val moneyPref = sharedPrefUser.getString("GeneralMoney", "0")
        money.value = if(moneyPref!=null) BigInteger(moneyPref) else BigInteger.ZERO
        val clickPref = sharedPrefUser.getString("Click", "1")
        click = if(clickPref!=null) BigInteger(clickPref) else BigInteger.ONE
        level = sharedPrefUser.getInt("LevelUser", 0)
//        money.value=money.value.add(BigInteger.valueOf(50000000))//TODO TOREMOVE
    }

    /**
     * save the user Data
     * @param sharedPrefUser the sharedPreferences where it's saved
     */
    fun saveData(sharedPrefUser : SharedPreferences){
        with(sharedPrefUser.edit()){
            putInt("LevelUser",level)
            putString("Click",click.toString())
            putString("GeneralMoney",money.value.toString())
            apply()
        }
    }
}
