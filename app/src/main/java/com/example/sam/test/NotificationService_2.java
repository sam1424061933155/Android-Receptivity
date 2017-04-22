package com.example.sam.test;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.Log;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;

public class NotificationService_2 extends NotificationListenerService {

    PowerManager powerMgr;
    Intent noti_service;
    static ArrayList<ArrayList<String>> questionnaire_list= new ArrayList<>();
    static ArrayList<String []> info_list= new ArrayList<>();
    //private DBHelper dbhelper = null;
    ArrayList<ArrayList<String>> WaitList= new ArrayList<>();  //for sqlite



    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("noti", "onCreate");
        /*dbhelper = new DBHelper(this);
        dbhelper.getWritableDatabase();*/
    }

    @Override
    public IBinder onBind(Intent mIntent) {
        IBinder mIBinder = super.onBind(mIntent);
        noti_service=mIntent;
        Log.i("noti", "onBind");
        return mIBinder;
    }

    @Override
    public boolean onUnbind(Intent mIntent) {
        boolean mOnUnbind = super.onUnbind(mIntent);
        Log.i("noti", "onUnbind");
        try {
        } catch (Exception e) {
            Log.e("noti", "Error during unbind", e);
        }
        return mOnUnbind;
    }

    public void onDestroy() {
        Log.i("noti", "stop noti sevice");
        UsageService.checkWriterAlive();

        try{
            if(info_list.size()!=0){
                UsageService.noti_writer.writeAll(info_list);
                UsageService.noti_writer.flush();
                info_list.clear();
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                if(UsageService.noti_writer!=null ){
                    UsageService.noti_writer.close();
                }
                UsageService.noti_writer=null;
                Log.d("csv","close successful");

            } catch (IOException e) {
                Log.d("csv","close file error");
                e.printStackTrace();
            }
        }
        if(noti_service!=null){
            stopService(noti_service);
        }
        super.onDestroy();
    }



    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {


        ArrayList<String> info = new ArrayList<>();
        //info=new ArrayList<>();
        //Set<String> info = new HashSet<>();

        String package_name = sbn.getPackageName();
        info.add(package_name);

        info.add("0");  // 0 for post

        /*Date date = new Date(sbn.getPostTime());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        String post_date = simpleDateFormat.format(date);*/
        String post_date = DateFormat.format("yyyy-MM-dd HH:mm:ss z", sbn.getPostTime()).toString();
        info.add(post_date);

        String tag=sbn.getTag();
        String s_tag=" ";
        if(tag!=null){
            s_tag=tag.toString();
        }
        info.add(s_tag);

        CharSequence ticket = sbn.getNotification().tickerText;
        String s_ticket=" ";
        if(ticket!=null){
            s_ticket=ticket.toString();
        }
        info.add(s_ticket);

        long[] vibrate_mode=sbn.getNotification().vibrate;
        String s_vibrate_mode="";
        if(vibrate_mode!=null){
            for(long pattern : vibrate_mode){
                s_vibrate_mode=s_vibrate_mode+String.valueOf(pattern)+" ";
            }
        }
        info.add(s_vibrate_mode);

        Uri sound_mode=sbn.getNotification().sound;
        String s_sound_mode=" ";
        if(sound_mode!=null){
            s_sound_mode=sound_mode.toString();
        }
        info.add(s_sound_mode);

        String category=sbn.getNotification().category;
        String s_category=" ";
        if(category!=null){
            s_category=category;
        }
        info.add(s_category);



        Bundle extras = sbn.getNotification().extras;
        if(extras==null){
            Log.d("extras", "extras is null");
        }

        CharSequence title = extras.getString(Notification.EXTRA_TITLE);
        String notificationTitle=" ";
        if(title!=null){
            notificationTitle=title.toString();
        }
        info.add(notificationTitle);

        CharSequence text = extras.getCharSequence(Notification.EXTRA_TEXT);
        String notificationText=" ";
        if(text!=null){
            notificationText=text.toString();
        }
        info.add(notificationText);

        CharSequence subtext = extras.getCharSequence(Notification.EXTRA_SUB_TEXT);
        String notificationSubText=" ";
        if(subtext!=null){
            notificationSubText=subtext.toString();
        }
        info.add(notificationSubText);


        String priority = mapPriority(sbn.getNotification().priority);
        info.add(priority);

        String visibility = mapVisibility(sbn.getNotification().visibility);
        info.add(visibility);

        Notification.Action[] action=sbn.getNotification().actions;
        String s_action="";
        if(action!=null){
            for(Notification.Action item : action){
                s_action=s_action+item.title;
                if(!item.equals(action[action.length-1])){
                    s_action=s_action+"#";
                }
            }
        }
        info.add(s_action);

        //Log.d("action",s_action);

        String contentType=mapContentType(sbn.getNotification().audioAttributes.getContentType());
        info.add(contentType);

        String flag=mapFlag(sbn.getNotification().audioAttributes.getFlags());
        //Log.d("audioattribure flag",mapFlag(sbn.getNotification().audioAttributes.getFlags()));
        info.add(flag);

        String usage=mapUsage(sbn.getNotification().audioAttributes.getUsage());
        info.add(usage);

        //Log.d("notificatino flag",Integer.toHexString(sbn.getNotification().flags));

        String clearable=mapBoolean(sbn.isClearable());
        info.add(clearable);

        String onging=mapBoolean(sbn.isOngoing());
        info.add(onging);

        powerMgr = (PowerManager) getSystemService(POWER_SERVICE);
        String interactive=mapInteractive(powerMgr.isInteractive());
        info.add(interactive);

        String current=String.valueOf(getCurrentTimeInMillis());

        info.add(current);

        info.add(getRingerUpdate());
        Log.d("ringermode",getRingerUpdate());




        uploadCsv(info);



    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {

        ArrayList<String> info = new ArrayList<>();


        //Log.d("remove noti",sbn.getPackageName());



        //Log.d("remove time second ",String.valueOf(loghour)+":"+String.valueOf(logmin)+":"+String.valueOf(logsec));


        String package_name = sbn.getPackageName();
        info.add(package_name);

        info.add("1");  // 1 for remove

        //Date date = new Date(sbn.getPostTime());
        String remove_date = DateFormat.format("yyyy-MM-dd HH:mm:ss z", getCurrentTimeInMillis()).toString();
        info.add(remove_date);

        String tag=sbn.getTag();
        String s_tag=" ";
        if(tag!=null){
            s_tag=tag.toString();
        }
        info.add(s_tag);

        CharSequence ticket = sbn.getNotification().tickerText;
        String s_ticket=" ";
        if(ticket!=null){
            s_ticket=ticket.toString();
        }
        info.add(s_ticket);

        long[] vibrate_mode=sbn.getNotification().vibrate;
        String s_vibrate_mode="";
        if(vibrate_mode!=null){
            for(long pattern : vibrate_mode){
                s_vibrate_mode=s_vibrate_mode+String.valueOf(pattern)+" ";
            }
        }
        info.add(s_vibrate_mode);
        //Log.d("vibrate","vibrate mode= "+vibrate_mode+" svibratemode= "+s_vibrate_mode);

        Uri sound_mode=sbn.getNotification().sound;
        String s_sound_mode=" ";
        if(sound_mode!=null){
            s_sound_mode=sound_mode.toString();
        }
        info.add(s_sound_mode);

        String category=sbn.getNotification().category;
        String s_category=" ";
        if(category!=null){
            s_category=category;
        }
        info.add(s_category);



        Bundle extras = sbn.getNotification().extras;
        if(extras==null){
            Log.d("extras", "extras is null");
        }

        CharSequence title = extras.getString(Notification.EXTRA_TITLE);
        String notificationTitle=" ";
        if(title!=null){
            notificationTitle=title.toString();
        }
        info.add(notificationTitle);

        CharSequence text = extras.getCharSequence(Notification.EXTRA_TEXT);
        String notificationText=" ";
        if(text!=null){
            notificationText=text.toString();
        }
        info.add(notificationText);

        CharSequence subtext = extras.getCharSequence(Notification.EXTRA_SUB_TEXT);
        String notificationSubText=" ";
        if(subtext!=null){
            notificationSubText=subtext.toString();
        }
        info.add(notificationSubText);


        String priority = mapPriority(sbn.getNotification().priority);
        info.add(priority);

        String visibility = mapVisibility(sbn.getNotification().visibility);
        info.add(visibility);

        Notification.Action[] action=sbn.getNotification().actions;
        String s_action="";
        if(action!=null){
            for(Notification.Action item : action){
                s_action=s_action+item.title;
                if(!item.equals(action[action.length-1])){
                    s_action=s_action+"#";
                }
            }
        }
        info.add(s_action);

        //Log.d("action",s_action);

        String contentType=mapContentType(sbn.getNotification().audioAttributes.getContentType());
        info.add(contentType);

        String flag=mapFlag(sbn.getNotification().audioAttributes.getFlags());
        //Log.d("audioattribure flag",mapFlag(sbn.getNotification().audioAttributes.getFlags()));
        info.add(flag);

        String usage=mapUsage(sbn.getNotification().audioAttributes.getUsage());
        info.add(usage);

        //Log.d("notificatino flag",Integer.toHexString(sbn.getNotification().flags));

        String clearable=mapBoolean(sbn.isClearable());
        info.add(clearable);

        String onging=mapBoolean(sbn.isOngoing());
        info.add(onging);

        powerMgr = (PowerManager) getSystemService(POWER_SERVICE);
        String interactive=mapInteractive(powerMgr.isInteractive());
        info.add(interactive);

        String current=String.valueOf(getCurrentTimeInMillis());

        info.add(current);
        info.add(getRingerUpdate());
        Log.d("ringermode",getRingerUpdate());





        uploadCsv(info);





    }

    /*public static ArrayList<String> getInfo(){
        return info;
    }*/
    public void uploadCsv( ArrayList<String> info){



        // get info and add to questinnaire list
        //info=NotificationService_2.getInfo();
        questionnaire_list.add(info);

        Log.d("data ","name= "+info.get(0)+"post= "+info.get(1)+"size= "+questionnaire_list.size());

        // able to send questionnaire
        if(questionnaire_list.size()>=10){
            UsageService.StartQuestionnaire=1;
        }

        // change into arr and add it into list then write to csv
        String[] infoArr = new String[info.size()];

        infoArr = info.toArray(infoArr);

        info_list.add(infoArr);

        if(info_list.size()>=10){
            UsageService.checkWriterAlive();
            UsageService.noti_writer.writeAll(info_list);
            try{
                UsageService.noti_writer.flush();
            }catch (IOException e){
                e.printStackTrace();
            }
            info_list.clear();
        }
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

        Log.d("usage","diff is = "+Math.abs( (getCurrentTimeInMillis()-UsageService.nowtime) / (1000*60) ));

        if(Math.abs( (getCurrentTimeInMillis()-UsageService.nowtime) / (1000*60) ) > 1 ){
            Log.d("usage","reopen usage");
            UsageService.writeSystemLog(getCurrentTimeInMillis(),"notification service reopen usage service, usage service stop at = "+UsageService.nowtime);
            Intent usage_service = new Intent(NotificationService_2.this, UsageService.class);
            startService(usage_service);
        }

        //增加上傳
        //info.clear();

        //infoArr=null;

    }

    public String getRingerUpdate() {

        AudioManager mAudioManager=(AudioManager)getSystemService(AUDIO_SERVICE);

        if (mAudioManager.getRingerMode()==AudioManager.RINGER_MODE_NORMAL)
            return "Normal";
        else if (mAudioManager.getRingerMode()==AudioManager.RINGER_MODE_VIBRATE)
            return "Vibrate";
        else if (mAudioManager.getRingerMode()==AudioManager.RINGER_MODE_SILENT)
            return "Silent";
        else
            return "";

    }



    public String mapVisibility(int num_vis){
        if(num_vis==0){
            return "VISIBILITY_PRIVATE";
        }
        if(num_vis==1){
            return "VISIBILITY_PUBLIC";
        }
        if(num_vis==-1){
            return "VISIBILITY_SECRET";
        }
        return " ";
    }

    public String mapContentType(int content){
        if(content==3){
            return "CONTENT_TYPE_MOVIE";
        }
        if(content==2){
            return "CONTENT_TYPE_MUSIC";
        }
        if(content==4){
            return "CONTENT_TYPE_SONIFICATION";
        }
        if(content==1){
            return "CONTENT_TYPE_SPEECH";
        }
        if(content==0){
            return "CONTENT_TYPE_UNKNOWN";
        }
        return " ";
    }

    public String mapFlag(int flag){
        String formatStr="%8x";
        String formatAns = String.format(formatStr, flag);
        String ans="";
        if(formatAns.charAt(5)=='1'){
            ans=ans+"FLAG_LOW_LATENCY#";
        }
        if(formatAns.charAt(6)=='1'){
            ans=ans+"FLAG_HW_AV_SYNC#";
        }
        if(formatAns.charAt(7)=='1'){
            ans=ans+"FLAG_AUDIBILITY_ENFORCED#";
        }
        if(ans!=""){
            return ans.substring(0,ans.length()-1);
        }
        return " ";
    }

    public String mapUsage(int usage){
        if(usage==4){
            return "USAGE_ALARM";
        }
        if(usage==11){
            return "USAGE_ASSISTANCE_ACCESSIBILITY";
        }
        if(usage==12){
            return "USAGE_ASSISTANCE_NAVIGATION_GUIDANCE";
        }
        if(usage==13){
            return "USAGE_ASSISTANCE_SONIFICATION";
        }
        if(usage==14){
            return "USAGE_GAME";
        }
        if(usage==1){
            return "USAGE_MEDIA";
        }
        if(usage==5){
            return "USAGE_NOTIFICATION";
        }
        if(usage==9){
            return "USAGE_NOTIFICATION_COMMUNICATION_DELAYED";
        }
        if(usage==8){
            return "USAGE_NOTIFICATION_COMMUNICATION_INSTANT";
        }
        if(usage==7){
            return "USAGE_NOTIFICATION_COMMUNICATION_REQUEST";
        }
        if(usage==10){
            return "USAGE_NOTIFICATION_EVENT";
        }
        if(usage==6){
            return "USAGE_NOTIFICATION_RINGTONE";
        }
        if(usage==0){
            return "USAGE_UNKNOWN";
        }
        if(usage==2){
            return "USAGE_VOICE_COMMUNICATION";
        }
        if(usage==3){
            return "USAGE_VOICE_COMMUNICATION_SIGNALLING";
        }
        return " ";
    }

    public String mapPriority(int pri){
        if(pri==0){
            return "PRIORITY_DEFAULT";
        }
        if(pri==1){
            return "PRIORITY_HIGH";
        }
        if(pri==-1){
            return "PRIORITY_LOW";
        }
        if(pri==2){
            return "PRIORITY_MAX";
        }
        if(pri==-2){
            return "PRIORITY_MIN";
        }
        return " ";
    }

    public String mapBoolean(boolean out){
        if(out==true){
            return "true";
        }
        return "false";
    }

    public String mapInteractive(boolean now){
        if(now){
            return "Interactive";
        }
        return "Screen off";
    }

    public long getCurrentTimeInMillis(){
        //get timzone
        TimeZone tz = TimeZone.getDefault();
        java.util.Calendar cal = java.util.Calendar.getInstance(tz);
        long t = cal.getTimeInMillis();
        return t;
    }





}
