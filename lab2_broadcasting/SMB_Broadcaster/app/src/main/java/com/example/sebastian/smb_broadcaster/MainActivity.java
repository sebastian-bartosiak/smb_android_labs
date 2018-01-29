package com.example.sebastian.smb_broadcaster;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {

    private EditText et_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_message = (EditText)findViewById(R.id.editText);
    }

    public void send(View view){
        Intent intent = new Intent();
        intent.setAction("com.example.sebastian.smb_broadcaster.MY_SUPER_NOTIFICATION");
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        String message = et_message.getText().toString();
        intent.putExtra("data",message);
        sendBroadcast(intent, "com.example.sebastian.smb_broadcaster.BROADCASTING_ACTIVITY");
    }
}
