package br.ufc.great.contextplayer.receiver;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import br.ufc.great.contextplayer.R;

public class HeadphoneReceiver extends BroadcastReceiver {

    private String CHANNEL_ID = "awareness-messages";

    @Override
    public void onReceive(Context context, Intent intent) {
        //push a notification

    }

    private void pushNotification(Context context){

        NotificationCompat.Builder builder;
        builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        builder.setSmallIcon(R.mipmap.ic_launcher_round);
        builder.setChannelId(CHANNEL_ID);
        builder.setContentTitle("Headphone plugged");
        builder.setContentText("\"recommend best playlist here\"");


        NotificationManagerCompat manager = context.getSystemService(NotificationManagerCompat.class);
        manager.notify(145, builder.build());
    }

    private void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Awareness Messages";
            String description = "Messages pushed using Awareness API";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            //if notification channel exists..
            if(notificationManager.getNotificationChannel(CHANNEL_ID) != null)
                return;
            notificationManager.createNotificationChannel(channel);
        }
    }
}
