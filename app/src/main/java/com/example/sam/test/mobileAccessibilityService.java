package com.example.sam.test;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TimeZone;

/**
 * Created by sam on 2016/12/1.
 */

public class mobileAccessibilityService  extends AccessibilityService {
    String TAG="in access";
    String[] PACKAGES = { "com.android.systemui" };
    /*int [] TYPES={AccessibilityEvent.TYPE_VIEW_CLICKED,AccessibilityEvent.TYPE_VIEW_CONTEXT_CLICKED,AccessibilityEvent.TYPE_VIEW_LONG_CLICKED,AccessibilityEvent.TYPE_VIEW_SCROLLED
                 ,AccessibilityEvent.TYPE_VIEW_SELECTED};*/
    ArrayList<ArrayList<String>> WaitList= new ArrayList<>();  //for sqlite


    @Override
    protected void onServiceConnected() {
        Log.i("in access", "config success!");
        AccessibilityServiceInfo accessibilityServiceInfo = new AccessibilityServiceInfo();
        //accessibilityServiceInfo.packageNames = PACKAGES;
        accessibilityServiceInfo.eventTypes = AccessibilityEvent.TYPE_TOUCH_INTERACTION_END | AccessibilityEvent.TYPE_TOUCH_INTERACTION_START | AccessibilityEvent.TYPE_VIEW_FOCUSED | AccessibilityEvent.TYPE_VIEW_HOVER_ENTER|AccessibilityEvent.TYPE_VIEW_LONG_CLICKED
                                            | AccessibilityEvent.TYPE_VIEW_HOVER_EXIT| AccessibilityEvent.TYPE_VIEW_CLICKED| AccessibilityEvent.TYPE_VIEW_CONTEXT_CLICKED | AccessibilityEvent.TYPE_VIEW_SCROLLED | AccessibilityEvent.TYPE_VIEW_SELECTED;
        //accessibilityServiceInfo.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED | AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED | AccessibilityEvent.TYPE_WINDOWS_CHANGED;
        accessibilityServiceInfo.feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN;
        accessibilityServiceInfo.notificationTimeout = 1000;
        setServiceInfo(accessibilityServiceInfo);



    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // TODO Auto-generated method stub
        int eventType = event.getEventType();
        String pack="";
        String text="";
        String type="";
        long time;

        if(event.getPackageName()!=null){
            pack=event.getPackageName().toString();
        }
        if(event.getText()!=null){
            text=event.getText().toString();
        }
        time= getCurrentTimeInMillis();

        switch (eventType) {
            /*case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                eventText = "TYPE_WINDOW_STATE_CHANGED";
                break;
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                eventText = "TYPE_WINDOW_CONTENT_CHANGED";
                break;
            case AccessibilityEvent.TYPE_WINDOWS_CHANGED:
                eventText = "TYPE_WINDOWS_CHANGED";
                break;*/
            case AccessibilityEvent.TYPE_TOUCH_INTERACTION_END:
                type="TYPE_TOUCH_INTERACTION_END";
                break;
            case AccessibilityEvent.TYPE_TOUCH_INTERACTION_START:
                type="TYPE_TOUCH_INTERACTION_START";
                break;
            case AccessibilityEvent.TYPE_VIEW_FOCUSED:
                type="TYPE_VIEW_FOCUSED";
                break;
            case AccessibilityEvent.TYPE_VIEW_HOVER_ENTER:
                type="TYPE_VIEW_HOVER_ENTER";
                break;
            case AccessibilityEvent.TYPE_VIEW_HOVER_EXIT:
                type="TYPE_VIEW_HOVER_EXIT";
                break;
            case AccessibilityEvent.TYPE_VIEW_CLICKED:
                type="TYPE_VIEW_CLICKED";
                break;
            case AccessibilityEvent.TYPE_VIEW_CONTEXT_CLICKED:
                type="TYPE_VIEW_CONTEXT_CLICKED";
                break;
            case AccessibilityEvent.TYPE_VIEW_LONG_CLICKED:
                type="TYPE_VIEW_LONG_CLICKED";
                break;
            case AccessibilityEvent.TYPE_VIEW_SCROLLED:
                type="TYPE_VIEW_SCROLLED";
                break;
            case AccessibilityEvent.TYPE_VIEW_SELECTED:
                type="TYPE_VIEW_SELECTED";
                break;
        }

        //eventText = eventText + ":" + eventType;
        Log.i(TAG,"pack= "+pack+"   text= "+text+"    type= "+type+" state= "+ScreenStatusReceiver.ScreenState);
        //Log.d("in access","pack= "+pack+" event = "+eventText+" text= "+text);

        String convert_time=DateFormat.format("yyyy-MM-dd HH:mm:ss z",time).toString();
        String [] access_arr={convert_time,String.valueOf(time),text,type,ScreenStatusReceiver.ScreenState};
        try{
            UsageService.checkWriterAlive();
            UsageService.access_writer.writeNext(access_arr);
            UsageService.access_writer.flush();
        }catch (IOException e){
            e.printStackTrace();
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        ArrayList<String> info = new ArrayList<>();
        info.add(toJsonObject(text,time,type).toString());

        //UsageService.insertSQLite("accessibility",info);

        if(UsageService.usage_start==1){
            try{
                if(DatabaseManager.getInstance() == null){
                    Log.d("access","in null");
                    WaitList.add(info);
                }else{
                    WaitList.add(info);
                    for(int i=0;i<WaitList.size();i++){
                        UsageService.insertSQLite("accessibility",WaitList.get(i));
                    }
                    Log.d("access","not null");

                    WaitList.clear();
                }

            }catch (IllegalStateException e){
                WaitList.add(info);
                DBHelper dbhelper = new DBHelper(this);
                DatabaseManager.initializeInstance(dbhelper);
                e.printStackTrace();
            }

        }
        info.clear();

        if(Math.abs( (getCurrentTimeInMillis()-UsageService.nowtime) / (1000*60) ) > 1 ){
            Log.d("usage","reopen usage");
            UsageService.writeSystemLog(getCurrentTimeInMillis(),"accessibility service reopen usage service, usage service stop at = "+UsageService.nowtime);
            Intent usage_service = new Intent(mobileAccessibilityService.this, UsageService.class);
            startService(usage_service);
        }

    }

    @Override
    public void onInterrupt() {
        // TODO Auto-generated method stub

    }
    public static long getCurrentTimeInMillis(){
        //get timzone
        TimeZone tz = TimeZone.getDefault();
        java.util.Calendar cal = java.util.Calendar.getInstance(tz);
        long t = cal.getTimeInMillis();
        return t;
    }

    public JSONObject toJsonObject(String text,long time,String type){

        JSONObject content = new JSONObject();
        try{
            String format_time = DateFormat.format("yyyy-MM-dd HH:mm:ss z",time).toString();
            content.put("CurrentTime",format_time);
            content.put("CurrentTimeInMillis",String.valueOf(time));
            content.put("EventText",text);
            content.put("EventType",type);
            content.put("ScreenState",ScreenStatusReceiver.ScreenState);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return content;
    }





}
