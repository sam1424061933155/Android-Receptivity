package com.example.sam.test;

import android.accessibilityservice.AccessibilityService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.TimeZone;
import java.util.logging.LogManager;

/**
 * Created by sam on 2016/12/2.
 */

public class BootCompleteReceiver extends BroadcastReceiver {

    private static final String LOG_TAG = "BootCompleteReceiver";
    private  static DBHelper dbhelper = null;


    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED))
        {
            Log.d("reboot","boot_complete in first");

            try{
                dbhelper = new DBHelper(context);
                dbhelper.getWritableDatabase();
                Log.d("reboot","db is ok");

            }finally {

                Log.d(LOG_TAG, "Successfully receive reboot request");
                //here we start the service
                Intent sintent = new Intent(context, UsageService.class);
                context.startService(sintent);
                Log.d("reboot","usage is ok");


                Intent nintent = new Intent(context, NotificationService_2.class);
                context.startService(nintent);
                Log.d("reboot","notification is ok");
            }






        }

    }

}
