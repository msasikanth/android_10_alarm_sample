package dev.sasikanth.bas

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlin.random.Random

class AlarmSoundService : Service() {

    companion object {
        private const val ALARM_CHANNEL_ID = "id_alarm"
    }

    private val alarmUri: Uri? = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
    private var alarmRingtone: Ringtone? = null

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        alarmRingtone = RingtoneManager.getRingtone(applicationContext, alarmUri)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        alarmRingtone?.play()
        showNotification(applicationContext)
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        alarmRingtone?.stop()
        alarmRingtone = null
        stopForeground(true)
        super.onDestroy()
    }

    private fun showNotification(context: Context) {
        createNotificationChannel(context)

        val alarmIntent = Intent(context, AlarmActivity::class.java)
        val alarmPendingIntent = PendingIntent.getActivity(
            context,
            0,
            alarmIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notificationBuilder = NotificationCompat.Builder(context, ALARM_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Wake up!")
            .setContentText("Time to do exercise")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setFullScreenIntent(alarmPendingIntent, true)
            .setOngoing(true)
            .addAction(
                NotificationCompat.Action(
                    0,
                    context.getString(R.string.dismiss),
                    dismissAlarm(context)
                )
            )

        val alarmNotification = notificationBuilder.build()
        startForeground(Random.nextInt(), alarmNotification)
    }

    private fun dismissAlarm(context: Context): PendingIntent {
        val silenceAlarmIntent = Intent(context, AlarmBroadcastReceiver::class.java).apply {
            putExtra(AlarmBroadcastReceiver.EXTRA, AlarmBroadcastReceiver.ACTION_DISMISS)
        }
        return PendingIntent.getBroadcast(
            context,
            0,
            silenceAlarmIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun createNotificationChannel(context: Context) {
        NotificationManagerCompat.from(context).createNotificationChannel(
            NotificationChannel(
                ALARM_CHANNEL_ID,
                "Alarms",
                NotificationManager.IMPORTANCE_HIGH
            )
        )
    }
}
