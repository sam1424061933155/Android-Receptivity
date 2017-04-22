package com.example.sam.test;

import android.text.format.DateFormat;
import android.util.Log;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by sam on 2016/11/26.
 */

public class Record {

    public String id;
    public String timeToSQLite;
    public String data;



    public Record(){

    }


    public Record(ArrayList<String> item){

        this.id=item.get(0);
        this.timeToSQLite= item.get(1);
        this.data=item.get(2);

    }

    public void setID(String id){
        this.id=id;
    }

    public String getID(){
        return id;
    }

    public String getTimeToSQLite(){
        return timeToSQLite;
    }



}
