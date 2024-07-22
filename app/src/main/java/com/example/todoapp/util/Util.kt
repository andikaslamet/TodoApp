package com.example.todoapp.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.todoapp.R
import com.example.todoapp.model.TodoDatabase
import com.example.todoapp.view.MainActivity

val DB_NAME = "newtododb"

fun buildDb(context: Context): TodoDatabase {
    val db = Room.databaseBuilder(context,
        TodoDatabase::class.java, DB_NAME)
        .addMigrations(MIGRATION_1_2, MIGRATION_2_3) // Added new migration
        .build()

    return db
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "ALTER TABLE todo ADD COLUMN priority INTEGER DEFAULT 3 not null")
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "ALTER TABLE todo ADD COLUMN is_done INTEGER DEFAULT 0 NOT NULL" // karena SQLite tidak memiliki tipe boolean, jadi menggunakan int yang lebih compatible
        )
    }
}

class NotificationHelper(val context: Context) {
    private val CHANNEL_ID = "todo_channel_id"
    private val NOTIFICATION_ID = 1

    companion object {
        val REQUEST_NOTIF = 100
    }

    private fun createNotificationChannel( ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            val chanel = NotificationChannel(CHANNEL_ID, CHANNEL_ID, NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = "Todo channel desc"
            }
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE)as NotificationManager
            notificationManager.createNotificationChannel(chanel)
        }
    }

    fun createNotification( title:String, message:String) {
        createNotificationChannel()
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(context,0, intent, PendingIntent.FLAG_IMMUTABLE)
        val icon = BitmapFactory.decodeResource(context.resources, R.drawable.todochar)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.checklist)
            .setLargeIcon(icon)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigPicture(icon)
                    .bigLargeIcon(null)
            )
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
        try {
            NotificationManagerCompat.from(context)
                .notify(NOTIFICATION_ID, notification)
        } catch (e:SecurityException) {
            Log.e("error", e.toString())
        }
    }



}

class TodoWorker (context: Context, params:WorkerParameters): Worker(context,params) {
    override fun doWork(): Result {
        NotificationHelper(applicationContext).createNotification(
            inputData.getString("title").toString(),
            inputData.getString("message").toString())
        return Result.success()


    }
}