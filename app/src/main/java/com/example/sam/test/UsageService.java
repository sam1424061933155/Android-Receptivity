package com.example.sam.test;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v7.app.AlertDialog;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.opencsv.CSVWriter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.SortedMap;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class UsageService extends Service {

    static String noti_name,usage_name,log_name;
    static String access_name;
    String nowDate=" ";
    static int inPrcoess=0;
    String longitude;
    String latitude;
    String accuracy;
    static long nowtime;
    int screenClose=1;
    long lastTimeSend=0;
    float diffTime;
    static int StartQuestionnaire=0;
    boolean interactive=true;
    static AlertDialog dialog=null;
    Handler handle = new Handler();
    ScheduledExecutorService service=null;
    static Intent usage_service=null;
    ActivityRecognition ar;
    LocationManager lm;
    SensorData sd;
    //int isUsage=0;
    public int CurrentHour=-1;



    //arraylist

    ArrayList<String> usage= new ArrayList<>();
    ArrayList<String []> usage_list= new ArrayList<>();
    ArrayList<String []> Noitem_list= new ArrayList<>();   // for csv
    ArrayList<ArrayList<String>> UsageNoitem_list= new ArrayList<>();  //for sqlite
    ArrayList<String> no; //for no usage


    static ArrayList<ArrayList<String>>RecentList = new ArrayList<>();


    //writer
    static CSVWriter noti_writer =null;
    static CSVWriter usage_writer =null;
    static CSVWriter log_writer =null;
    static CSVWriter access_writer=null;


    //path
    static final String PACKAGE_DIRECTORY_PATH="/Android/data/com.example.sam.test/";

    //manager
    AudioManager mAudioManager;
    ConnectivityManager mConnectivityManager;
    UsageStatsManager mUsmManager;
    NotificationManager notificationManager;
    PowerManager powerMgr;
    TelephonyManager mTelephonyManager;
    TelephonyStateListenner mTelephonyStateListener;
    BatteryStatusReceiver mBatteryStatusReceiver;
    ScreenStatusReceiver mScreenStatusReceiver;




    //audio and ringer
    public final String RINGER_MODE_NORMAL = "Normal";
    public final String RINGER_MODE_VIBRATE = "Vibrate";
    public final String RINGER_MODE_SILENT = "Silent";

    public final String MODE_CURRENT = "Current";
    public final String MODE_INVALID = "Invalid";
    public final String MODE_IN_CALL = "InCall";
    public final String MODE_IN_COMMUNICATION = "InCommunicaiton";
    public final String MODE_NORMAL = "Normal";
    public final String MODE_RINGTONE = "Ringtone";

    public String mRingerMode="";
    public String mAudioMode="";
    public String mStreamVolumeMusic="";
    public String mStreamVolumeNotification="";
    public String mStreamVolumeRing="";
    public String mStreamVolumeVoicecall="";
    public String mStreamVolumeSystem="";


    //network connectivity
    public String mNetworkType="";
    public String NETWORK_TYPE_WIFI = "Wifi";
    public String NETWORK_TYPE_MOBILE = "Mobile";
    public boolean mIsNetworkAvailable = false;
    public boolean mIsConnected = false;
    public boolean mIsWifiAvailable = false;
    public boolean mIsMobileAvailable = false;
    public boolean mIsWifiConnected = false;
    public boolean mIsMobileConnected = false;

    //telephony
    public String NetworkOperatorName="";
    public static String CallState="";
    public String SignalType="";
    private static boolean mIsGSM = false;
    public static String LTE_Strength="";
    public static String GSM_Strength="";
    public static String CDMA_Strength="";
    public static String CDMA_Level="";
    public static String General_Signal_Strength="";

    //battery
    public static String BatteryLevel="";
    public static String BatteryPercentage="";
    public static String IsCharging="false";
    public static String ChargingState="";

    //unlock
    public static String Unlock="";



    //header
    String [] noti_header={"package name","post_or_remove","post time","tag","tickerText","vibrate mode","sound mode","category","title","text","subtext"
            ,"priority","visibility","actions","audio content type","audio flags","audio usage","isClearable","isOnging","isInteractive","CurrentTimeInMillis","RingerMode"};

    String [] usage_header={"package name","CurrentTime","CurrentTimeInMillis","LastTimeUsed","LastTimeUsedInMillis","Ringer mode","Audio mode","VolumeMusic","VolumeNotification","VolumeRing","VolumeVoicecall","VolumeSystem"
            ,"NetworkType","IsWifiAvailable","IsWifiConnected","IsMobileAvailable","IsMobileConnected","IsNetworkAvailable","IsConnected","longitude"
            ,"latitude","Accuracy","activity","Accele_x","Accele_y","Accele_z","Gyroscope_x","Gyroscope_y","Gyroscope_z","Gravity_x","Gravity_y","Gravity_z","LinearAcceleration_x","LinearAcceleration_y","LinearAcceleration_z"
            ,"RotationVector_x_sin","RotationVector_y_sin","RotationVector_z_sin","RotationVector_cos","MagneticField_x","MagneticField_y","MagneticField_z","Proximity","AmbientTemperature","Light","Pressure","RelativeHumidity"
            ,"HeartRate","StepCount","StepDetect","NetworkOperatorName","CallState","SignalType","LTE_Strength","GSM_Strength","CDMA_Level","General_Signal_Strength","BatteryLevel","BatteryPercentage","IsCharging","ChargingState"
            ,"Altitude","Direction"};



    // for sqlite
    private  static DBHelper dbhelper = null;
    static SQLiteDatabase db;
    public int send_rowid_noti=-1;
    public int send_rowid_usage=-1;
    public int send_rowid_accessibility=-1;
    public int send_rowid_questionnaire=-1;
    public String noti_key="-1";
    public String usage_key="-1";
    public String accessibility_key="-1";
    public String questionnaire_key="-1";

    public int upload_time=0;

    // for questionnaire
    public static String index_questionnaire;
    public static int usage_start=0;

    public boolean stop_app=false;








    public UsageService() {
        //dbhelper = new DBHelper(this);
        //dbhelper.getWritableDatabase();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public void onCreate() {

        Log.d("usage"," start service on create");
        setTheme(R.style.AppTheme);
        mUsmManager = (UsageStatsManager)getSystemService(USAGE_STATS_SERVICE);
        powerMgr = (PowerManager) getSystemService(POWER_SERVICE);
        ar = new ActivityRecognition(getApplicationContext());
        lm = new LocationManager(getApplicationContext());
        sd = new SensorData(getApplicationContext());
        mTelephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        mTelephonyStateListener = new TelephonyStateListenner(getApplicationContext());
        mBatteryStatusReceiver = new BatteryStatusReceiver();
        IntentFilter batteryStatus_filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(mBatteryStatusReceiver, batteryStatus_filter);

        mScreenStatusReceiver = new ScreenStatusReceiver();
        IntentFilter ScreenStatus_filter = new IntentFilter();
        ScreenStatus_filter.addAction(Intent.ACTION_SCREEN_ON);
        ScreenStatus_filter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(mScreenStatusReceiver, ScreenStatus_filter);




    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        Log.d("usage"," start service on startcommand");
        log_name="Log"+DateFormat.format("yyyy-MM-dd", getCurrentTimeInMillis()).toString()+".csv";

        if(isExternalStorageWritable()){
            try{
                File PackageDirectory = new File(Environment.getExternalStorageDirectory() + PACKAGE_DIRECTORY_PATH);
                //check whether the project diectory exists
                if(!PackageDirectory.exists()){
                    PackageDirectory.mkdir();
                }
                if(log_writer==null)
                    log_writer= new CSVWriter(new FileWriter(Environment.getExternalStorageDirectory()+PACKAGE_DIRECTORY_PATH+ log_name,true));
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
        writeSystemLog(getCurrentTimeInMillis(),"usage service onStartCommand");
        usage_service=intent;
        Log.d("service","before service start");

        if(service!=null){
            service.shutdown();
        }
        service = Executors.newScheduledThreadPool(5);
        service.scheduleAtFixedRate(UsageRunnable,0,5, TimeUnit.SECONDS);
        Toast.makeText(UsageService.this, "收集資料開始", Toast.LENGTH_LONG).show();

        //dbhelper=new DBHelper(this);
        //context=getApplicationContext();
        /*us=getApplicationContext();
        if(us!=null){
            Log.d("us","us is not null");
        }else{
            Log.d("us","us is null");

        }*/







        //return START_STICKY;
        return START_REDELIVER_INTENT;

    }
    @Override
    public void onDestroy() {
        // The service is no longer used and is being destroyed

        Log.d("usage","usage on destroy");
        writeSystemLog(getCurrentTimeInMillis(),"usage service onDestroy");
        stop_app=true;

        checkWriterAlive();
        try{
            if(usage_list.size()!=0){
                usage_writer.writeAll(usage_list);
                usage_writer.flush();
                usage_list.clear();
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                if(usage_writer!=null){
                    usage_writer.close();
                }
                usage_writer=null;
                Log.d("csv","close successful");

            } catch (IOException e) {
                Log.d("csv","close file error");
                e.printStackTrace();
            }
        }

        if(log_writer!=null){
            try{
                log_writer.close();
                log_writer=null;
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            checkLastData("notification");
            checkLastData("usage");
            checkLastData("accessibility");
            checkLastData("questionnaire");
        }


        service=null;
        ar=null;
        if(usage_service!=null){
            stopService(usage_service);
        }

        unregisterReceiver(mBatteryStatusReceiver);
        unregisterReceiver(mScreenStatusReceiver);

        super.onDestroy();
    }

    public void createNewFile(){



        //date different create new file
        //Log.d("date","nowdate="+nowDate+" , " +"format date"+sDateFormat.format(new java.util.Date()) );
        Log.d("date","nowdate= "+nowDate);
        if( !nowDate.equals(DateFormat.format("yyyy-MM-dd", getCurrentTimeInMillis()).toString()) && nowDate!=null) {

            //close previous writer
            if(noti_writer!=null ){
                if(NotificationService_2.info_list.size()!=0){
                    try{
                        noti_writer.writeAll(NotificationService_2.info_list);
                        noti_writer.flush();
                        NotificationService_2.info_list.clear();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
                try {
                    noti_writer.close();
                    noti_writer=null;
                    Log.d("csv","close noti successful");

                } catch (IOException e) {
                    Log.d("csv","close noti error");
                    e.printStackTrace();
                }
            }
            if(usage_writer!=null){
                try {
                    usage_writer.close();
                    usage_writer=null;
                    Log.d("csv","close usage successful");

                } catch (IOException e) {
                    Log.d("csv","close usage error");
                    e.printStackTrace();
                }
            }
            if(log_writer!=null){
                try {
                    log_writer.close();
                    log_writer=null;
                    Log.d("csv","close log successful");

                } catch (IOException e) {
                    Log.d("csv","close log error");
                    e.printStackTrace();
                }
            }
            if(access_writer!=null){
                try {
                    access_writer.close();
                    access_writer=null;
                    Log.d("csv","close access successful");

                } catch (IOException e) {
                    Log.d("csv","close access error");
                    e.printStackTrace();
                }
            }

            nowDate=DateFormat.format("yyyy-MM-dd", getCurrentTimeInMillis()).toString();

            noti_name="Noti"+nowDate+".csv";
            usage_name="Usage"+nowDate+".csv";
            log_name="Log"+nowDate+".csv";
            access_name="Acees"+nowDate+".csv";

            int header_n=0,header_u=0;

            if(fileExist(noti_name)==0){          //file not exist need to write header
                header_n=1;
            }
            if(fileExist(usage_name)==0){
                header_u=1;
            }

            //create csv file
            if(isExternalStorageWritable()){
                try{
                    File PackageDirectory = new File(Environment.getExternalStorageDirectory() + PACKAGE_DIRECTORY_PATH);

                    //check whether the project diectory exists
                    if(!PackageDirectory.exists()){
                        PackageDirectory.mkdir();
                    }
                    noti_writer = new CSVWriter(new FileWriter(Environment.getExternalStorageDirectory()+PACKAGE_DIRECTORY_PATH+ noti_name,true));
                    usage_writer= new CSVWriter(new FileWriter(Environment.getExternalStorageDirectory()+PACKAGE_DIRECTORY_PATH+ usage_name,true));
                    if(log_writer==null)
                        log_writer= new CSVWriter(new FileWriter(Environment.getExternalStorageDirectory()+PACKAGE_DIRECTORY_PATH+ log_name,true));
                    access_writer= new CSVWriter(new FileWriter(Environment.getExternalStorageDirectory()+PACKAGE_DIRECTORY_PATH+ access_name,true));

                    Log.d("csv","create successful");
                    writeSystemLog(getCurrentTimeInMillis(),"usage service create file successful");


                }catch (IOException e) {
                    Log.d("csv","create file error");
                    writeSystemLog(getCurrentTimeInMillis(),"usage service create file error");
                    e.printStackTrace();
                }
            }


            //wirte header


            if(header_n==1){
                noti_writer.writeNext(noti_header);
                try{
                    noti_writer.flush();
                }catch (IOException e){
                    e.printStackTrace();
                }
                Log.d("csv","add noti header");
            }



            if(header_u==1){
                usage_writer.writeNext(usage_header);
                try{
                    usage_writer.flush();
                }catch (IOException e){
                    e.printStackTrace();
                }
                Log.d("csv","add usage header");

            }

        }


    }
    Runnable UsageRunnable = new Runnable() {
        @Override
        public void run() {
            List<UsageStats> appList = null;
            String lastPackage = " ";
            SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();

            nowtime=getCurrentTimeInMillis();
            Log.d("test","nowtime is= "+nowtime);
            //get the application usage statistics
            //Log.d("runnable","inrunnable before createfile");
            diffTime=(float)((nowtime-lastTimeSend)/(60*60*1000.0));   // in hour

            createNewFile();


            if(!getCurrentHour()){

                appList = mUsmManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST,
                        //start time
                        getCurrentTimeInMillis()- 5000,
                        //end time: until now
                        getCurrentTimeInMillis());
                Log.d("usage","applist size "+appList.size());
                Log.d("usage","checkdb "+DBHelper.checkdb);
                if(appList!=null && appList.size()>0 ){
                    //isUsage=1;
                    Log.d("usage","=====start=====");
                    usage_start=1;
                    Log.d("usage","new applist");
                    for(UsageStats usageStats : appList) {
                        mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
                    }


                    String packageName = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
                    String lasttimeused = DateFormat.format("yyyy-MM-dd HH:mm:ss z", mySortedMap.get(mySortedMap.lastKey()).getLastTimeUsed()).toString();
                    //寫入file
                    usage.add(packageName);
                    Log.d("usage","package name= "+packageName);
                    //new
                    String currentTime=DateFormat.format("yyyy-MM-dd HH:mm:ss z", getCurrentTimeInMillis()).toString();
                    usage.add(currentTime);
                    usage.add(String.valueOf(getCurrentTimeInMillis()));
                    usage.add(lasttimeused);
                    usage.add(String.valueOf(mySortedMap.get(mySortedMap.lastKey()).getLastTimeUsed()));

                    getAudioRingerUpdate(usage);
                    getNetworkConnectivityUpdate(usage);
                    usage.add(LocationManager.longitude);
                    usage.add(LocationManager.latitude);
                    usage.add(LocationManager.accuracy);
                    usage.add(ActivityRecognitionService.now_activity);
                    getSensor(usage);
                    getTelephony(usage);
                    getBattery(usage);
                    usage.add(LocationManager.altitude);
                    usage.add(LocationManager.direction);

                    //Log.d("activity","nowactivity= "+ActivityRecognitionService.now_activity);
                    //Log.d("usage","usage pack "+packageName);
                    //Log.d("usage","usage size= "+usage.size());
                    String[] usageArr = new String[usage.size()];
                    //Log.d("usage","usage size "+usage.size());
                    usageArr = usage.toArray(usageArr);
                    usage_list.add(usageArr);
                    //Log.d("usage","usage_list size= "+usage_list.size());
                    insertSQLite("usage",usage);
                    usage.clear();
                    //Log.d("usage","after usage clear");

                    //usageArr=null;


                    //Log.d("usage","usage size "+usage_list.size());

                    checkWriterAlive();
                    usage_writer.writeAll(usage_list);
                    //Log.d("usage","after write to csv");
                    try{
                        usage_writer.flush();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                    usage_list.clear();
                    //Log.d("usage","after usage_list clear");

                    Log.d("close",String.valueOf(screenClose));
                    //diffTime=(float)((nowtime-lastTimeSend)/(60*60*1000.0));   // in hour

                    //Log.d("data","currenttime= "+String.valueOf(nowtime)+" lastsend= "+String.valueOf(lastTimeSend));
                    Log.d("data ","nowtime= "+nowtime+" lastimesend= "+lastTimeSend+" difftime= "+String.valueOf(diffTime));
                    //Log.d("data ","inprocess="+String.valueOf(inPrcoess));

                    if(StartQuestionnaire==1){
                        if(  ((powerMgr.isInteractive()==true && screenClose==1) || (powerMgr.isInteractive() && (lastPackage!=mySortedMap.get(mySortedMap.lastKey()).getPackageName())) ) &&  diffTime>=1.5 && inPrcoess==0 ){
                            writeSystemLog(getCurrentTimeInMillis(),"usage service able to send questionnaire");
                            inPrcoess=1;

                            if(mIsConnected==true){
                                checkLastData("questionnaire");
                            }

                            Log.d("data ","before preprocess");
                            PreprocessData();
                            Log.d("usage","after preprocess");
                            if(RecentList.size()>0){

                                screenClose=0;
                                lastTimeSend=getCurrentTimeInMillis();
                                Log.d("data","lasttimesend= "+lastTimeSend);

                                if(dialog!=null){
                                    dialog.dismiss();
                                }

                                //handle.post(showdialog);
                                handle.postDelayed(showdialog,5000);
                                Log.d("question","send question");
                                writeSystemLog(getCurrentTimeInMillis(),"usage service send questionnaire");

                                /*if(mIsConnected==true){
                                    queryitem("questionnaire");
                                }*/


                                //create questionnaire data in sqlite
                                ArrayList<String> answer= new ArrayList<>();

                                answer.add("NA");
                                insertSQLite("questionnaire",answer);



                            }
                        }

                    }

                    lastPackage=mySortedMap.get(mySortedMap.lastKey()).getPackageName();
                    Log.d("nowPackage",lastPackage);

                    // IF NETWORK CONNECT UPLOAD DATA

                    if(mIsWifiConnected==true){
                        checkLastData("notification");
                        checkLastData("usage");
                        checkLastData("accessibility");
                        //queryitem("questionnaire");
                    }

                }

                interactive=powerMgr.isInteractive();
                if(interactive==false){
                    screenClose=1;
                }
                if(appList.size()==0 && usage_start==1 ){
                    //isUsage=0;
                    Log.d("applist","no item");
                    //ArrayList<String> no = new ArrayList<>();
                    no = new ArrayList<>();
                    String packageName = "InActive";
                    no.add(packageName);
                    long no_time=getCurrentTimeInMillis();
                    String currentTime=DateFormat.format("yyyy-MM-dd HH:mm:ss z", no_time).toString();
                    no.add(currentTime);
                    no.add(String.valueOf(no_time));
                    for(int i=0;i<2;i++){
                        no.add("");
                    }
                    getAudioRingerUpdate(no);
                    getNetworkConnectivityUpdate(no);
                    no.add(LocationManager.longitude);
                    no.add(LocationManager.latitude);
                    no.add(LocationManager.accuracy);
                    no.add(ActivityRecognitionService.now_activity);
                    getSensor(no);
                    getTelephony(no);
                    getBattery(no);
                    no.add(LocationManager.altitude);
                    no.add(LocationManager.direction);


                    String[] usageArr = new String[no.size()];
                    usageArr = no.toArray(usageArr);

                    Noitem_list.add(usageArr);
                    UsageNoitem_list.add(no);

                    if(Noitem_list.size()>20){
                        Log.d("data","no item write");
                        try{
                            checkWriterAlive();
                            usage_writer.writeAll(Noitem_list);
                            usage_writer.flush();
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                        for(int i =0;i<UsageNoitem_list.size();i++){
                            insertSQLite("usage",UsageNoitem_list.get(i));
                        }
                        Noitem_list.clear();
                        UsageNoitem_list.clear();
                    }
                }

                // check network send is alive
                if(mIsConnected==true){
                    checkHour();
                }


            }




        }
    };


    private Runnable showdialog = new Runnable() {

        @Override
        public void run() {


            final int notifyID = 1; // 通知的識別號碼
            Intent intent = new Intent(UsageService.this,questionnaire.class);
            int flags = PendingIntent.FLAG_CANCEL_CURRENT; // ONE_SHOT：PendingIntent只使用一次；CANCEL_CURRENT：PendingIntent執行前會先結束掉之前的；NO_CREATE：沿用先前的PendingIntent，不建立新的PendingIntent；UPDATE_CURRENT：更新先前PendingIntent所帶的額外資料，並繼續沿用
            PendingIntent pendingIntent = PendingIntent.getActivity(UsageService.this, notifyID, intent, flags); // 取得PendingIntent

            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE); // 取得系統的通知服務
            Notification notification = new Notification.Builder(getApplicationContext()).setSmallIcon(R.drawable.survey)
                    .setContentTitle("Questionnaire")
                    .setContentText("please complete the questionnaire")
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setVibrate(new long[] {0,200,800,500})
                    .build(); // 建立通知
            notificationManager.notify(notifyID, notification); // 發送通知

            AlertDialog.Builder builder =new AlertDialog.Builder(UsageService.this);
            builder.setTitle("Mobile Receptivity")
                    .setMessage("請協助填寫問卷")
                    .setPositiveButton("現在填寫", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // 左方按鈕方法
                            notificationManager.cancel(1);
                            Intent q_intent =new Intent(UsageService.this,questionnaire.class);
                            q_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(q_intent);
                        }
                    })
                    .setNegativeButton("稍後填寫", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // 左方按鈕方法
                            dialog.dismiss();
                        }
                    });
            dialog = builder.create();
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
            dialog.show();
            if( (questionnaire.question_a!=null) && (!questionnaire.question_a.isDestroyed()) ){
                questionnaire.question_a.finish();
            }

            startService(usage_service);


        }
    };
    public static void checkWriterAlive(){

        if(noti_writer==null){
            try{
                noti_writer = new CSVWriter(new FileWriter(Environment.getExternalStorageDirectory()+PACKAGE_DIRECTORY_PATH+ noti_name,true));
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        if(usage_writer==null){
            try{
                usage_writer = new CSVWriter(new FileWriter(Environment.getExternalStorageDirectory()+PACKAGE_DIRECTORY_PATH+ usage_name,true));
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        if(log_writer==null){
            try{
                log_writer = new CSVWriter(new FileWriter(Environment.getExternalStorageDirectory()+PACKAGE_DIRECTORY_PATH+ log_name,true));
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        if(access_writer==null){
            try{
                access_writer = new CSVWriter(new FileWriter(Environment.getExternalStorageDirectory()+PACKAGE_DIRECTORY_PATH+ access_name,true));
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public int fileExist(String name){
        File f = new File(Environment.getExternalStorageDirectory()+PACKAGE_DIRECTORY_PATH+ name);
        if(f.exists()){
            Log.d("csv","exist");
            return 1;
        }
        Log.d("csv"," not exist");

        return 0;

    }
    private  boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
    public static long getCurrentTimeInMillis(){
        //get timzone
        TimeZone tz = TimeZone.getDefault();
        java.util.Calendar cal = java.util.Calendar.getInstance(tz);
        long t = cal.getTimeInMillis();
        return t;
    }

    public boolean getCurrentHour(){

        TimeZone tz = TimeZone.getDefault();
        java.util.Calendar cal = java.util.Calendar.getInstance(tz);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        if(hour>=2 && hour<8){
            return true;  // return true
        }else{
            return false;
        }
    }
    public void checkHour(){

        TimeZone tz = TimeZone.getDefault();
        java.util.Calendar cal = java.util.Calendar.getInstance(tz);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        if(hour!=CurrentHour){
            //send is alive
            String time = DateFormat.format("yyyy-MM-dd HH:mm:ss z", getCurrentTimeInMillis()).toString();
            aliveFirebase(Constants.DEVICE_ID,time);
            CurrentHour=hour;
        }
    }

    public void getAudioRingerUpdate(ArrayList now_list) {



        mAudioManager=(AudioManager)getSystemService(AUDIO_SERVICE);

        if (mAudioManager.getRingerMode()==AudioManager.RINGER_MODE_NORMAL)
            mRingerMode = RINGER_MODE_NORMAL;
        else if (mAudioManager.getRingerMode()==AudioManager.RINGER_MODE_VIBRATE)
            mRingerMode = RINGER_MODE_VIBRATE;
        else if (mAudioManager.getRingerMode()==AudioManager.RINGER_MODE_SILENT)
            mRingerMode = RINGER_MODE_SILENT;

        Log.d("ringermode",mRingerMode);


        mStreamVolumeMusic= String.valueOf(mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        mStreamVolumeNotification= String.valueOf(mAudioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION));
        mStreamVolumeRing= String.valueOf(mAudioManager.getStreamVolume(AudioManager.STREAM_RING));
        mStreamVolumeVoicecall = String.valueOf(mAudioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL));
        mStreamVolumeSystem= String.valueOf(mAudioManager.getStreamVolume(AudioManager.STREAM_SYSTEM));

        mAudioMode = getAudioMode(mAudioManager.getMode());


        now_list.add(mRingerMode);
        now_list.add(mAudioMode);
        now_list.add(mStreamVolumeMusic);
        now_list.add(mStreamVolumeNotification);
        now_list.add(mStreamVolumeRing);
        now_list.add(mStreamVolumeVoicecall);
        now_list.add(mStreamVolumeSystem);


        //android 6
        /*if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            mAllAudioDevices = mAudioManager.getDevices(AudioManager.GET_DEVICES_ALL);
        }

        mAudioManager.getStreamVolume(AudioManager.STREAM_ALARM);*/

        /*Log.d("getAudioRingerUpdate", "[test source being requested] volume:  music" + mStreamVolumeMusic
                + " volume: notification: " +  mStreamVolumeNotification
                + " volume: ring " +  mStreamVolumeRing
                + " volume: voicecall: " +  mStreamVolumeVoicecall
                + " volume: voicesystem: " +  mStreamVolumeSystem
                + " mode:  " +  mAudioMode);*/



    }

    public String getAudioMode(int mode) {

        if (mode==AudioManager.MODE_CURRENT)
            return MODE_CURRENT;
        else if (mode==AudioManager.MODE_IN_CALL)
            return MODE_IN_CALL;
        else if (mode==AudioManager.MODE_IN_COMMUNICATION)
            return MODE_IN_COMMUNICATION;
        else if (mode==AudioManager.MODE_INVALID)
            return MODE_INVALID;

        else if (mode==AudioManager.MODE_NORMAL)
            return MODE_NORMAL;

        else if (mode==AudioManager.MODE_RINGTONE)
            return MODE_RINGTONE;
        else
            return " ";
    }

    private void getNetworkConnectivityUpdate(ArrayList now_list) {

        mConnectivityManager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);

        mNetworkType=" ";
        mIsNetworkAvailable = false;
        mIsConnected = false;
        mIsWifiAvailable = false;
        mIsMobileAvailable = false;
        mIsWifiConnected = false;
        mIsMobileConnected = false;

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            Network[] networks = mConnectivityManager.getAllNetworks();

            NetworkInfo activeNetwork;

            for (Network network : networks) {
                activeNetwork = mConnectivityManager.getNetworkInfo(network);

                if (activeNetwork.getType()== ConnectivityManager.TYPE_WIFI){
                    mIsWifiAvailable = activeNetwork.isAvailable();
                    mIsWifiConnected = activeNetwork.isConnected();
                }

                else if (activeNetwork.getType()==ConnectivityManager.TYPE_MOBILE){
                    mIsMobileAvailable = activeNetwork.isAvailable();
                    mIsMobileConnected = activeNetwork.isConnected();
                }

            }

            if (mIsWifiConnected) {
                mNetworkType = NETWORK_TYPE_WIFI;
            }
            else if (mIsMobileConnected) {
                mNetworkType = NETWORK_TYPE_MOBILE;
            }

            mIsNetworkAvailable = mIsWifiAvailable | mIsMobileAvailable;
            mIsConnected = mIsWifiConnected | mIsMobileConnected;


            /*Log.d("NetworkUpdate", "[test save records] connectivity change available? WIFI: available " + mIsWifiAvailable  +
                    "  mIsConnected: " + mIsWifiConnected + " Mobile: available: " + mIsMobileAvailable + " mIs connected: " + mIsMobileConnected
                    +" network type: " + mNetworkType + ",  mIs connected: " + mIsConnected + " mIs network available " + mIsNetworkAvailable);*/


        }

        else{

            Log.d("NetworkUpdate", "[test save records] api under lollipop " );


            if (mConnectivityManager!=null) {

                NetworkInfo activeNetworkWifi = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                NetworkInfo activeNetworkMobile = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

                boolean isWiFi = activeNetworkWifi.getType() == ConnectivityManager.TYPE_WIFI;
                boolean isMobile = activeNetworkMobile.getType() == ConnectivityManager.TYPE_MOBILE;

                Log.d("NetworkUpdate", "[test save records] connectivity change available? " + isWiFi);


                if(activeNetworkWifi !=null) {

                    mIsWifiConnected = activeNetworkWifi != null &&
                            activeNetworkWifi.isConnected();
                    mIsMobileConnected = activeNetworkWifi != null &&
                            activeNetworkMobile.isConnected();

                    mIsConnected = mIsWifiConnected | mIsMobileConnected;

                    mIsWifiAvailable = activeNetworkWifi.isAvailable();
                    mIsMobileAvailable = activeNetworkMobile.isAvailable();

                    mIsNetworkAvailable = mIsWifiAvailable | mIsMobileAvailable;


                    if (mIsWifiConnected) {
                        mNetworkType = NETWORK_TYPE_WIFI;
                    }

                    else if (mIsMobileConnected) {
                        mNetworkType = NETWORK_TYPE_MOBILE;
                    }


                    //assign value
//
                    /*Log.d("NetworkUpdate", "[test save records] connectivity change available? WIFI: available " + mIsWifiAvailable  +
                            "  mIsConnected: " + mIsWifiConnected + " Mobile: available: " + mIsMobileAvailable + " mIs connected: " + mIsMobileConnected
                            +" network type: " + mNetworkType + ",  mIs connected: " + mIsConnected + " mIs network available " + mIsNetworkAvailable);*/

                }
            }

        }


        now_list.add(mNetworkType);
        now_list.add(String.valueOf(mIsWifiAvailable));
        now_list.add(String.valueOf(mIsWifiConnected));
        now_list.add(String.valueOf(mIsMobileAvailable));
        now_list.add(String.valueOf(mIsMobileConnected));
        now_list.add(String.valueOf(mIsNetworkAvailable));
        now_list.add(String.valueOf(mIsConnected));


    }

    public void  getSensor(ArrayList now_list){

        now_list.add(SensorData.mAccele_x);
        now_list.add(SensorData.mAccele_y);
        now_list.add(SensorData.mAccele_z);
        now_list.add(SensorData.mGyroscope_x);
        now_list.add(SensorData.mGyroscope_y);
        now_list.add(SensorData.mGyroscope_z);
        now_list.add(SensorData.mGravity_x);
        now_list.add(SensorData.mGravity_y);
        now_list.add(SensorData.mGravity_z);
        now_list.add(SensorData.mLinearAcceleration_x);
        now_list.add(SensorData.mLinearAcceleration_y);
        now_list.add(SensorData.mLinearAcceleration_z);
        now_list.add(SensorData.mRotationVector_x_sin);
        now_list.add(SensorData.mRotationVector_y_sin);
        now_list.add(SensorData.mRotationVector_z_sin);
        now_list.add(SensorData.mRotationVector_cos);
        now_list.add(SensorData.mMagneticField_x);
        now_list.add(SensorData.mMagneticField_y);
        now_list.add(SensorData.mMagneticField_z);
        now_list.add(SensorData.mProximity);
        now_list.add(SensorData.mAmbientTemperature);
        now_list.add(SensorData.mLight);
        now_list.add(SensorData.mPressure);
        now_list.add(SensorData.mRelativeHumidity);
        now_list.add(SensorData.mHeartRate);
        now_list.add(SensorData.mStepCount);
        now_list.add(SensorData.mStepDetect);

    }

    public void getTelephony(ArrayList now_list){

        mTelephonyManager.listen(mTelephonyStateListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);

        NetworkOperatorName = mTelephonyManager.getNetworkOperatorName();
        //CallState = callStateString(mTelephonyManager.getCallState());
        SignalType =phoneTypeString(mTelephonyManager.getPhoneType());

        now_list.add(NetworkOperatorName);
        now_list.add(CallState);
        now_list.add(SignalType);
        now_list.add(LTE_Strength);
        now_list.add(GSM_Strength);
        now_list.add(CDMA_Level);
        now_list.add(General_Signal_Strength);

    }

    public void getBattery(ArrayList now_list){


        now_list.add(BatteryLevel);
        now_list.add(BatteryPercentage);
        now_list.add(IsCharging);
        now_list.add(ChargingState);


    }

    public static boolean DataFilter(ArrayList<String> item){

        if(item.get(0).equals("com.example.sam.test")){
            Log.d("datafilter","0");
            return false;
        }
        if(item.get(1).equals("1")){            //remove
            Log.d("datafilter","1");
            return false;
        }
        if(item.get(8).equals(" ") || item.get(8).equals("")){          //title is null
            Log.d("datafilter","2");
            return false;
        }
        if(item.get(17).equals("false")){                   // isclearable is false
            Log.d("datafilter","3");
            return false;
        }
        if(item.get(7).equals("sys") || item.get(7).equals("progress") || item.get(7).equals("status") || item.get(7).equals("alarm")){
            Log.d("datafilter","4");
            return false;
        }
        if(item.get(19).equals("Interactive")){             //is interactive
            Log.d("datafilter","5");
            return false;
        }
        if(item.get(21).equals("Silent") || item.get(21).equals("")){
            return false;
        }
        if(!item.get(6).equals(" ")){           //have sound
            return true;
        }
        if(!item.get(21).equals("Silent") && !item.get(21).equals("")){             // no silent
            return true;
        }

        return true;

    }

    public static void PreprocessData(){

        long posttime=0;
        //RecentList =  new ArrayList<>();
        //WaitList = new ArrayList<>();
        RecentList.clear();
        Log.d("data","questionnaire list size "+NotificationService_2.questionnaire_list.size() );
        for(ArrayList<String> record : NotificationService_2.questionnaire_list){
            //Log.d("data","in processdata loop");
            //Log.d("data","record size= "+record.size());
            //Log.d("data","package "+record.get(0)+" time= "+record.get(20)+ " record "+DataFilter(record));
            if(DataFilter(record)){

                //Log.d("data",String.valueOf(record.size()));
                //Log.d("data",record.get(20));
                posttime=Long.parseLong(record.get(20));
                //Log.d("data",String.valueOf(posttime));
                int diff=(int)((getCurrentTimeInMillis()-posttime)/(1000*60));
                //Log.d("data","diff = "+diff);
                if(diff<=30){
                    RecentList.add(record);
                }/*else{
                    WaitList.add(record);
                }*/
            }
        }
        /*if(RecentList.size()<3){
            int need=3-RecentList.size();
            int have=WaitList.size();
            int time=1;
            while( have>0 && need>0){
                RecentList.add(WaitList.get(WaitList.size()-time));
                have--;
                need--;
                time++;
            }
        }*/
        Log.d("data","size of recentlist= "+RecentList.size());
        writeSystemLog(getCurrentTimeInMillis(),"usage service RecentList size = "+RecentList.size());

        if(RecentList.size()!=0){
            //RecentList.clear();
            NotificationService_2.questionnaire_list.clear();
            for(int i=0;i<RecentList.size();i++){
                NotificationService_2.questionnaire_list.add(RecentList.get(i));
            }
        }else{
            NotificationService_2.questionnaire_list.clear();
        }
        //WaitList.clear();
        //RecentList=null;


        /*for(ArrayList<String> recent : RecentList){
            Log.d("data","recent package= "+recent.get(0));
        }*/
        //questionnaire_list.clear();
        inPrcoess=0;
    }


    public static JSONObject toJsonObject(String table,ArrayList<String> item){

        JSONObject content = new JSONObject();
        try{
            if(table.equals("notification")){
                content.put("packageName",item.get(0));
                content.put("put_or_remove",item.get(1));
                content.put("postTime",item.get(2));
                content.put("tag",item.get(3));
                content.put("tickerText",item.get(4));
                content.put("vibrateMode",item.get(5));
                content.put("soundMode",item.get(6));
                content.put("category",item.get(7));
                content.put("title",item.get(8));
                content.put("text",item.get(9));
                content.put("subtext",item.get(10));
                content.put("actions",item.get(11));
                content.put("priority",item.get(12));
                content.put("visibility",item.get(13));
                content.put("audioContentType",item.get(14));
                content.put("audioFlag",item.get(15));
                content.put("AaudioUsage",item.get(16));
                content.put("isClearable",item.get(17));
                content.put("isOngoing",item.get(18));
                content.put("isInteractive",item.get(19));
                content.put("CurrentTimeInMillis",item.get(20));
                content.put("RingerMode",item.get(21));


            }else if(table.equals("usage")){
                content.put("packageName",item.get(0));
                content.put("CurrentTime",item.get(1));
                content.put("CurrentTimeInMillis",item.get(2));
                content.put("lastTimeUSsed",item.get(3));
                content.put("LastTimeUsedInMillis",item.get(4));
                content.put("RingerMode",item.get(5));
                content.put("AudioMode",item.get(6));
                content.put("VolumeMusic",item.get(7));
                content.put("VolumeNotification",item.get(8));
                content.put("VolumeRing",item.get(9));
                content.put("VolumeVoicecall",item.get(10));
                content.put("VolumeSystem",item.get(11));
                content.put("NetworkType",item.get(12));
                content.put("IsWifiAvailable",item.get(13));
                content.put("IsWifiConnected",item.get(14));
                content.put("IsMobileAvailable",item.get(15));
                content.put("IsMobileConnected",item.get(16));
                content.put("IsNetworkAvailable",item.get(17));
                content.put("IsConnected",item.get(18));
                content.put("longitude",item.get(19));
                content.put("latitude",item.get(20));
                content.put("accuracy",item.get(21));
                content.put("activity",item.get(22));
                content.put("Accele_x",item.get(23));
                content.put("Accele_y",item.get(24));
                content.put("Accele_z",item.get(25));
                content.put("Gyroscope_x",item.get(26));
                content.put("Gyroscope_y",item.get(27));
                content.put("Gyroscope_z",item.get(28));
                content.put("Gravity_x",item.get(29));
                content.put("Gravity_y",item.get(30));
                content.put("Gravity_z",item.get(31));
                content.put("LinearAcceleration_x",item.get(32));
                content.put("LinearAcceleration_y",item.get(33));
                content.put("LinearAcceleration_z",item.get(34));
                content.put("RotationVector_x_sin",item.get(35));
                content.put("RotationVector_y_sin",item.get(36));
                content.put("RotationVector_z_sin",item.get(37));
                content.put("RotationVector_cos",item.get(38));
                content.put("MagneticField_x",item.get(39));
                content.put("MagneticField_y",item.get(40));
                content.put("MagneticField_z",item.get(41));
                content.put("Proximity",item.get(42));
                content.put("AmbientTemperature",item.get(43));
                content.put("Light",item.get(44));
                content.put("Pressure",item.get(45));
                content.put("RelativeHumidity",item.get(46));
                content.put("HeartRate",item.get(47));
                content.put("StepCount",item.get(48));
                content.put("StepDetect",item.get(49));
                content.put("NetworkOperatorName",item.get(50));
                content.put("CallState",item.get(51));
                content.put("SignalType",item.get(52));
                content.put("LTE_Strength",item.get(53));
                content.put("GSM_Strength",item.get(54));
                content.put("CDMA_Level",item.get(55));
                content.put("General_Signal_Strength",item.get(56));
                content.put("BatteryLevel",item.get(57));
                content.put("BatteryPercentage",item.get(58));
                content.put("IsCharging",item.get(59));
                content.put("ChargingState",item.get(60));
                content.put("Altitude",item.get(61));
                content.put("Direction",item.get(62));



            }else if(table.equals("questionnaire")){
                content.put("question0",item.get(0));
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return content;
    }


    public static  synchronized void insertSQLite(String table, ArrayList<String> item){
        Log.d("db","size= "+item.size());
        Log.d("db","insertSQLite");

        ContentValues content = new ContentValues();

        content.put("device_id",Constants.DEVICE_ID);

        long time_millis = getCurrentTimeInMillis();
        String time = DateFormat.format("yyyy-MM-dd HH:mm:ss z", time_millis).toString();

        try{
            content.put("timeToSQLite",time);
            if(table.equals("notification")){
                content.put("Currenttime",item.get(20));
                content.put("Data",toJsonObject("notification",item).toString());
            }else if(table.equals("usage")){
                content.put("Data",toJsonObject("usage",item).toString());
            }else if(table.equals("questionnaire")){
                content.put("Data",toJsonObject("questionnaire",item).toString());
                content.put("page","NA");
                content.put("isGenerate",time);
                content.put("GenerateTime",String.valueOf(time_millis));
                content.put("isOpen","NA");
                content.put("OpenTime","NA");
                content.put("isRespond","NA");
                content.put("RespondTime","NA");

            }else if(table.equals("accessibility")){
                content.put("Data",item.get(0));

            }
        }catch (IndexOutOfBoundsException e){
            content=null;
        }


        Log.d("db","after put all");

        writeSystemLog(getCurrentTimeInMillis(),"usage service try to insert into SQLite " + table);

        if(content!=null){
            try{

                SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
                Log.d("db","get writeable db");

                long result =db.insertOrThrow(table,null,content);
                Log.d("db","insertSQLite after insert");

                if(result==-1){
                    Log.d("db","insert error");
                }else{
                    Log.d("db","insert correct");
                }
            }catch (NullPointerException e){
                writeSystemLog(getCurrentTimeInMillis(),"getWritableDatabase NullPointerException");
                Log.d("db","null pointer");
                e.printStackTrace();
                //us.startService(usage_service);
            }finally {
                content.clear();
                DatabaseManager.getInstance().closeDatabase();
            }
            writeSystemLog(getCurrentTimeInMillis(),"usage service insert into SQLite successful " +table);

        }

        if(table.equals("questionnaire")){
            queryid("questionnaire");
        }


    }

    public static  void queryid(String table){

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+table, null);


        int row=cursor.getCount();
        int col=cursor.getColumnCount();
        if(row!=0) {
            cursor.moveToLast();
            index_questionnaire=cursor.getString(0);

        }
        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

    }

    public synchronized void checkLastData(String table){


        final String temp_table=table;


        Log.d("checkdata","table name "+ temp_table);

        if(table.equals("usage")){
            table="context";
        }

        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference(table+"/user/"+Constants.DEVICE_ID);
        Query queryRef = mDatabase.orderByKey().limitToLast(1);
        queryRef.keepSynced(true);
        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public synchronized void onDataChange(DataSnapshot dataSnapshot) {
                SQLiteDatabase db;
                //ArrayList<String> create_record = new ArrayList<>();
                Cursor cursor;

                Log.d("checkdata_new","ondatachange");


                if(dataSnapshot.getValue()!=null){
                    Log.d("checkdata_new","table name = "+temp_table+ " dataSnapshot.getValue not null");

                    db = DatabaseManager.getInstance().openDatabase();
                    cursor = db.rawQuery("SELECT * FROM "+temp_table, null);
                    if(cursor.getCount()>0){
                        Log.d("checkdata_new","table name = "+temp_table+ " number of row in db = "+cursor.getCount());

                        cursor.moveToFirst();
                        try{
                            for(int i=0;i<cursor.getCount();i++){

                                boolean delete=false;
                                boolean upload=false;
                                Log.d("checkdata_new","table name "+temp_table+" stop_app " +stop_app);

                                try{
                                    if(temp_table.equals("notification")){

                                        if((float)((getCurrentTimeInMillis()-Long.parseLong(cursor.getString(3)))/(1000*60*60.0))>0.002 || stop_app){

                                            Log.d("checkdata_new","table name "+temp_table+" snapshot "+dataSnapshot.getValue().toString());

                                            Log.d("checkdata_new","snapshot "+dataSnapshot.getValue().toString().indexOf("currentTime"));
                                            int start_byte=dataSnapshot.getValue().toString().indexOf("currentTime");
                                            Log.d("checkdata_new", "time "+dataSnapshot.getValue().toString().substring(start_byte+12,start_byte+25) );
                                            long firebase_time=Long.parseLong(dataSnapshot.getValue().toString().substring(start_byte+12,start_byte+25));

                                            Log.d("checkdata_new","table name = "+temp_table+" time on phone "+cursor.getString(3)+" last time on firebase "+firebase_time);

                                            if(Long.parseLong(cursor.getString(3))<firebase_time){
                                                delete=true;
                                            }else{
                                                upload=true;
                                            }
                                        }

                                    }else if(temp_table.equals("usage")){

                                        int start_byte =cursor.getString(3).indexOf("CurrentTimeInMillis");

                                        if((float)((getCurrentTimeInMillis()-Long.parseLong(cursor.getString(3).substring(start_byte+22,start_byte+35)))/(1000*60*60.0))>0.002 || stop_app){

                                            Log.d("checkdata_new","snapshot "+dataSnapshot.getValue().toString().indexOf("CurrentTimeInMillis"));
                                            start_byte=dataSnapshot.getValue().toString().indexOf("CurrentTimeInMillis");
                                            Log.d("checkdata_new","time "+dataSnapshot.getValue().toString().substring(start_byte+22,start_byte+35));
                                            long firebase_time=Long.parseLong(dataSnapshot.getValue().toString().substring(start_byte+22,start_byte+35));
                                            start_byte=cursor.getString(3).indexOf("CurrentTimeInMillis");
                                            long phone_time =Long.parseLong(cursor.getString(3).substring(start_byte+22,start_byte+35));

                                            Log.d("checkdata_new","table name = "+temp_table+" time on phone "+phone_time+" last time on firebase "+firebase_time);

                                            if(phone_time<firebase_time){
                                                delete=true;
                                            }else{
                                                upload=true;
                                            }
                                        }

                                    }else if(temp_table.equals("accessibility")){

                                        int start_byte =cursor.getString(3).indexOf("CurrentTimeInMillis");

                                        if((float)((getCurrentTimeInMillis()-Long.parseLong(cursor.getString(3).substring(start_byte+22,start_byte+35)))/(1000*60*60.0))>0.002 || stop_app){

                                            Log.d("checkdata_new","snapshot "+dataSnapshot.getValue().toString().indexOf("CurrentTimeInMillis"));
                                            start_byte=dataSnapshot.getValue().toString().indexOf("CurrentTimeInMillis");
                                            Log.d("checkdata_new","time "+dataSnapshot.getValue().toString().substring(start_byte+22,start_byte+35));
                                            long firebase_time=Long.parseLong(dataSnapshot.getValue().toString().substring(start_byte+22,start_byte+35));
                                            start_byte=cursor.getString(3).indexOf("CurrentTimeInMillis");
                                            long phone_time =Long.parseLong(cursor.getString(3).substring(start_byte+22,start_byte+35));



                                            Log.d("checkdata_new","table name = "+temp_table+" time on phone "+phone_time+" last time on firebase "+firebase_time);

                                            if(phone_time<firebase_time){
                                                delete=true;
                                            }else{
                                                upload=true;
                                            }
                                        }

                                    }else if(temp_table.equals("questionnaire")){

                                        if((float)((getCurrentTimeInMillis()-Long.parseLong(cursor.getString(6)))/(1000*60*60.0))>1.5 || stop_app){
                                            Log.d("checkdata_new","table name = "+temp_table+" time is ready");

                                            Log.d("checkdata_new","table name = "+temp_table+" number of data "+cursor.getColumnCount());

                                            Log.d("checkdata_new","table name "+temp_table+" snapshot "+dataSnapshot.getValue().toString());

                                            Log.d("checkdata_new","snapshot "+dataSnapshot.getValue().toString().indexOf("GenerateTime"));
                                            int Q_start_byte=dataSnapshot.getValue().toString().indexOf("GenerateTime");
                                            Log.d("checkdata_new","table name = "+temp_table+" start "+Q_start_byte);
                                            Log.d("checkdata_new","table name = "+temp_table+" phone time "+cursor.getString(5));

                                            Log.d("checkdata_new","table name = "+temp_table+" phone time "+cursor.getString(6));
                                            Log.d("checkdata_new","table name = "+temp_table+" firebase time "+dataSnapshot.getValue().toString().substring(Q_start_byte+13,Q_start_byte+26));

                                            long firebase_time=Long.parseLong(dataSnapshot.getValue().toString().substring(Q_start_byte+13,Q_start_byte+26));

                                            Log.d("checkdata_new","table name = "+temp_table+" time on phone "+cursor.getString(6)+" last time on firebase "+firebase_time);

                                            if(Long.parseLong(cursor.getString(6))<firebase_time){
                                                delete=true;
                                            }else{
                                                upload=true;
                                            }


                                        }

                                    }

                                }catch (IllegalStateException e){
                                    delete=false;
                                    upload=false;
                                    e.printStackTrace();
                                }catch (NumberFormatException e){
                                    delete=false;
                                    upload=false;
                                }


                                if(delete){
                                    Log.d("checkdata_new","table name = "+temp_table+ "delete "+cursor.getString(0));

                                    db.delete(temp_table,"rowid="+cursor.getString(0),null);
                                }
                                if(upload){

                                    ArrayList<String> create_record = new ArrayList<>();


                                    Log.d("checkdata_new","table name = "+temp_table+ "upload "+cursor.getString(0));

                                    for(int j=1;j<cursor.getColumnCount();j++){
                                        create_record.add(cursor.getString(j));
                                    }
                                    if(temp_table.equals("notification")){

                                        uploadToFireDB("notification",Constants.DEVICE_ID,create_record,cursor.getString(0));

                                    }else if(temp_table.equals("usage")){

                                        uploadToFireDB("context",Constants.DEVICE_ID,create_record,cursor.getString(0));

                                    }else if(temp_table.equals("accessibility")){

                                        uploadToFireDB("accessibility",Constants.DEVICE_ID,create_record,cursor.getString(0));


                                    }else if(temp_table.equals("questionnaire")){
                                        Log.d("checkdata_new","table name = "+temp_table+" record  size"+create_record.size());

                                        Log.d("checkdata_new","table name = "+temp_table+" record "+create_record.toString());
                                        uploadToFireDB("questionnaire", Constants.DEVICE_ID, create_record, cursor.getString(0));

                                    }

                                    create_record.clear();
                                }

                                if(i!=cursor.getCount()-1){
                                    cursor.moveToNext();
                                }
                            }

                        }finally {
                            cursor.close();
                            DatabaseManager.getInstance().closeDatabase();
                        }

                    }else{
                        cursor.close();
                        DatabaseManager.getInstance().closeDatabase();
                    }

                }else{
                    Log.d("checkdata_new","table name = "+temp_table+ "dataSnapshot.getValue is null");

                    db = DatabaseManager.getInstance().openDatabase();
                    cursor = db.rawQuery("SELECT * FROM "+temp_table, null);
                    if(cursor.getCount()>0){
                        cursor.moveToFirst();
                        try{
                            for(int i=0;i<cursor.getCount();i++){

                                ArrayList<String> create_record = new ArrayList<>();

                                Log.d("checkdata_new","table name = "+temp_table+ " upload "+i);

                                for(int j=1;j<cursor.getColumnCount();j++){
                                    create_record.add(cursor.getString(j));
                                }
                                if(temp_table.equals("notification")){

                                    uploadToFireDB("notification",Constants.DEVICE_ID,create_record,cursor.getString(0));

                                }else if(temp_table.equals("usage")){

                                    uploadToFireDB("context",Constants.DEVICE_ID,create_record,cursor.getString(0));

                                }else if(temp_table.equals("accessibility")){

                                    uploadToFireDB("accessibility",Constants.DEVICE_ID,create_record,cursor.getString(0));


                                }else if(temp_table.equals("questionnaire")){

                                    if((float)((getCurrentTimeInMillis()-Long.parseLong(cursor.getString(6)))/(1000*60*60.0))>1.5) {
                                        Log.d("checkdata_new", "table name = " + temp_table + " time is ready");

                                        Log.d("checkdata_new", "table name = " + temp_table + " number of data " + cursor.getColumnCount());

                                        uploadToFireDB("questionnaire", Constants.DEVICE_ID, create_record, cursor.getString(0));

                                    }
                                }

                                create_record.clear();

                                if(i!=cursor.getCount()-1){
                                    cursor.moveToNext();
                                }
                            }

                        }finally {
                            cursor.close();
                            DatabaseManager.getInstance().closeDatabase();
                        }

                    }else{
                        cursor.close();
                        DatabaseManager.getInstance().closeDatabase();
                    }


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public synchronized void queryitem(String table){

        SQLiteDatabase db;
        ArrayList<String> create_record = new ArrayList<>();
        Cursor cursor;
        int start;
        Log.d("db","query item after select");

        Log.d("checkdata","table name "+ table+ " start check");
        //checkLastData(table,Constants.DEVICE_ID);

        Log.d("checkdata","table name "+ table+ " after check");

        db = DatabaseManager.getInstance().openDatabase();
        cursor = db.rawQuery("SELECT * FROM "+table, null);
        Log.d("checkdata","table name "+ table+ " outside delete rowCount= "+cursor.getCount());

        Log.d("db","row= "+cursor.getCount());
        int row=cursor.getCount();
        int col=cursor.getColumnCount();
        //int last=-1;
        int query=0;
        if(row!=0){
            cursor.moveToFirst();
            start=Integer.parseInt(cursor.getString(0));
            if(table.equals("notification")){
                if(start!=send_rowid_noti){
                    query=1;
                }
            }else if(table.equals("usage")){
                if(start!=send_rowid_usage){
                    query=1;
                }
            }else if(table.equals("accessibility")){
                if(start!=send_rowid_accessibility){
                    query=1;
                }
            }else if(table.equals("questionnaire")){
                if(start!=send_rowid_questionnaire){
                    query=1;
                }
            }
            if(query==1){
                try{
                    for(int i=0;i<row;i++){
                        for(int j=1;j<col;j++){
                            create_record.add(cursor.getString(j));

                            //Log.d("db","col = "+j +"value= "+cursor.getString(j));
                        }
                        if(table.equals("notification")){

                            Log.d("db","size of create noti = "+create_record.size());
                            Log.d("usage","before upload to firebase noti ");


                            uploadToFireDB("notification",Constants.DEVICE_ID,create_record,cursor.getString(0));
                            send_rowid_noti=Integer.parseInt(cursor.getString(0));
                            //last=send_rowid_noti;
                            noti_key = create_record.get(2);

                        }else if(table.equals("usage")){
                            Log.d("db","size of create usage = "+create_record.size());
                            Log.d("usage","before upload to firebase usage");


                            uploadToFireDB("context",Constants.DEVICE_ID,create_record,cursor.getString(0));
                            send_rowid_usage=Integer.parseInt(cursor.getString(0));
                            //last=send_rowid_usage;
                            usage_key = create_record.get(2);

                        }else if(table.equals("accessibility")){
                            Log.d("db","size of create accessibility = "+create_record.size());
                            Log.d("usage","before upload to firebase accessibility");

                            uploadToFireDB("accessibility",Constants.DEVICE_ID,create_record,cursor.getString(0));
                            send_rowid_accessibility=Integer.parseInt(cursor.getString(0));
                            //last=send_rowid_accessibility;
                            accessibility_key = create_record.get(2);


                        }else if(table.equals("questionnaire")){
                            Log.d("db","size of create questionnaire = "+create_record.size());
                            Log.d("usage","before upload to firebase questionnaire");

                            uploadToFireDB("questionnaire",Constants.DEVICE_ID,create_record,cursor.getString(0));
                            send_rowid_questionnaire=Integer.parseInt(cursor.getString(0));
                            //last=send_rowid_questionnaire;
                            questionnaire_key = create_record.get(5);

                        }
                        if(i!=row-1){
                            cursor.moveToNext();
                        }
                        create_record.clear();
                    }
                }finally {
                    cursor.close();
                    DatabaseManager.getInstance().closeDatabase();
                    Log.d("checkdata","after upload");

                }
                /*Log.d("checkdata","to delete table "+table);
                Log.d("checkdata","before delete item start= "+start +"last= "+last);
                for(int i=start;i<=last;i++){
                    db.delete(table,"rowid="+i,null);
                }
                DatabaseManager.getInstance().closeDatabase();
                Log.d("checkdata","data after delete");*/

            }
        }else{
            cursor.close();
            DatabaseManager.getInstance().closeDatabase();
        }






    }

    public  void uploadToFireDB(String table,String id, ArrayList<String> item,String rowid){


        FirebaseDatabase database = FirebaseDatabase.getInstance();

        String node = item.get(1)+"|"+rowid;
        //Log.d("id","device id= " +id);
        if(id.equals("")||id.equals("NA")){
            TelephonyManager mngr = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
            id=mngr.getDeviceId();
            Constants.DEVICE_ID = mngr.getDeviceId();
            if(item.get(0).equals("")||item.get(0).equals("NA")){
                item.set(0,id);
            }
        }

        DatabaseReference myRef = database.getReference(table).child("user").child(id).child(node);
        if(table.equals("notification")){
            Log.d("db","before create noti in upload");

            NotiRecord noti_record = new NotiRecord(item);
            Log.d("db","after create noti in upload");

            myRef.setValue(noti_record);
        }else if(table.equals("context")){
            Log.d("db","before create usage in upload");

            Record usage_record = new Record(item);
            myRef.setValue(usage_record);
            Log.d("db","after create usage in upload");

        }else if(table.equals("accessibility")){
            Log.d("db","before create accessibility in upload");

            Record access_record = new Record(item);
            myRef.setValue(access_record);
            Log.d("db","after create accessibility in upload");

        }else if(table.equals("questionnaire")){
            Log.d("db","before create questionnaire in upload");
            QuestionnaireRecord question_record = new QuestionnaireRecord(item);
            myRef.setValue(question_record );
            Log.d("db","after create questionnaire in upload");
        }



        Log.d("db","write to firebase ok");


    }
    public void aliveFirebase(String id,String time){

        if(id.equals("")||id.equals("NA")){
            TelephonyManager mngr = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
            id=mngr.getDeviceId();
        }
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("alive").child("user").child(id).child(time);
        myRef.setValue("Alive " + time);

    }

    public static void writeSystemLog(long time ,String event){

        String eventtime=DateFormat.format("yyyy-MM-dd HH:mm:ss z",time).toString();
        String [] log_data={eventtime,event};
        Log.d("writer","eventime = "+eventtime+"  event= "+event);
        if(log_writer!=null){
            log_writer.writeNext(log_data);
            try{
                log_writer.flush();
            }catch (IOException e){
                e.printStackTrace();
            }
        }else{
            Log.d("writer","log writer is null");
        }

    }


    public String phoneTypeString(int state){

        if(state==TelephonyManager.PHONE_TYPE_NONE){
            return "PHONE_TYPE_NONE";
        }else if(state==TelephonyManager.PHONE_TYPE_GSM){
            return "PHONE_TYPE_GSM";
        }else if(state==TelephonyManager.PHONE_TYPE_CDMA){
            return "PHONE_TYPE_CDMA";
        }else if(state==TelephonyManager.PHONE_TYPE_SIP){
            return "PHONE_TYPE_SIP";
        }
        return "";
    }
    public static void setCallState (int callState) {

        if (callState==TelephonyManager.CALL_STATE_IDLE)
            CallState = "CALL_STATE_IDLE";
        else if (callState==TelephonyManager.CALL_STATE_OFFHOOK)
            CallState = "CALL_STATE_OFFHOOK";
        else if (callState==TelephonyManager.CALL_STATE_RINGING)
            CallState = "CALL_STATE_RINGING";

    }
    public static boolean isGSM() {
        return mIsGSM;
    }

    public static void setGSM(boolean isGSM) {
        mIsGSM = isGSM;
    }
    public static void setLTESignalStrength(int lTESignalStrength) {
        LTE_Strength = String.valueOf(lTESignalStrength);
    }
    public static void setGeneralSignalStrength(int generalSignalStrength) {
        General_Signal_Strength = String.valueOf(generalSignalStrength);
    }
    public static void setGsmSignalStrength(int gsmSignalStrength) {
        GSM_Strength = String.valueOf(gsmSignalStrength);
    }
    public static void setCdmaSignalStrenthLevel(int cdmaSignalStrenthLevel) {
        CDMA_Level = String.valueOf(cdmaSignalStrenthLevel);
    }
    public static void setBatteryLevel(int batteryLevel) {
        BatteryLevel = String.valueOf(batteryLevel);
    }
    public static void setBatteryPercentage(float batteryPercentage) {
        BatteryPercentage = String.valueOf(batteryPercentage);
    }
    public static void setIsCharging(boolean isCharging) {
        IsCharging = String.valueOf(isCharging);
    }
    public static void setBatteryChargingState(String batteryChargingState) {
        ChargingState = batteryChargingState;
    }




}
