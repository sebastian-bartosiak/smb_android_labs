package com.example.sebastian.smb;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class SettingsActivity extends Activity {

    private final String FONT_SIZE = "font_size";
    private final String COLOUR = "colour";
    private EditText colourET;
    private EditText fontSizeET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        LayoutHelper.LoadActionBarColor(this);
        this.attachControls();
        this.loadData();
    }

    public void applySettingsEvent(View view){
        this.saveSettings();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void attachControls() {
        colourET = (EditText)findViewById(R.id.colour);
        fontSizeET = (EditText)findViewById(R.id.font_size);
    }

    private void loadData() {
        SharedPreferences prefs = this.getSharedPreferences("general_settings", Context.MODE_PRIVATE);
        colourET.setText(prefs.getString(COLOUR, "#000000"));

        int fontSize = prefs.getInt(FONT_SIZE, 15);
        fontSizeET.setText(String.valueOf(fontSize));

        TextView tv1 = (TextView) findViewById(R.id.textView);
        TextView tv2 = (TextView) findViewById(R.id.textView2);
        tv1.setTextSize(fontSize);
        tv2.setTextSize(fontSize);
    }

    private void saveSettings(){
        SharedPreferences prefs = this.getSharedPreferences("general_settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        int fontSize = 15;
        try{
            fontSize = Integer.parseInt(fontSizeET.getText().toString());
        }
        catch(Exception e){
        }

        editor.putInt(FONT_SIZE, fontSize);
        editor.putString(COLOUR, colourET.getText().toString());
        editor.commit();
    }
}
