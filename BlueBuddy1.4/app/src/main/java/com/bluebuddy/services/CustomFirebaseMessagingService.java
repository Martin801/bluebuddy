package com.bluebuddy.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.bluebuddy.R;
import com.bluebuddy.activities.BuddyContactList;
import com.bluebuddy.activities.ChatMessagingActivity;
import com.bluebuddy.activities.MobileVerificationActivity;
import com.bluebuddy.activities.NotificationActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class CustomFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "CustomFMS";

    private String type = null;
    private String firebaseRegId = null;
    private String name;
    private String email;
    private String image;

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
//            moveToIntent(remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        //sendNotification(remoteMessage.getNotification().getBody());
        //    showNotification(remoteMessage);
        showNotification(remoteMessage.getNotification(), remoteMessage.getData());
        //  showNotification2(remoteMessage.getNotification().getBody());
    }

    // [END receive_message]

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String messageBody) {
        /*Intent intent = new Intent(this, ChatActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 *//* Request code *//*, intent,
                PendingIntent.FLAG_ONE_SHOT);
        long[] pattern = {500,500,500,500,500};

        Bitmap icon = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                R.drawable.ic_stat_ic_notification);
        Uri customUri = Uri.parse("android.resource://com.coderefer.firebasecloudmessagingtutorial/" + R.raw.pikachu);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setSmallIcon(R.drawable.ic_stat_ic_notification);
        notificationBuilder.setLargeIcon(icon)
                .setContentTitle("FCM Tutorial")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setVibrate(pattern)
                .setSound(customUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 *//* ID of notification *//*, notificationBuilder.build());*/
    }

    private void showNotification(RemoteMessage.Notification notification, Map<String, String> headerData) {
        Intent inten = null;
        // private void showNotification(RemoteMessage remoteMessage) {
        Uri uriDefaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), getString(R.string.default_notification_channel_id))
                .setSmallIcon(R.drawable.app_icon)
                .setLargeIcon((((BitmapDrawable) getResources().getDrawable(R.drawable.app_icon)).getBitmap()))
                .setContentTitle(notification.getTitle())
                .setContentText(notification.getBody())
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(notification.getBody()))
                .setContentInfo("BlueBuddy")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setTicker("BlueBuddy")
                .setSound(uriDefaultSound);

        if (headerData.size() > 0) {

            moveToIntent(headerData);

            switch (type) {

                case "1":
                    inten = new Intent(CustomFirebaseMessagingService.this, ChatMessagingActivity.class);
                    inten.putExtra("receiver_id", firebaseRegId);
                    inten.putExtra("email", email);
                    inten.putExtra("image", image);
                    inten.putExtra("name", name);
                    inten.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    break;

                case "2":
                    inten = new Intent(CustomFirebaseMessagingService.this, NotificationActivity.class);
                    inten.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    break;

                case "3":
                    inten = new Intent(CustomFirebaseMessagingService.this, BuddyContactList.class);
                    inten.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    break;

                case "4":
                    inten = new Intent(CustomFirebaseMessagingService.this, NotificationActivity.class);
                    inten.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    break;

                case "5":
                    /*inten = new Intent(CustomFirebaseMessagingService.this, MobileVerificationActivity.class);
                    inten.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);*/
                    break;

                default:
                    inten = new Intent(this, MobileVerificationActivity.class);
                    inten.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    break;
            }
        }

        if (inten != null) {
            PendingIntent intent = PendingIntent.getActivity(this, 0, inten, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(intent);
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(getString(R.string.default_notification_channel_id),
                    "YOUR_CHANNEL_NAME",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("YOUR_NOTIFICATION_CHANNEL_DISCRIPTION");
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(1, builder.build());

    }

    private void moveToIntent(Map<String, String> headerData) {
        type = headerData.get("push_type");
        firebaseRegId = headerData.get("receiver_id");
        email = headerData.get("email");
        name = headerData.get("name");
        image = headerData.get("image");
    }

//    private void showNotification2(String messageBody) {
//        Intent inten = new Intent(this, MyProfileActivity.class);
//        inten.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//        // private void showNotification(RemoteMessage remoteMessage) {
//        Uri uriDefaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext())
//                .setSmallIcon(R.drawable.app_icon)
//                .setLargeIcon((((BitmapDrawable) getResources().getDrawable(R.drawable.app_icon)).getBitmap()))
//                .setContentTitle("BlueBuddy")
//                .setContentText(messageBody)
//                // .setContentText(remoteMessage.getNotification().getBody())
//                .setContentInfo("0")
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setAutoCancel(true)
//                .setTicker("BlueBuddy").setColor(Color.BLUE)
//                .setSound(uriDefaultSound);
//
//       /* Intent inten = new Intent(this, MobileVerificationActivity.class);
//        inten.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);*/
//
//        PendingIntent intent = PendingIntent.getActivity(this, 0, inten, PendingIntent.FLAG_UPDATE_CURRENT);
//        builder.setContentIntent(intent);
//
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(1, builder.build());
//    }
}
