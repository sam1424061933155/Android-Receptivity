package com.example.sam.test;

import java.util.ArrayList;

/**
 * Created by sam on 2016/12/20.
 */

public class NotiRecord {

    public String id;
    public String currentTime;
    public String timeToSQLite;
    public String data;

    public NotiRecord(){

    }


    public NotiRecord(ArrayList<String> item){

        this.id=item.get(0);
        this.timeToSQLite= item.get(1);
        this.currentTime=item.get(2);
        this.data=item.get(3);

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
