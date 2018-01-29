package com.example.sebastian.smb;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

final class LayoutHelper {
    private static final String COLOUR = "colour";

    public static void LoadActionBarColor(Activity activity) {
        SharedPreferences prefs = activity.getSharedPreferences("general_settings", Context.MODE_PRIVATE);
        String appColor = prefs.getString(COLOUR, "#000000");
        ActionBar actionBar = activity.getActionBar();
        int backgroundColor = Color.BLACK;
        try
        {
            backgroundColor =  Color.parseColor(appColor);
        }
        catch(Exception e){
        }
        actionBar.setBackgroundDrawable(new ColorDrawable(backgroundColor));
    }
}
