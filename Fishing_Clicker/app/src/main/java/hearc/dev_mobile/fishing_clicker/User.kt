package hearc.dev_mobile.fishing_clicker

import android.graphics.drawable.Drawable
import java.math.BigInteger
import kotlin.math.pow

class User {
    var level : Int =  0
    var click = BigInteger("1")
    var money : Money = Money()
    fun getClickValue(): BigInteger {
        return click.multiply(BigInteger.valueOf(10.0.pow(level).toLong()))
    }
}