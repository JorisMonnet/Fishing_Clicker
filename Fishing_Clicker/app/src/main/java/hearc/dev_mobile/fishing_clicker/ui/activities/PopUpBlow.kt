package hearc.dev_mobile.fishing_clicker.ui.activities

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.Color
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.*
import android.util.Log
import android.view.animation.DecelerateInterpolator
import androidx.core.graphics.ColorUtils
import hearc.dev_mobile.fishing_clicker.MainActivity
import hearc.dev_mobile.fishing_clicker.R
import kotlinx.android.synthetic.main.activity_pop_up_blow.*
import java.math.BigInteger


class PopUpBlow : MainActivity() {

    var hasBlow = false

    fun isBlowing(): Boolean {
        var recorder = true
        val minSize = AudioRecord.getMinBufferSize(
            8000,
            AudioFormat.CHANNEL_CONFIGURATION_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )
        val ar = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            8000,
            AudioFormat.CHANNEL_CONFIGURATION_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            minSize
        )
        val buffer = ShortArray(minSize)
        ar.startRecording()
        while (recorder) {
            ar.read(buffer, 0, minSize)
            for (s in buffer) {
                if (Math.abs(s.toInt()) > 27000) //DETECT VOLUME (IF I BLOW IN THE MIC)
                {
                    val blow_value = Math.abs(s.toInt())
                    System.out.println("Blow Value=${blow_value}")
                    ar.stop()
                    recorder = false
                    return true
                }
            }
        }
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(0, 0)
        setContentView(R.layout.activity_pop_up_blow)
        vibration(1500L)


        //animation for background
        val alpha = 100 //between 0-255
        val alphaColor = ColorUtils.setAlphaComponent(Color.parseColor("#000000"), alpha)
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), Color.TRANSPARENT, alphaColor)
        colorAnimation.duration = 500 // milliseconds
        colorAnimation.addUpdateListener { animator ->
            pop_up_blow_background.setBackgroundColor(animator.animatedValue as Int)
        }
        colorAnimation.start()

        //animation for popup window
        pop_up_blow_view_with_border.alpha = 0f
        pop_up_blow_view_with_border.animate().alpha(1f).setDuration(500).setInterpolator(
            DecelerateInterpolator()
        ).start()

        Thread {//AFK MECHANISM
            while (true) {
                if (!hasBlow) {
                    hasBlow = isBlowing()
                } else {
                    Log.v("Blow detected", hasBlow.toString())
                    onBackPressed()
                }
            }
        }.start()

    }


    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onBackPressed() {
        // Fade animation for the background of Popup Window when you press the back button
        val alpha = 100 // between 0-255
        val alphaColor = ColorUtils.setAlphaComponent(Color.parseColor("#000000"), alpha)
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), alphaColor, Color.TRANSPARENT)
        colorAnimation.duration = 500 // milliseconds
        colorAnimation.addUpdateListener { animator ->
            pop_up_blow_background.setBackgroundColor(
                animator.animatedValue as Int
            )
        }

        // Fade animation for the Popup Window when you press the back button
        pop_up_blow_view_with_border.animate().alpha(0f).setDuration(500).setInterpolator(
            DecelerateInterpolator()
        ).start()

        // After animation finish, close the Activity
        colorAnimation.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                finish()
                hasBlow = false
                overridePendingTransition(0, 0)
            }
        })
        colorAnimation.start()
    }

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