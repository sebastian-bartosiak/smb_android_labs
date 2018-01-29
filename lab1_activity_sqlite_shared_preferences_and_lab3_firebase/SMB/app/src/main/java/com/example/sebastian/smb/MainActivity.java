package com.example.sebastian.smb;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        populateDefaultSettings();
        LayoutHelper.LoadActionBarColor(this);
    }

    public void goToListEvent(View view){
        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
    }

    public void goToSettingsEvent(View view){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private void populateDefaultSettings() {
        SharedPreferences prefs = this.getSharedPreferences("general_settings", Context.MODE_PRIVATE);

        if (prefs.getString("colour", null) == null) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("font_size", 15);
            editor.putString("colour", "#000000");
            editor.commit();
        }
    }

}
