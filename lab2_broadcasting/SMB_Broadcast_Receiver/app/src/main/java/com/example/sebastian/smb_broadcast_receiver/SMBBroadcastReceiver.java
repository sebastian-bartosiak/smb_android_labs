package com.example.sebastian.smb_broadcast_receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import static android.content.ContentValues.TAG;

public class SMBBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String message = intent.getStringExtra("data");
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        NotifyService.startActionNotify(context, message);
    }
}
