package com.dicoding.myapplication

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import androidx.work.Worker
import androidx.work.WorkerParameters

class NotificationWorker(ctx: Context, params: WorkerParameters): Worker(ctx, params) {

    private val channelName = inputData.getString(NOTIFICATION_CHANNEL_ID)

    private fun getPendingIntent(): PendingIntent? {
        val intent = Intent(applicationContext, MainActivity::class.java)
        return TaskStackBuilder.create(applicationContext).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }
    }

    override fun doWork(): Result {
        val notificationManagerCompat = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder = channelName?.let { NotificationCompat.Builder(applicationContext, it) }
            ?.setSmallIcon(R.drawable.ic_notifications)
            ?.setContentTitle("REMINDER")
            ?.setContentText("MAKAN BANG")
            ?.setContentIntent(getPendingIntent())
            ?.setPriority(NotificationCompat.PRIORITY_DEFAULT)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            //WAJIB
            val channel = NotificationChannel(
                channelName,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            //----
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            //WAJIB
            if (channelName != null) {
                builder?.setChannelId(channelName)
            }
            //WAJIB
            notificationManagerCompat.createNotificationChannel(channel)
        }
        val notification = builder?.build()
        notificationManagerCompat.notify(10, notification)
        return  Result.success()
    }
    companion object{
        val NOTIFICATION_CHANNEL_ID = "notification_channel_id"
    }

}