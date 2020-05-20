/*
 * Copyright (C) 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.androidmarki.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import com.example.android.androidmarki.R
import com.example.android.androidmarki.receiver.SnoozeReceiver
import com.example.android.androidmarki.ui.home.HomeActivity


// Notification ID.
private val EGG_TIMER_NOTIFICATION_ID = 0
private val REQUEST_CODE = 0
private val FLAGS = 0

// TODO: Step 1.1 extension function to send messages (GIVEN)
/**
 * Builds and delivers the notification.
 *
 * @param context, activity context.
 */
fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context) {
    // Create the content intent for the notification, which launches
    // this activity
    // TODO: Step 1.11 create intent
    val contentIntent = Intent(applicationContext, HomeActivity::class.java)
    // TODO: Step 1.12 create PendingIntent
    val contentPendingIntent
            = PendingIntent.getActivity(
        applicationContext,
        EGG_TIMER_NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )
//    val r
//    = NavDeepLinkBuilder(applicationContext)
//        .setGraph(R.navigation.home_nav_graph)
//        .setDestination(R.id.nav_egg_timer)
//        .createPendingIntent()

    // TODO: Step 2.0 add style
    val eggImage = BitmapFactory.decodeResource(
        applicationContext.resources,
        R.drawable.egg_timer_cooked_egg
    )
    val bigPicStyle = NotificationCompat.BigPictureStyle()
        .bigPicture(eggImage)
        .bigLargeIcon(null)

    // TODO: Step 2.2 add snooze action
    val snoozeIntent = Intent(applicationContext, SnoozeReceiver::class.java)
    val snoozePendingIntent: PendingIntent = PendingIntent.getBroadcast(
        applicationContext,
        REQUEST_CODE,
        snoozeIntent,
        FLAGS)

    // TODO: Step 1.2 get an instance of NotificationCompat.Builder
    // Build the notification
    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.egg_notification_channel_id)
    )

    // TODO: Step 1.8 use the new 'breakfast' notification channel

    // TODO: Step 1.3 set title, text and icon to builder
        .setSmallIcon(R.drawable.egg_timer_cooked_egg)
        .setContentTitle(applicationContext
            .getString(R.string.notification_title))
        .setContentText(messageBody)

    // TODO: Step 1.13 set content intent
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)

        // TODO: Step 2.1 add style to builder
        .setStyle(bigPicStyle)
        .setLargeIcon(eggImage)

        // TODO: Step 2.3 add snooze action
        .addAction(
            R.drawable.egg_icon,
            applicationContext.getString(R.string.snooze),
            snoozePendingIntent
        )

        // TODO: Step 2.5 set priority
        .setPriority(NotificationCompat.PRIORITY_HIGH)
    // TODO: Step 1.4 call notify
    notify(EGG_TIMER_NOTIFICATION_ID, builder.build())
}

// TODO: Step 1.14 Cancel all notifications
/**
 * Cancels all notifications.
 *
 */
fun NotificationManager.cancelNotifications() {
    cancelAll()
}



/*
 * We need to create a NotificationChannel associated with our CHANNEL_ID before sending a
 * notification.
 */
fun createChannel(context: Context?) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val notificationChannel = NotificationChannel(
            CHANNEL_ID,
            context?.getString(R.string.channel_name),

            NotificationManager.IMPORTANCE_HIGH
        )
            .apply {
                setShowBadge(false)
            }

        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.RED
        notificationChannel.enableVibration(true)
        notificationChannel.description = context?.getString(R.string.notification_channel_description)

        val notificationManager = context?.getSystemService(NotificationManager::class.java)
        notificationManager?.createNotificationChannel(notificationChannel)
    }
}

/*
 * A Kotlin extension function for AndroidX's NotificationCompat that sends our Geofence
 * entered notification.  It sends a custom notification based on the name string associated
 * with the LANDMARK_DATA from GeofencingConstatns in the GeofenceUtils file.
 */
fun NotificationManager.sendGeoFenceEnteredNotification(context: Context, foundIndex: Int) {
    val contentIntent = Intent(context, HomeActivity::class.java)
    contentIntent.putExtra(GeofencingConstants.EXTRA_GEOFENCE_INDEX, foundIndex)
    val contentPendingIntent = PendingIntent.getActivity(
        context,
       GEO_FENCE_NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )
    val mapImage = BitmapFactory.decodeResource(
        context.resources,
        R.drawable.geo_fence_map_small
    )
    val bigPicStyle = NotificationCompat.BigPictureStyle()
        .bigPicture(mapImage)
        .bigLargeIcon(null)

    // We use the name resource ID from the LANDMARK_DATA along with content_text to create
    // a custom message when a Geofence triggers.
    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setContentTitle(context.getString(R.string.app_name))
        .setContentText(context.getString(R.string.content_text,
            context.getString(GeofencingConstants.LANDMARK_DATA[foundIndex].name)))
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setContentIntent(contentPendingIntent)
        .setSmallIcon(R.drawable.geo_fence_map_small)
        .setStyle(bigPicStyle)
        .setLargeIcon(mapImage)

    notify(GEO_FENCE_NOTIFICATION_ID, builder.build())
}

private const val GEO_FENCE_NOTIFICATION_ID = 33
private const val CHANNEL_ID = "GeofenceChannel"