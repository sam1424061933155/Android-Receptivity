package com.example.sam.test;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by sam on 2016/11/25.
 */

public class DBHelper extends SQLiteOpenHelper {

    // for notification

    public String noti_table = "notification";  //表格名稱

    public String usage_table="usage";

    public String access_table="accessibility";

    public String questionnaire_table="questionnaire";

    // for notification

    public String ROWID = "rowid";
    public String DEVICE = "device_id";
    public String TIME = "timeToSQLite";
    public String DATA = "Data";
    public String CURRENT="Currenttime";

    //for questionnaire
    public String ISGENERATE="isGenerate";
    public String GENERATETIME="GenerateTime";

    public String ISOPEN="isOpen";
    public String OPENTIME="OpenTime";

    public String ISRESPOND="isRespond";
    public String RESPONDTIME="RespondTime";

    public String PAGE="page";




    public static String DATABASE_NAME = "MySQLite.db";  //資料庫名稱

    public static int DATABASE_VERSION = 1;  //資料庫版本

    public static int checkdb=0;

    public DBHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        if(context==null){
            UsageService.writeSystemLog(UsageService.getCurrentTimeInMillis(),"dbhelper context is null");
        }

        //Log.d("db",String.valueOf(DATABASE_VERSION));
        initiateDatabaseManager();
        checkdb=1;



    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("db","oncreate");
        createNotiTable(db);
        createUsageTable(db);
        createAccessTable(db);
        createAnswerTable(db);




    }

    @Override
    public void onUpgrade(SQLiteDatabase db,
                          int oldVersion, int newVersion) {

    }
    public void initiateDatabaseManager() {
        DatabaseManager.initializeInstance(this);
    }

    public void createNotiTable(SQLiteDatabase db){
        Log.d("db","create noti table");


        String cmd = "CREATE TABLE" + " " +
                noti_table + " ( "+
                ROWID + " " + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DEVICE + " TEXT, " +
                TIME + " TEXT NOT NULL, " +
                CURRENT + " TEXT, " +
                DATA + " TEXT " +
                ");" ;

        db.execSQL(cmd);

    }
    public void createUsageTable(SQLiteDatabase db){
        Log.d("db","create usage table");


        String cmd = "CREATE TABLE" + " " +
                usage_table + " ( "+
                ROWID + " " + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DEVICE + " TEXT, " +
                TIME + " TEXT NOT NULL, " +
                DATA + " TEXT " +
                ");" ;

        db.execSQL(cmd);

        /*Cursor cursor = db.rawQuery("SELECT * FROM "+usage_table, null);
        Log.d("tablecol",cursor.getColumnNames().toString());*/

    }
    public void createAccessTable(SQLiteDatabase db){
        Log.d("db","create access table");


        String cmd = "CREATE TABLE" + " " +
                access_table + " ( "+
                ROWID + " " + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DEVICE + " TEXT, " +
                TIME + " TEXT NOT NULL, " +
                DATA + " TEXT " +
                ");" ;

        db.execSQL(cmd);

        /*Cursor cursor = db.rawQuery("SELECT * FROM "+access_table, null);
        Log.d("tablecol",cursor.getColumnNames().toString());*/

    }
    public void createAnswerTable(SQLiteDatabase db){
        Log.d("db","create answer table");


        String cmd = "CREATE TABLE" + " " +
                questionnaire_table + " ( "+
                ROWID + " " + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DEVICE + " TEXT, " +
                TIME + " TEXT NOT NULL, " +
                DATA + " TEXT, " +
                PAGE + " TEXT, " +
                ISGENERATE + " TEXT, " +
                GENERATETIME + " TEXT, " +
                ISOPEN + " TEXT, " +
                OPENTIME + " TEXT, " +
                ISRESPOND + " TEXT, " +
                RESPONDTIME + " TEXT " +
                ");" ;

        db.execSQL(cmd);

    }

}



