package com.dz.gestures;

import android.content.Context;
import android.content.SharedPreferences;

public class Extras {

    public static void setTheme(Context context, String mode){
        SharedPreferences sharedPreferences = context.getSharedPreferences("prefs",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putString("mode",mode);
        editor.apply();
    }

    public static String getTheme(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("prefs",Context.MODE_PRIVATE);
        return sharedPreferences.getString("mode","light");
    }
}
