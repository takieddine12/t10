package com.dz.gestures;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class MainActivity extends AppCompatActivity {


    private String status = "foreground";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        // TODO : Register broadcast receiver
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
//        registerReceiver(new PowerButtonReceiver(),intentFilter);

        Button button = findViewById(R.id.mode);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Objects.equals(Extras.getTheme(MainActivity.this), "light")) {
                    setDarkMode();
                } else {
                    setLightMode();
                }
            }
        });


        if (Objects.equals(Extras.getTheme(this), "light")) {
            setLightMode();
        } else {
            setDarkMode();
        }
    }


    private void setDarkMode() {
        Extras.setTheme(this, "dark");
        applyTheme(AppCompatDelegate.MODE_NIGHT_YES, Color.LTGRAY);
    }

    private void setLightMode() {
        Extras.setTheme(this, "light");
        applyTheme(AppCompatDelegate.MODE_NIGHT_NO, Color.DKGRAY);
    }

    private void applyTheme(int nightMode, int statusBarColor) {
        AppCompatDelegate.setDefaultNightMode(nightMode);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(statusBarColor);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       // endProcess();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Objects.equals(status, "background")){
            finish();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            status = "foreground";
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        status = "background";
    }

    /// TODO : END APP WHEN IT GOES TO BACKGROUND
    /// TODO : We look for top visible app which is this and kill it
    private void endProcess(){
        ActivityManager am = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        if(am != null) {
            List<ActivityManager.AppTask> tasks = am.getAppTasks();
            if (tasks != null && tasks.size() > 0) {
                tasks.get(0).setExcludeFromRecents(true);
            }
        }
    }


    /// TODO : LISTEN TO POWER KEY PRESS
    public class PowerButtonReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                killApp();
            }
        }
    }


    /// TODO : We look for current visible app and kill it by its packageName
    @SuppressLint("NewApi")
    private void killApp(){
        ActivityManager  manager = (ActivityManager)this.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> listOfProcesses = manager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo process : listOfProcesses) {
            if (process.processName.contains(getPackageName())) {
                finishAndRemoveTask();
                break;
            }
        }

    }


}