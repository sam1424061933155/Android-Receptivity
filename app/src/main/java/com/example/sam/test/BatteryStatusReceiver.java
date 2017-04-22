package com.example.sam.test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.util.Log;

/**
 * Created by sam on 2017/1/4.
 */

public class BatteryStatusReceiver extends BroadcastReceiver {

    private static final String LOG_TAG = "BatteryStatusReceiver ";


    @Override
    public void onReceive(Context context, Intent batteryStatus){
        Log.d("battery","in battery status");

        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        float battPercentage = level/(float)scale;
        Log.d("battery","data : level= "+level+" percentage= "+battPercentage);

        UsageService.setBatteryLevel(level);
        UsageService.setBatteryPercentage(battPercentage);

        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;

        UsageService.setIsCharging(isCharging);

        int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;


        if (!isCharging){
            UsageService.setBatteryChargingState("Not Charging");
        }
        else if (chargePlug==BatteryManager.BATTERY_PLUGGED_USB){
            UsageService.setBatteryChargingState("USB Charging");
        }else if (chargePlug==BatteryManager.BATTERY_PLUGGED_AC){
            UsageService.setBatteryChargingState("AC Charging");
        }



    }
}
