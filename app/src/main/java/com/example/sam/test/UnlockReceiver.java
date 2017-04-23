package com.example.sam.test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TimeZone;

/**
 * Created by sam on 2017/1/4.
 */

public class UnlockReceiver extends BroadcastReceiver {

    private static final String LOG_TAG = "UnlockReceiver ";
    ArrayList<ArrayList<String>> WaitList= new ArrayList<>();  //for sqlite



    @Override
    public void onReceive(Context context, Intent intent){

        if(intent.getAction().equalsIgnoreCase(Intent.ACTION_USER_PRESENT)){
            Log.d("in access","user present");
            long time=getCurrentTimeInMillis();
            String format_time = DateFormat.format("yyyy-MM-dd HH:mm:ss z",time).toString();

            String [] access_arr={format_time,String.valueOf(time),"","USER_PRESENT",ScreenStatusReceiver.ScreenState};
            try{
                UsageService.checkWriterAlive();
                UsageService.access_writer.writeNext(access_arr);
                UsageService.access_writer.flush();
            }catch (IOException e){
                e.printStackTrace();
            }catch (NullPointerException e){
                e.printStackTrace();
            }


            JSONObject content = new JSONObject();
            try{
                content.put("CurrentTime",format_time);
                content.put("CurrentTimeInMillis",String.valueOf(time));
                content.put("EventText","");
                content.put("EventType","USER_PRESENT");
                content.put("ScreenState",ScreenStatusReceiver.ScreenState);

            }catch (JSONException e){
                e.printStackTrace();
            }

            ArrayList<String> info = new ArrayList<>();
            info.add(content.toString());
            if(UsageService.usage_start==1){
                try{
                    if(DatabaseManager.getInstance() == null){
                        Log.d("access","in null");
                        WaitList.add(info);
                    }else{
                        try{
                            WaitList.add(info);
                            for(int i=0;i<WaitList.size();i++){
                                UsageService.insertSQLite("accessibility",WaitList.get(i));
                            }
                            Log.d("access","not null");
                        }finally {
                            WaitList.clear();
                        }
                    }

                }catch (IllegalStateException e){
                    WaitList.add(info);
                    DBHelper dbhelper = new DBHelper(context);
                    DatabaseManager.initializeInstance(dbhelper);
                    e.printStackTrace();
                }

            }

            //UsageService.insertSQLite("accessibility",info);
            //info.clear();
        }

    }
    public long getCurrentTimeInMillis(){
        //get timzone
        TimeZone tz = TimeZone.getDefault();
        java.util.Calendar cal = java.util.Calendar.getInstance(tz);
        long t = cal.getTimeInMillis();
        return t;
    }
}