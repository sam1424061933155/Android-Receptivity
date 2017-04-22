package com.example.sam.test;

import java.util.ArrayList;

/**
 * Created by sam on 2016/12/16.
 */

public class QuestionnaireRecord {

    public String user_id;
    public String timeToSQLite;
    public String data;
    public String page;
    public String isGenerate;
    public String GenerateTime;

    public String isOpen;
    public String OpenTime;

    public String isRespond;
    public String RespondTime;

    public QuestionnaireRecord(){



    }


    public QuestionnaireRecord(ArrayList<String> item){

        this.user_id=item.get(0);
        this.timeToSQLite= item.get(1);
        this.data=item.get(2);
        this.page=item.get(3);
        this.isGenerate=item.get(4);
        this.GenerateTime=item.get(5);
        this.isOpen=item.get(6);
        this.OpenTime=item.get(7);
        this.isRespond=item.get(8);
        this.RespondTime=item.get(9);



    }



}
