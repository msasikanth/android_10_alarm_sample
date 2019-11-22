package dev.sasikanth.bas

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.AlarmManagerCompat
import androidx.core.content.getSystemService
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val alarmManager = getSystemService<AlarmManager>()
        requireNotNull(alarmManager)

        val broadCastIntent = Intent(this, AlarmBroadcastReceiver::class.java).apply {
            putExtra(AlarmBroadcastReceiver.EXTRA, AlarmBroadcastReceiver.ACTION_NEW_ALARM)
        }
        val pi = PendingIntent.getBroadcast(
            this,
            AlarmBroadcastReceiver.REQ_CODE,
            broadCastIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        startAlarm.setOnClickListener {
            startAlarm(alarmManager, pi)
        }

        cancelAlarm.setOnClickListener {
            cancelAlarm(alarmManager, pi)
        }
    }

    private fun startAlarm(alarmManager: AlarmManager, pi: PendingIntent) {
        Log.d(javaClass.simpleName, "Alarm triggered")
        AlarmManagerCompat.setAndAllowWhileIdle(
            alarmManager,
            AlarmManager.RTC_WAKEUP,
            oneMinuteInFuture(),
            pi
        )
    }

    private fun cancelAlarm(alarmManager: AlarmManager, pi: PendingIntent) {
        Log.d(javaClass.simpleName, "Alarm canceled")
        alarmManager.cancel(pi)
    }

    private fun oneMinuteInFuture(): Long {
        val calendar = Calendar.getInstance().apply {
            add(Calendar.MINUTE, 1)
        }
        return calendar.timeInMillis
    }
}
