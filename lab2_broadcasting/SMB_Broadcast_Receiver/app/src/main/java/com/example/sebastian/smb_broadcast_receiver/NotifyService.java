package com.example.sebastian.smb_broadcast_receiver;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

public class NotifyService extends IntentService {

    private static final String ACTION_NOTIFY = "com.example.sebastian.smb_broadcast_receiver.action.NOTIFY";

    private static final String EXTRA_MESSAGE = "com.example.sebastian.smb_broadcast_receiver.extra.MESSAGE";

    public NotifyService() {
        super("NotifyService");
    }

    public static void startActionNotify(Context context, String message) {
        Intent intent = new Intent(context, NotifyService.class);
        intent.setAction(ACTION_NOTIFY);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_NOTIFY.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_MESSAGE);
                handleActionNotify(param1);
            }
        }
    }

    private void handleActionNotify(String message) {
// The id of the channel.
      //  String CHANNEL_ID = "my_channel_01";
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("My notification")
                        .setContentText(message);
// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, ActionForNotificationActivity.class);
        resultIntent.putExtra("data",message);
        int mNotificationId = 1;
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(ActionForNotificationActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(mNotificationId, mBuilder.build());
    }
}
