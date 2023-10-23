package com.dz.gestures;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import java.util.List;
import java.util.function.Consumer;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // TODO : Register broadcast receiver
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(new PowerButtonReceiver(),intentFilter);
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        endProcess();
    }

    @Override
    protected void onPause() {
        super.onPause();
        endProcess();
    }

    @Override
    protected void onStop() {
        super.onStop();
        endProcess();
    }

    /// TODO : END APP WHEN IT GOES TO BACKGROUND
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