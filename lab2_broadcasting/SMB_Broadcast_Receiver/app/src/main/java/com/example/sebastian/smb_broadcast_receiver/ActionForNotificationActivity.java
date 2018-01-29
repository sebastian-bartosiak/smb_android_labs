package com.example.sebastian.smb_broadcast_receiver;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ActionForNotificationActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_for_notification);
        TextView tv = (TextView)findViewById(R.id.textView2);
        String message = getIntent().getStringExtra("data");
        tv.setText(message);
    }

}
