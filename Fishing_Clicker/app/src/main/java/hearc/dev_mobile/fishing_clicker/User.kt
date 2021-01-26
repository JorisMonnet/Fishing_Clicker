package hearc.dev_mobile.fishing_clicker

import android.content.SharedPreferences
import android.util.Log
import kotlinx.android.synthetic.main.app_bar_main.*
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.math.pow

class User(private val mainActivity: MainActivity) {
    var levelProgress: Int = 0
    var level: Int = 0
    private var click = BigInteger.ONE
    var money: Money = Money(BigInteger.ZERO)

    fun doLevel() {
        if (money.value.compareTo(
                BigInteger.valueOf(
                    10.0.pow(level * 3 + 6).toLong()
                )
            ) == 1
        ) {
            level++
            //TODO()  //change background when changing of level
            //contentLayout.background = R.drawable.bgLevel1
        } else {
            val topFloor = BigInteger.valueOf(
                10.0.pow(level * 3 + 6).toLong()
            )
            var percent=BigDecimal(money.value/topFloor)
            Log.v("pp",percent.toString())
        }
    }

    /**
     * Function to get value of the click with the given user level
     * @return the value of the click
     */
    fun getClickValue(): BigInteger {
        return click.multiply(BigInteger.valueOf(10.0.pow(level).toLong()))
    }

    /**
     * Get the value of the user from the shared Preferences when game is quit and restart
     * @param sharedPrefUser the SharedPreferences
     */
    fun createValuesFromPref(sharedPrefUser: SharedPreferences) {
        val moneyPref = sharedPrefUser.getString("GeneralMoney", "0")
        money.value = if (moneyPref != null) BigInteger(moneyPref) else BigInteger.ZERO
        val clickPref = sharedPrefUser.getString("Click", "1")
        click = if (clickPref != null) BigInteger(clickPref) else BigInteger.ONE
        level = sharedPrefUser.getInt("Level", 0)
        money.value = money.value.add(BigInteger.valueOf(5000))//TODO TOREMOVE
    }

    /**
     * save the user Data
     * @param sharedPrefUser the sharedPreferences where it's saved
     */
    fun saveData(sharedPrefUser: SharedPreferences) {
        with(sharedPrefUser.edit()) {
            putInt("LevelUser", level)
            putString("Click", click.toString())
            putString("GeneralMoney", money.value.toString())
            apply()
        }
    }
}
