package dev.sasikanth.bas

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log


class AlarmBroadcastReceiver : BroadcastReceiver() {

    companion object {
        const val REQ_CODE = 1402
        const val EXTRA = "extra"

        const val ACTION_NEW_ALARM = "new_alarm"
        const val ACTION_DISMISS = "dismiss_alarm"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null && intent != null) {
            val extra = intent.getStringExtra(EXTRA)
            Log.d(javaClass.simpleName, extra ?: "No extra")

            if (extra == ACTION_NEW_ALARM) {
                context.startService(Intent(context, AlarmSoundService::class.java))
            } else if (extra == ACTION_DISMISS) {
                context.stopService(Intent(context, AlarmSoundService::class.java))
            }
        }
    }
}