package hearc.dev_mobile.fishing_clicker.ui.activities

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Color
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.*
import android.view.animation.DecelerateInterpolator
import androidx.core.graphics.ColorUtils
import hearc.dev_mobile.fishing_clicker.MainActivity
import hearc.dev_mobile.fishing_clicker.R
import kotlinx.android.synthetic.main.activity_pop_up_blow.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlin.math.abs


class PopUpBlow : MainActivity() {

    private var hasBlow = false


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
                    onBackPressed()
                }
            }
        }.start()

        Handler(Looper.getMainLooper()).postDelayed({
            onBackPressed()
        }, 7000)

    }


    override fun onBackPressed() {

        val intent = Intent()
        intent.putExtra(
            "SHARKY", when (hasBlow) {
                true -> 1
                false -> 0
            }
        )
        setResult(RESULT_OK, intent)
        hasBlow = false
        finish()

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

    private fun isBlowing(): Boolean {
        val recorder = true
        val minSize = AudioRecord.getMinBufferSize(
            8000,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )
        val ar = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            8000,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            minSize
        )
        val buffer = ShortArray(minSize)
        ar.startRecording()
        while (recorder) {
            ar.read(buffer, 0, minSize)
            for (s in buffer) {
                if (abs(s.toInt()) > 27000) //DETECT VOLUME (IF I BLOW IN THE MIC)
                {
                    ar.stop()
                    return true
                }
            }
        }
        return false
    }
}