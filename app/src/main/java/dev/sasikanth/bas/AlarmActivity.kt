package dev.sasikanth.bas

import android.app.KeyguardManager
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import kotlinx.android.synthetic.main.activity_alarm.*

class AlarmActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)
        setShowWhenLocked(true)
        setTurnScreenOn(true)
        getSystemService<KeyguardManager>()?.requestDismissKeyguard(this, null)

        silenceAlarm.setOnClickListener {
            val silenceAlarmIntent = Intent(this, AlarmBroadcastReceiver::class.java).apply {
                putExtra(AlarmBroadcastReceiver.EXTRA, AlarmBroadcastReceiver.ACTION_DISMISS)
            }
            sendBroadcast(silenceAlarmIntent)
        }
    }
}
