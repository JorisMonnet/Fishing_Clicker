package hearc.dev_mobile.fishing_clicker.ui.activities

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.*
import android.util.Log
import android.view.animation.DecelerateInterpolator
import androidx.core.graphics.ColorUtils
import hearc.dev_mobile.fishing_clicker.MainActivity
import hearc.dev_mobile.fishing_clicker.R
import kotlinx.android.synthetic.main.activity_pop_up_shake.*

/**
 * Class managing the shakeEvent page
 */
class PopUpShake : MainActivity() {

    private var sensorManager: SensorManager? = null
    private var acceleration = 0f
    private var currentAcceleration = 0f
    private var lastAcceleration = 0f
    private var shakeFloor = 6
    private var canIncPercent = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(0, 0)
        setContentView(R.layout.activity_pop_up_shake)
        vibration(1500L)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorManager?.registerListener(
            sensorListener, sensorManager!!
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL
        )
        acceleration = 10f
        currentAcceleration = SensorManager.GRAVITY_EARTH
        lastAcceleration = SensorManager.GRAVITY_EARTH


        //animation for background
        val alpha = 100 //between 0-255
        val alphaColor = ColorUtils.setAlphaComponent(Color.parseColor("#000000"), alpha)
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), Color.TRANSPARENT, alphaColor)
        colorAnimation.duration = 500 // milliseconds
        colorAnimation.addUpdateListener { animator ->
            pop_up_shake_background.setBackgroundColor(animator.animatedValue as Int)
        }
        colorAnimation.start()

        //animation for popup window
        pop_up_shake_view_with_border.alpha = 0f
        pop_up_shake_view_with_border.animate().alpha(1f).setDuration(500).setInterpolator(
            DecelerateInterpolator()
        ).start()

        //timer to stop event
        Handler(Looper.getMainLooper()).postDelayed({
            canIncPercent = false
            setDisplayText()
        }, 5000)
        setDisplayText()
    }

    /**
     * Sensor listener for accelerometer
     */
    private val sensorListener: SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]
            lastAcceleration = currentAcceleration
            currentAcceleration = kotlin.math.sqrt((x * x + y * y + z * z).toDouble()).toFloat()
            val delta: Float = currentAcceleration - lastAcceleration
            acceleration = acceleration * 0.9f + delta
            if (acceleration > shakeFloor && canIncPercent) {
                shakeFloor += shakeFloor / 6
                percentToAddAfterShakeEvent += 1
                vibration(750L)
            }
        }
        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
    }

    override fun onResume() {
        sensorManager!!.registerListener(
            sensorListener, sensorManager!!.getDefaultSensor(
                Sensor.TYPE_ACCELEROMETER
            ), SensorManager.SENSOR_DELAY_NORMAL
        )
        super.onResume()
    }

    /**
     * Display text in the popup
     */
    private fun setDisplayText() {
        try {
            popup_window_text.text =
                this.resources.getString(R.string.percent2, percentToAddAfterShakeEvent.toString())
            popup_window_text.textSize = 30F
        } catch (e: Exception) {}

        Handler(Looper.getMainLooper()).postDelayed({
            canIncPercent = true
            onBackPressed()
        }, 3000)
    }

    /**
     * Function when quitting the shake page with an animation
     */
    override fun onBackPressed() {
        setDisplayText()
        // Fade animation for the background of Popup Window when you press the back button
        val alpha = 100 // between 0-255
        val alphaColor = ColorUtils.setAlphaComponent(Color.parseColor("#000000"), alpha)
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), alphaColor, Color.TRANSPARENT)
        colorAnimation.duration = 500 // milliseconds
        colorAnimation.addUpdateListener { animator ->
            pop_up_shake_background.setBackgroundColor(
                animator.animatedValue as Int
            )
        }
        // Fade animation for the Popup Window when you press the back button
        pop_up_shake_view_with_border.animate().alpha(0f).setDuration(500).setInterpolator(
            DecelerateInterpolator()
        ).start()

        // After animation finish, close the Activity
        colorAnimation.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                finish()
                overridePendingTransition(0, 0)
                doShakeReward()
            }
        })
        colorAnimation.start()
    }

    /**
     * Function to vibrate th phone
     * @param delay the delay between vibrations
     */
    private fun vibration(delay: Long) {
        val v = getSystemService(VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(delay, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            //deprecated in API 26
            @Suppress("DEPRECATION")
            v.vibrate(delay)
        }
    }
}