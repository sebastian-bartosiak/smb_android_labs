package com.example.sebastian.smb_widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;

/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {

    private static boolean firstPhotoIsSet;
    private static MediaPlayer mediaPlayer;
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        views.setOnClickPendingIntent(R.id.web_btn, CreateWebIntent(context));
        views.setOnClickPendingIntent(R.id.photo_btn, CreatePendingIntent(context, "changePhoto"));
        views.setOnClickPendingIntent(R.id.play_btn, CreatePendingIntent(context, "startMusic"));
        views.setOnClickPendingIntent(R.id.stop_btn, CreatePendingIntent(context, "stopMusic"));
        views.setImageViewResource(R.id.imageView3, R.drawable.first);
        firstPhotoIsSet = true;
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals("changePhoto")){
            AppWidgetManager mgr = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = mgr.getAppWidgetIds(new ComponentName(context, NewAppWidget.class));
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
            if(firstPhotoIsSet){
                views.setImageViewResource(R.id.imageView3, R.drawable.second);
            }
            else {
                views.setImageViewResource(R.id.imageView3, R.drawable.first);
            }
            firstPhotoIsSet = !firstPhotoIsSet;
            mgr.updateAppWidget(appWidgetIds, views);
        }
        else if(intent.getAction().equals("startMusic")){
            mediaPlayer = MediaPlayer.create(context, R.raw.nokia_ringtone);
            mediaPlayer.start();
        }
        else if(intent.getAction().equals("stopMusic")){
            mediaPlayer.stop();
            mediaPlayer.reset();
        }



        super.onReceive(context, intent);
    }

    public static PendingIntent CreateWebIntent(Context context){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse("https://google.com"));
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        return pendingIntent;
    }

    public static PendingIntent CreatePendingIntent(Context context, String action){
        Intent intent = new Intent(context, NewAppWidget.class);
        intent.setAction(action);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        return pendingIntent;
    }
}

