package com.example.sam.test;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.Math.abs;

public class questionnaire extends AppCompatActivity {

    private ViewPager mViewPager;
    Button finish;
    List<View> viewList;
    TextView Title1,Title2,Title3;
    TextView Time1,Time2,Time3;
    TextView Content1,Content2,Content3;
    EditText edt1_0_new,edt1_2,edt1_6,edt1_7,edt1_8,edt1_10,edt1_11,edt1_12,edt1_14,edt1_15,
             edt2_0_new,edt2_2,edt2_6,edt2_7,edt2_8,edt2_10,edt2_11,edt2_12,edt2_14,edt2_15,
             edt3_0_new,edt3_2,edt3_6,edt3_7,edt3_8,edt3_10,edt3_11,edt3_12,edt3_14,edt3_15;

    View v1,v2,v3,v4;

    String et1_0_new="",ans1_0="",ans1_1="",ans1_2="",et1_2="",ans1_3="",ans1_4="",ans1_5="",ans1_6="",et1_6="",ans1_7="",et1_7="",ans1_8="",et1_8="",ans1_9="",ans1_10="",et1_10="",ans1_11="",et1_11="",ans1_12="",et1_12="",ans1_13="",ans1_14="",et1_14="",ans1_15="",et1_15="";
    String et2_0_new="",ans2_0="",ans2_1="",ans2_2="",et2_2="",ans2_3="",ans2_4="",ans2_5="",ans2_6="",et2_6="",ans2_7="",et2_7="",ans2_8="",et2_8="",ans2_9="",ans2_10="",et2_10="",ans2_11="",et2_11="",ans2_12="",et2_12="",ans2_13="",ans2_14="",et2_14="",ans2_15="",et2_15="";
    String et3_0_new="",ans3_0="",ans3_1="",ans3_2="",et3_2="",ans3_3="",ans3_4="",ans3_5="",ans3_6="",et3_6="",ans3_7="",et3_7="",ans3_8="",et3_8="",ans3_9="",ans3_10="",et3_10="",ans3_11="",et3_11="",ans3_12="",et3_12="",ans3_13="",ans3_14="",et3_14="",ans3_15="",et3_15="";

    CheckBox cb1_1,cb1_2,cb1_3,cb1_4,cb1_5,cb1_6,cb1_7,cb1_8,cb1_9,cb1_10,cb1_11,cb1_12,cb1_13,cb1_14,cb1_15,cb1_16,cb1_17,
             cb2_1,cb2_2,cb2_3,cb2_4,cb2_5,cb2_6,cb2_7,cb2_8,cb2_9,cb2_10,cb2_11,cb2_12,cb2_13,cb2_14,cb2_15,cb2_16,cb2_17,
             cb3_1,cb3_2,cb3_3,cb3_4,cb3_5,cb3_6,cb3_7,cb3_8,cb3_9,cb3_10,cb3_11,cb3_12,cb3_13,cb3_14,cb3_15,cb3_16,cb3_17;


    int number_page;
    int error1=0,error2=0,error3=0;

    ArrayList<Integer> before = new ArrayList<>();
    private DBHelper dbhelper = null;
    SQLiteDatabase db;
    static Activity question_a;
    public long opentime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        if(UsageService.dialog!=null){
            UsageService.dialog.dismiss();
        }
        LayoutInflater mInflater = getLayoutInflater().from(this);

        v1 = mInflater.inflate(R.layout.page1, null);
        v2 = mInflater.inflate(R.layout.page2,null );
        v3 = mInflater.inflate(R.layout.page3, null );
        v4 = mInflater.inflate(R.layout.finish, null);

        Initialize();
        UsageService.PreprocessData();
        RandomData();
        LoadQuestionnaire();
        Toast.makeText(questionnaire.this, "左右滑動至上下一頁", Toast.LENGTH_LONG).show();
        //dbhelper = new DBHelper(this);
        question_a=this;



        //实例化适配器
        mViewPager.setAdapter(new MyViewPagerAdapter(viewList));
        mViewPager.setCurrentItem(0);


    }
    public void onDestroy() {
        Log.d("test","in ondestroy");


        super.onDestroy();

    }
    public void updateinfo(){

        ContentValues values = new ContentValues();
        /*JSONObject content = new JSONObject();
        try{
            opentime=getCurrentTimeInMillis();
            String format_time = DateFormat.format("yyyy-MM-dd HH:mm:ss z",opentime).toString();
            content.put("OpenTime",format_time);
            content.put("OpenTimeInMillis",String.valueOf(opentime));
        }catch (JSONException e){
            e.printStackTrace();
        }*/
        //values.put("Data",content.toString());

        opentime=getCurrentTimeInMillis();
        String format_time = DateFormat.format("yyyy-MM-dd HH:mm:ss z",opentime).toString();
        values.put("page",number_page);
        values.put("isOpen", format_time);
        values.put("OpenTime", String.valueOf(opentime));


        try{
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

            int row =db.update("questionnaire",values,"rowid="+UsageService.index_questionnaire,null);
            Log.d("db","after add questionnaire to sqlite");

            if(row!=1){
                Log.d("db","update questionnaire info error");
            }else{
                Log.d("db","update questionnaire info correct");
            }
        }finally {
            values.clear();
            DatabaseManager.getInstance().closeDatabase();

        }
    }

    public JSONObject AnswertoJson(){
        Log.d("question_info",UsageService.RecentList.get(before.get(0)).get(2)+"#"+UsageService.RecentList.get(before.get(0)).get(8)+"#"+UsageService.RecentList.get(before.get(0)).get(9));

        JSONObject content = new JSONObject();
        int temp_page=number_page;
        try{
            if(temp_page>0){
                JSONArray arr1 = new JSONArray();
                JSONObject arr1_obj = new JSONObject();
                //String format_time = DateFormat.format("yyyy-MM-dd HH:mm:ss z",opentime).toString();
                arr1_obj.put("id",UsageService.RecentList.get(before.get(0)).get(20));
                arr1_obj.put("info",UsageService.RecentList.get(before.get(0)).get(2)+"#"+UsageService.RecentList.get(before.get(0)).get(8)+"#"+UsageService.RecentList.get(before.get(0)).get(9));
                //arr1_obj.put("OpenTime",format_time);
                //arr1_obj.put("OpenTimeInMillis",String.valueOf(opentime));
                arr1_obj.put("question0_new",et1_0_new);
                arr1_obj.put("question0",ans1_0);
                arr1_obj.put("question1",ans1_1);
                arr1_obj.put("question2",ans1_2);
                arr1_obj.put("other2",et1_2);
                arr1_obj.put("question3",ans1_3);
                arr1_obj.put("question4",ans1_4);
                arr1_obj.put("question5",ans1_5);
                arr1_obj.put("question6",ans1_6);
                arr1_obj.put("other6",et1_6);
                arr1_obj.put("question7",ans1_7);
                arr1_obj.put("other7",et1_7);
                arr1_obj.put("question8",ans1_8);
                arr1_obj.put("other8",et1_8);
                arr1_obj.put("question9",ans1_9);
                arr1_obj.put("question10",ans1_10);
                arr1_obj.put("other10",et1_10);
                arr1_obj.put("question11",ans1_11);
                arr1_obj.put("other11",et1_11);
                arr1_obj.put("question12",ans1_12);
                arr1_obj.put("other12",et1_12);
                arr1_obj.put("question13",ans1_13);
                arr1_obj.put("question14",ans1_14);
                arr1_obj.put("other14",et1_14);
                arr1_obj.put("question15",ans1_15);
                arr1_obj.put("other15",et1_15);
                arr1.put(arr1_obj);
                content.put("questionnaire1",arr1);
                temp_page--;
                if(temp_page>0){
                    JSONArray arr2 = new JSONArray();
                    JSONObject arr2_obj = new JSONObject();
                    arr2_obj.put("id",UsageService.RecentList.get(before.get(1)).get(20));
                    arr2_obj.put("info",UsageService.RecentList.get(before.get(1)).get(2)+"#"+UsageService.RecentList.get(before.get(1)).get(8)+"#"+UsageService.RecentList.get(before.get(1)).get(9));
                    arr2_obj.put("question0_new",et2_0_new);
                    arr2_obj.put("question0",ans2_0);
                    arr2_obj.put("question1",ans2_1);
                    arr2_obj.put("question2",ans2_2);
                    arr2_obj.put("other2",et2_2);
                    arr2_obj.put("question3",ans2_3);
                    arr2_obj.put("question4",ans2_4);
                    arr2_obj.put("question5",ans2_5);
                    arr2_obj.put("question6",ans2_6);
                    arr2_obj.put("other6",et2_6);
                    arr2_obj.put("question7",ans2_7);
                    arr2_obj.put("other7",et2_7);
                    arr2_obj.put("question8",ans2_8);
                    arr2_obj.put("other8",et2_8);
                    arr2_obj.put("question9",ans2_9);
                    arr2_obj.put("question10",ans2_10);
                    arr2_obj.put("other10",et2_10);
                    arr2_obj.put("question11",ans2_11);
                    arr2_obj.put("other11",et2_11);
                    arr2_obj.put("question12",ans2_12);
                    arr2_obj.put("other12",et2_12);
                    arr2_obj.put("question13",ans2_13);
                    arr2_obj.put("question14",ans2_14);
                    arr2_obj.put("other14",et2_14);
                    arr2_obj.put("question15",ans2_15);
                    arr2_obj.put("other15",et2_15);
                    arr2.put(arr2_obj);
                    content.put("questionnaire2",arr2);
                    temp_page--;
                    if(temp_page>0) {
                        JSONArray arr3 = new JSONArray();
                        JSONObject arr3_obj = new JSONObject();
                        arr3_obj.put("id",UsageService.RecentList.get(before.get(2)).get(20));
                        arr3_obj.put("info",UsageService.RecentList.get(before.get(2)).get(2)+"#"+UsageService.RecentList.get(before.get(2)).get(8)+"#"+UsageService.RecentList.get(before.get(2)).get(9));
                        arr3_obj.put("question0_new",et3_0_new);
                        arr3_obj.put("question0", ans3_0);
                        arr3_obj.put("question1", ans3_1);
                        arr3_obj.put("question2", ans3_2);
                        arr3_obj.put("other2", et3_2);
                        arr3_obj.put("question3", ans3_3);
                        arr3_obj.put("question4", ans3_4);
                        arr3_obj.put("question5", ans3_5);
                        arr3_obj.put("question6", ans3_6);
                        arr3_obj.put("other6", et3_6);
                        arr3_obj.put("question7", ans3_7);
                        arr3_obj.put("other7", et3_7);
                        arr3_obj.put("question8", ans3_8);
                        arr3_obj.put("other8", et3_8);
                        arr3_obj.put("question9", ans3_9);
                        arr3_obj.put("question10", ans3_10);
                        arr3_obj.put("other10", et3_10);
                        arr3_obj.put("question11", ans3_11);
                        arr3_obj.put("other11", et3_11);
                        arr3_obj.put("question12", ans3_12);
                        arr3_obj.put("other12", et3_12);
                        arr3_obj.put("question13", ans3_13);
                        arr3_obj.put("question14", ans3_14);
                        arr3_obj.put("other14", et3_14);
                        arr3_obj.put("question15", ans3_15);
                        arr3_obj.put("other15", et3_15);
                        arr3.put(arr3_obj);
                        content.put("questionnaire3", arr3);
                        temp_page--;
                    }
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return content;
    }

    public void Initialize(){


        Time1=(TextView)v1.findViewById(R.id.Time1);
        Time2=(TextView)v2.findViewById(R.id.Time1);
        Time3=(TextView)v3.findViewById(R.id.Time1);

        Title1=(TextView)v1.findViewById(R.id.Title1);
        Content1=(TextView)v1.findViewById(R.id.Content1);

        Title2=(TextView)v2.findViewById(R.id.Title1);
        Content2=(TextView)v2.findViewById(R.id.Content1);

        Title3=(TextView)v3.findViewById(R.id.Title1);
        Content3=(TextView)v3.findViewById(R.id.Content1);

        finish=(Button)v4.findViewById(R.id.finish);

        finish.setOnClickListener(new Button.OnClickListener(){

            @Override

            public void onClick(View v) {

                // TODO Auto-generated method stub

                ans1_7="";ans1_10="";ans1_11="";
                ans2_7="";ans2_10="";ans2_11="";
                ans3_7="";ans3_10="";ans3_11="";
                //processradio();
                processCheckbox();
                processEdittext();
                //processSeekbar();
                updateData();
                NotificationService_2.questionnaire_list.clear();
                UsageService.RecentList.clear();

                //Log.d("answer","ans = "+ans1_9);

                Log.d("answer "+et1_0_new+",",ans1_0+","+ans1_1+","+ans1_2+","+et1_2+","+ans1_3+","+ans1_4+","+ans1_5+","+ans1_6+","+et1_6+","+ans1_7+","
                        +et1_7+","+ans1_8+","+et1_8+","+ans1_9+","+ans1_10+","+et1_10+","+ans1_11+","+et1_11+","+ans1_12+","+et1_12+","+ans1_13+","+ans1_14+","+et1_14+","+ans1_15+","+et1_15);
                Log.d("answer "+et2_0_new+",",ans2_0+","+ans2_1+","+ans2_2+","+et2_2+","+ans2_3+","+ans2_4+","+ans2_5+","+ans2_6+","+et2_6+","+ans2_7+","
                        +et2_7+","+ans2_8+","+et2_8+","+ans2_9+","+ans2_10+","+et2_10+","+ans2_11+","+et2_11+","+ans2_12+","+et2_12+","+ans2_13+","+ans2_14+","+et2_14+","+ans2_15+","+et2_15);
                Log.d("answer "+et3_0_new+",",ans3_0+","+ans3_1+","+ans3_2+","+et3_2+","+ans3_3+","+ans3_4+","+ans3_5+","+ans3_6+","+et3_6+","+ans3_7+","
                        +et3_7+","+ans3_8+","+et3_8+","+ans3_9+","+ans3_10+","+et3_10+","+ans3_11+","+et3_11+","+ans3_12+","+et3_12+","+ans3_13+","+ans3_14+","+et3_14+","+ans3_15+","+et3_15);
                questionnaire.this.finish();
            }
        });


        //checkbox

        cb1_1=(CheckBox)v1.findViewById(R.id.answer7_1);
        cb1_2=(CheckBox)v1.findViewById(R.id.answer7_2);
        cb1_3=(CheckBox)v1.findViewById(R.id.answer7_3);
        cb1_4=(CheckBox)v1.findViewById(R.id.answer7_4);
        cb1_5=(CheckBox)v1.findViewById(R.id.answer7_5);
        cb1_6=(CheckBox)v1.findViewById(R.id.answer10_1);
        cb1_7=(CheckBox)v1.findViewById(R.id.answer10_2);
        cb1_8=(CheckBox)v1.findViewById(R.id.answer10_3);
        cb1_9=(CheckBox)v1.findViewById(R.id.answer10_4);
        cb1_10=(CheckBox)v1.findViewById(R.id.answer10_5);
        cb1_11=(CheckBox)v1.findViewById(R.id.answer10_6);
        cb1_12=(CheckBox)v1.findViewById(R.id.answer11_1);
        cb1_13=(CheckBox)v1.findViewById(R.id.answer11_2);
        cb1_14=(CheckBox)v1.findViewById(R.id.answer11_3);
        cb1_15=(CheckBox)v1.findViewById(R.id.answer11_4);
        cb1_16=(CheckBox)v1.findViewById(R.id.answer11_5);
        cb1_17=(CheckBox)v1.findViewById(R.id.answer11_6);


        cb2_1=(CheckBox)v2.findViewById(R.id.answer2_7_1);
        cb2_2=(CheckBox)v2.findViewById(R.id.answer2_7_2);
        cb2_3=(CheckBox)v2.findViewById(R.id.answer2_7_3);
        cb2_4=(CheckBox)v2.findViewById(R.id.answer2_7_4);
        cb2_5=(CheckBox)v2.findViewById(R.id.answer2_7_5);
        cb2_6=(CheckBox)v2.findViewById(R.id.answer2_10_1);
        cb2_7=(CheckBox)v2.findViewById(R.id.answer2_10_2);
        cb2_8=(CheckBox)v2.findViewById(R.id.answer2_10_3);
        cb2_9=(CheckBox)v2.findViewById(R.id.answer2_10_4);
        cb2_10=(CheckBox)v2.findViewById(R.id.answer2_10_5);
        cb2_11=(CheckBox)v2.findViewById(R.id.answer2_10_6);
        cb2_12=(CheckBox)v2.findViewById(R.id.answer2_11_1);
        cb2_13=(CheckBox)v2.findViewById(R.id.answer2_11_2);
        cb2_14=(CheckBox)v2.findViewById(R.id.answer2_11_3);
        cb2_15=(CheckBox)v2.findViewById(R.id.answer2_11_4);
        cb2_16=(CheckBox)v2.findViewById(R.id.answer2_11_5);
        cb2_17=(CheckBox)v2.findViewById(R.id.answer2_11_6);

        cb3_1=(CheckBox)v3.findViewById(R.id.answer3_7_1);
        cb3_2=(CheckBox)v3.findViewById(R.id.answer3_7_2);
        cb3_3=(CheckBox)v3.findViewById(R.id.answer3_7_3);
        cb3_4=(CheckBox)v3.findViewById(R.id.answer3_7_4);
        cb3_5=(CheckBox)v3.findViewById(R.id.answer3_7_5);
        cb3_6=(CheckBox)v3.findViewById(R.id.answer3_10_1);
        cb3_7=(CheckBox)v3.findViewById(R.id.answer3_10_2);
        cb3_8=(CheckBox)v3.findViewById(R.id.answer3_10_3);
        cb3_9=(CheckBox)v3.findViewById(R.id.answer3_10_4);
        cb3_10=(CheckBox)v3.findViewById(R.id.answer3_10_5);
        cb3_11=(CheckBox)v3.findViewById(R.id.answer3_10_6);
        cb3_12=(CheckBox)v3.findViewById(R.id.answer3_11_1);
        cb3_13=(CheckBox)v3.findViewById(R.id.answer3_11_2);
        cb3_14=(CheckBox)v3.findViewById(R.id.answer3_11_3);
        cb3_15=(CheckBox)v3.findViewById(R.id.answer3_11_4);
        cb3_16=(CheckBox)v3.findViewById(R.id.answer3_11_5);
        cb3_17=(CheckBox)v3.findViewById(R.id.answer3_11_6);


        final RadioGroup r1_0,r1_1,r1_2,r1_3,r1_4,r1_5,r1_6,r1_8,r1_9,r1_12,r1_13,r1_14,r1_15,
                   r2_0,r2_1,r2_2,r2_3,r2_4,r2_5,r2_6,r2_8,r2_9,r2_12,r2_13,r2_14,r2_15,
                   r3_0,r3_1,r3_2,r3_3,r3_4,r3_5,r3_6,r3_8,r3_9,r3_12,r3_13,r3_14,r3_15;

        r1_0=(RadioGroup)v1.findViewById(R.id.radiogroup0);
        r1_1=(RadioGroup)v1.findViewById(R.id.radiogroup1);
        r1_2=(RadioGroup)v1.findViewById(R.id.radiogroup2);
        r1_3=(RadioGroup)v1.findViewById(R.id.radiogroup3);
        r1_4=(RadioGroup)v1.findViewById(R.id.radiogroup4);
        r1_5=(RadioGroup)v1.findViewById(R.id.radiogroup5);
        r1_6=(RadioGroup)v1.findViewById(R.id.radiogroup6);
        r1_8=(RadioGroup)v1.findViewById(R.id.radiogroup8);
        r1_9=(RadioGroup)v1.findViewById(R.id.radiogroup9);
        r1_12=(RadioGroup)v1.findViewById(R.id.radiogroup12);
        r1_13=(RadioGroup)v1.findViewById(R.id.radiogroup13);
        r1_14=(RadioGroup)v1.findViewById(R.id.radiogroup14);
        r1_15=(RadioGroup)v1.findViewById(R.id.radiogroup15);
        r2_0=(RadioGroup)v2.findViewById(R.id.radiogroup2_0);
        r2_1=(RadioGroup)v2.findViewById(R.id.radiogroup2_1);
        r2_2=(RadioGroup)v2.findViewById(R.id.radiogroup2_2);
        r2_3=(RadioGroup)v2.findViewById(R.id.radiogroup2_3);
        r2_4=(RadioGroup)v2.findViewById(R.id.radiogroup2_4);
        r2_5=(RadioGroup)v2.findViewById(R.id.radiogroup2_5);
        r2_6=(RadioGroup)v2.findViewById(R.id.radiogroup2_6);
        r2_8=(RadioGroup)v2.findViewById(R.id.radiogroup2_8);
        r2_9=(RadioGroup)v2.findViewById(R.id.radiogroup2_9);
        r2_12=(RadioGroup)v2.findViewById(R.id.radiogroup2_12);
        r2_13=(RadioGroup)v2.findViewById(R.id.radiogroup2_13);
        r2_14=(RadioGroup)v2.findViewById(R.id.radiogroup2_14);
        r2_15=(RadioGroup)v2.findViewById(R.id.radiogroup2_15);
        r3_0=(RadioGroup)v3.findViewById(R.id.radiogroup3_0);
        r3_1=(RadioGroup)v3.findViewById(R.id.radiogroup3_1);
        r3_2=(RadioGroup)v3.findViewById(R.id.radiogroup3_2);
        r3_3=(RadioGroup)v3.findViewById(R.id.radiogroup3_3);
        r3_4=(RadioGroup)v3.findViewById(R.id.radiogroup3_4);
        r3_5=(RadioGroup)v3.findViewById(R.id.radiogroup3_5);
        r3_6=(RadioGroup)v3.findViewById(R.id.radiogroup3_6);
        r3_8=(RadioGroup)v3.findViewById(R.id.radiogroup3_8);
        r3_9=(RadioGroup)v3.findViewById(R.id.radiogroup3_9);
        r3_12=(RadioGroup)v3.findViewById(R.id.radiogroup3_12);
        r3_13=(RadioGroup)v3.findViewById(R.id.radiogroup3_13);
        r3_14=(RadioGroup)v3.findViewById(R.id.radiogroup3_14);
        r3_15=(RadioGroup)v3.findViewById(R.id.radiogroup3_15);




        r1_0.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                if(checkedId==-1){
                    error1=1;
                }else{
                    ans1_0=String.valueOf(group.indexOfChild((RadioButton) findViewById(checkedId))) ;
                }
                if(ans1_0.equals("0")){
                    StateChange(r1_15,0);
                    StateChange(r1_1,1);
                    StateChange(r1_2,1);
                    StateChange(r1_3,1);
                    StateChange(r1_4,1);
                    StateChange(r1_5,1);
                    StateChange(r1_6,1);
                    StateChange(r1_8,1);
                    StateChange(r1_9,1);
                    StateChange(r1_12,1);
                    StateChange(r1_13,1);
                    StateChange(r1_14,1);
                    cb1_1.setEnabled(true);
                    cb1_2.setEnabled(true);
                    cb1_3.setEnabled(true);
                    cb1_4.setEnabled(true);
                    cb1_5.setEnabled(true);
                    cb1_6.setEnabled(true);
                    cb1_7.setEnabled(true);
                    cb1_8.setEnabled(true);
                    cb1_9.setEnabled(true);
                    cb1_10.setEnabled(true);
                    cb1_11.setEnabled(true);
                    cb1_12.setEnabled(true);
                    cb1_13.setEnabled(true);
                    cb1_14.setEnabled(true);
                    cb1_15.setEnabled(true);
                    cb1_16.setEnabled(true);
                    cb1_17.setEnabled(true);


                }else if(ans1_0.equals("1")){
                    StateChange(r1_15,1);
                    StateChange(r1_1,0);
                    StateChange(r1_2,0);
                    StateChange(r1_3,0);
                    StateChange(r1_4,0);
                    StateChange(r1_5,0);
                    StateChange(r1_6,0);
                    StateChange(r1_8,0);
                    StateChange(r1_9,0);
                    StateChange(r1_12,0);
                    StateChange(r1_13,0);
                    StateChange(r1_14,0);
                    cb1_1.setEnabled(false);
                    cb1_2.setEnabled(false);
                    cb1_3.setEnabled(false);
                    cb1_4.setEnabled(false);
                    cb1_5.setEnabled(false);
                    cb1_6.setEnabled(false);
                    cb1_7.setEnabled(false);
                    cb1_8.setEnabled(false);
                    cb1_9.setEnabled(false);
                    cb1_10.setEnabled(false);
                    cb1_11.setEnabled(false);
                    cb1_12.setEnabled(false);
                    cb1_13.setEnabled(false);
                    cb1_14.setEnabled(false);
                    cb1_15.setEnabled(false);
                    cb1_16.setEnabled(false);
                    cb1_17.setEnabled(false);
                }
            }
        });

        r1_1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                if(checkedId==-1){
                    error1=1;
                }else{
                    ans1_1=String.valueOf(group.indexOfChild((RadioButton) findViewById(checkedId))) ;
                }
            }
        });

        r1_2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                if(checkedId==-1){
                    error1=1;
                }else{
                    ans1_2=String.valueOf(group.indexOfChild((RadioButton) findViewById(checkedId))) ;
                }
            }
        });

        r1_3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                if(checkedId==-1){
                    error1=1;
                }else{
                    ans1_3=String.valueOf(group.indexOfChild((RadioButton) findViewById(checkedId))) ;
                }
                if(ans1_3.equals("1")){
                    StateChange(r1_4,0);
                    StateChange(r1_5,0);
                    StateChange(r1_6,0);
                    StateChange(r1_8,0);
                    StateChange(r1_9,0);
                    StateChange(r1_12,0);
                    StateChange(r1_13,0);
                    StateChange(r1_14,0);
                    StateChange(r1_15,1);
                    cb1_1.setEnabled(false);
                    cb1_2.setEnabled(false);
                    cb1_3.setEnabled(false);
                    cb1_4.setEnabled(false);
                    cb1_5.setEnabled(false);
                    cb1_6.setEnabled(false);
                    cb1_7.setEnabled(false);
                    cb1_8.setEnabled(false);
                    cb1_9.setEnabled(false);
                    cb1_10.setEnabled(false);
                    cb1_11.setEnabled(false);
                    cb1_12.setEnabled(false);
                    cb1_13.setEnabled(false);
                    cb1_14.setEnabled(false);
                    cb1_15.setEnabled(false);
                    cb1_16.setEnabled(false);
                    cb1_17.setEnabled(false);
                }else{
                    StateChange(r1_4,1);
                    StateChange(r1_5,1);
                    StateChange(r1_6,1);
                    StateChange(r1_8,1);
                    StateChange(r1_9,1);
                    StateChange(r1_12,1);
                    StateChange(r1_13,1);
                    StateChange(r1_14,1);
                    StateChange(r1_15,0);
                    cb1_1.setEnabled(true);
                    cb1_2.setEnabled(true);
                    cb1_3.setEnabled(true);
                    cb1_4.setEnabled(true);
                    cb1_5.setEnabled(true);
                    cb1_6.setEnabled(true);
                    cb1_7.setEnabled(true);
                    cb1_8.setEnabled(true);
                    cb1_9.setEnabled(true);
                    cb1_10.setEnabled(true);
                    cb1_11.setEnabled(true);
                    cb1_12.setEnabled(true);
                    cb1_13.setEnabled(true);
                    cb1_14.setEnabled(true);
                    cb1_15.setEnabled(true);
                    cb1_16.setEnabled(true);
                    cb1_17.setEnabled(true);
                }
            }
        });

        r1_4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                if(checkedId==-1){
                    error1=1;
                }else{
                    ans1_4=String.valueOf(group.indexOfChild((RadioButton) findViewById(checkedId))) ;
                }
                if(ans1_4.equals("0")){
                    cb1_6.setEnabled(true);
                    cb1_7.setEnabled(true);
                    cb1_8.setEnabled(true);
                    cb1_9.setEnabled(true);
                    cb1_10.setEnabled(true);
                    cb1_11.setEnabled(true);
                    cb1_12.setEnabled(false);
                    cb1_13.setEnabled(false);
                    cb1_14.setEnabled(false);
                    cb1_15.setEnabled(false);
                    cb1_16.setEnabled(false);
                    cb1_17.setEnabled(false);

                }else if(ans1_4.equals("2")){
                    cb1_6.setEnabled(false);
                    cb1_7.setEnabled(false);
                    cb1_8.setEnabled(false);
                    cb1_9.setEnabled(false);
                    cb1_10.setEnabled(false);
                    cb1_11.setEnabled(false);
                    cb1_12.setEnabled(true);
                    cb1_13.setEnabled(true);
                    cb1_14.setEnabled(true);
                    cb1_15.setEnabled(true);
                    cb1_16.setEnabled(true);
                    cb1_17.setEnabled(true);
                }
            }
        });

        r1_5.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                if(checkedId==-1){
                    error1=1;
                }else{
                    ans1_5=String.valueOf(group.indexOfChild((RadioButton) findViewById(checkedId))) ;
                }
                if(ans1_5.equals("0")){
                    StateChange(r1_6,0);
                    StateChange(r1_8,1);
                    StateChange(r1_9,1);
                    StateChange(r1_12,1);
                    StateChange(r1_15,0);
                    cb1_1.setEnabled(true);
                    cb1_2.setEnabled(true);
                    cb1_3.setEnabled(true);
                    cb1_4.setEnabled(true);
                    cb1_5.setEnabled(true);

                }else if(ans1_5.equals("1")){
                    StateChange(r1_6,0);
                    StateChange(r1_8,1);
                    StateChange(r1_9,1);
                    StateChange(r1_12,1);
                    StateChange(r1_15,0);
                    cb1_1.setEnabled(false);
                    cb1_2.setEnabled(false);
                    cb1_3.setEnabled(false);
                    cb1_4.setEnabled(false);
                    cb1_5.setEnabled(false);

                }else if(ans1_5.equals("2")){
                    StateChange(r1_6,0);
                    StateChange(r1_8,0);
                    StateChange(r1_9,1);
                    StateChange(r1_12,1);
                    StateChange(r1_15,0);
                    cb1_1.setEnabled(true);
                    cb1_2.setEnabled(true);
                    cb1_3.setEnabled(true);
                    cb1_4.setEnabled(true);
                    cb1_5.setEnabled(true);

                }else if(ans1_5.equals("3")){
                    StateChange(r1_6,1);
                    StateChange(r1_8,0);
                    StateChange(r1_9,0);
                    StateChange(r1_12,1);
                    StateChange(r1_15,0);
                    cb1_1.setEnabled(false);
                    cb1_2.setEnabled(false);
                    cb1_3.setEnabled(false);
                    cb1_4.setEnabled(false);
                    cb1_5.setEnabled(false);

                }else if(ans1_5.equals("4")){
                    StateChange(r1_6,0);
                    StateChange(r1_8,0);
                    StateChange(r1_9,0);
                    StateChange(r1_12,0);
                    StateChange(r1_15,1);
                    cb1_1.setEnabled(false);
                    cb1_2.setEnabled(false);
                    cb1_3.setEnabled(false);
                    cb1_4.setEnabled(false);
                    cb1_5.setEnabled(false);

                }

            }
        });

        r1_6.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                if(checkedId==-1){
                    error1=1;
                }else{
                    ans1_6=String.valueOf(group.indexOfChild((RadioButton) findViewById(checkedId))) ;
                }

            }
        });

        r1_8.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                if(checkedId==-1){
                    error1=1;
                }else{
                    ans1_8=String.valueOf(group.indexOfChild((RadioButton) findViewById(checkedId))) ;
                }
            }
        });

        r1_9.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                if(checkedId==-1){
                    error1=1;
                }else{
                    ans1_9=String.valueOf(group.indexOfChild((RadioButton) findViewById(checkedId))) ;
                }
            }
        });

        r1_12.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                if(checkedId==-1){
                    error1=1;
                }else{
                    ans1_12=String.valueOf(group.indexOfChild((RadioButton) findViewById(checkedId))) ;
                }

            }
        });

        r1_13.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                if(checkedId==-1){
                    error1=1;
                }else{
                    ans1_13=String.valueOf(group.indexOfChild((RadioButton) findViewById(checkedId))) ;
                }

            }
        });

        r1_14.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                if(checkedId==-1){
                    error1=1;
                }else{
                    ans1_14=String.valueOf(group.indexOfChild((RadioButton) findViewById(checkedId))) ;
                }
            }
        });

        r1_15.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                if(checkedId==-1){
                    error1=1;
                }else{
                    ans1_15=String.valueOf(group.indexOfChild((RadioButton) findViewById(checkedId))) ;
                }
            }
        });


        r2_0.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                if(checkedId==-1){
                    error1=1;
                }else{
                    ans2_0=String.valueOf(group.indexOfChild((RadioButton) findViewById(checkedId))) ;
                }
                if(ans2_0.equals("0")){
                    StateChange(r2_15,0);
                    StateChange(r2_1,1);
                    StateChange(r2_2,1);
                    StateChange(r2_3,1);
                    StateChange(r2_4,1);
                    StateChange(r2_5,1);
                    StateChange(r2_6,1);
                    StateChange(r2_8,1);
                    StateChange(r2_9,1);
                    StateChange(r2_12,1);
                    StateChange(r2_13,1);
                    StateChange(r2_14,1);
                    cb2_1.setEnabled(true);
                    cb2_2.setEnabled(true);
                    cb2_3.setEnabled(true);
                    cb2_4.setEnabled(true);
                    cb2_5.setEnabled(true);
                    cb2_6.setEnabled(true);
                    cb2_7.setEnabled(true);
                    cb2_8.setEnabled(true);
                    cb2_9.setEnabled(true);
                    cb2_10.setEnabled(true);
                    cb2_11.setEnabled(true);
                    cb2_12.setEnabled(true);
                    cb2_13.setEnabled(true);
                    cb2_14.setEnabled(true);
                    cb2_15.setEnabled(true);
                    cb2_16.setEnabled(true);
                    cb2_17.setEnabled(true);
                }else if(ans2_0.equals("1")){
                    StateChange(r2_15,1);
                    StateChange(r2_1,0);
                    StateChange(r2_2,0);
                    StateChange(r2_3,0);
                    StateChange(r2_4,0);
                    StateChange(r2_5,0);
                    StateChange(r2_6,0);
                    StateChange(r2_8,0);
                    StateChange(r2_9,0);
                    StateChange(r2_12,0);
                    StateChange(r2_13,0);
                    StateChange(r2_14,0);
                    cb2_1.setEnabled(false);
                    cb2_2.setEnabled(false);
                    cb2_3.setEnabled(false);
                    cb2_4.setEnabled(false);
                    cb2_5.setEnabled(false);
                    cb2_6.setEnabled(false);
                    cb2_7.setEnabled(false);
                    cb2_8.setEnabled(false);
                    cb2_9.setEnabled(false);
                    cb2_10.setEnabled(false);
                    cb2_11.setEnabled(false);
                    cb2_12.setEnabled(false);
                    cb2_13.setEnabled(false);
                    cb2_14.setEnabled(false);
                    cb2_15.setEnabled(false);
                    cb2_16.setEnabled(false);
                    cb2_17.setEnabled(false);
                }
            }
        });

        r2_1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                if(checkedId==-1){
                    error2=1;
                }else{
                    ans2_1=String.valueOf(group.indexOfChild((RadioButton) findViewById(checkedId))) ;
                }
            }
        });

        r2_2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                if(checkedId==-1){
                    error2=1;
                }else{
                    ans2_2=String.valueOf(group.indexOfChild((RadioButton) findViewById(checkedId))) ;
                }
            }
        });

        r2_3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                if(checkedId==-1){
                    error2=1;
                }else{
                    ans2_3=String.valueOf(group.indexOfChild((RadioButton) findViewById(checkedId))) ;
                }
                if(ans2_3.equals("1")){
                    StateChange(r2_4,0);
                    StateChange(r2_5,0);
                    StateChange(r2_6,0);
                    StateChange(r2_8,0);
                    StateChange(r2_9,0);
                    StateChange(r2_12,0);
                    StateChange(r2_13,0);
                    StateChange(r2_14,0);
                    StateChange(r2_15,1);
                    cb2_1.setEnabled(false);
                    cb2_2.setEnabled(false);
                    cb2_3.setEnabled(false);
                    cb2_4.setEnabled(false);
                    cb2_5.setEnabled(false);
                    cb2_6.setEnabled(false);
                    cb2_7.setEnabled(false);
                    cb2_8.setEnabled(false);
                    cb2_9.setEnabled(false);
                    cb2_10.setEnabled(false);
                    cb2_11.setEnabled(false);
                    cb2_12.setEnabled(false);
                    cb2_13.setEnabled(false);
                    cb2_14.setEnabled(false);
                    cb2_15.setEnabled(false);
                    cb2_16.setEnabled(false);
                    cb2_17.setEnabled(false);
                }else{
                    StateChange(r2_4,1);
                    StateChange(r2_5,1);
                    StateChange(r2_6,1);
                    StateChange(r2_8,1);
                    StateChange(r2_9,1);
                    StateChange(r2_12,1);
                    StateChange(r2_13,1);
                    StateChange(r2_14,1);
                    StateChange(r2_15,0);
                    cb2_1.setEnabled(true);
                    cb2_2.setEnabled(true);
                    cb2_3.setEnabled(true);
                    cb2_4.setEnabled(true);
                    cb2_5.setEnabled(true);
                    cb2_6.setEnabled(true);
                    cb2_7.setEnabled(true);
                    cb2_8.setEnabled(true);
                    cb2_9.setEnabled(true);
                    cb2_10.setEnabled(true);
                    cb2_11.setEnabled(true);
                    cb2_12.setEnabled(true);
                    cb2_13.setEnabled(true);
                    cb2_14.setEnabled(true);
                    cb2_15.setEnabled(true);
                    cb2_16.setEnabled(true);
                    cb2_17.setEnabled(true);
                }
            }
        });

        r2_4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                if(checkedId==-1){
                    error2=1;
                }else{
                    ans2_4=String.valueOf(group.indexOfChild((RadioButton) findViewById(checkedId))) ;
                }
                if(ans2_4.equals("0")){
                    cb2_6.setEnabled(true);
                    cb2_7.setEnabled(true);
                    cb2_8.setEnabled(true);
                    cb2_9.setEnabled(true);
                    cb2_10.setEnabled(true);
                    cb2_11.setEnabled(true);
                    cb2_12.setEnabled(false);
                    cb2_13.setEnabled(false);
                    cb2_14.setEnabled(false);
                    cb2_15.setEnabled(false);
                    cb2_16.setEnabled(false);
                    cb2_17.setEnabled(false);

                }else if(ans2_4.equals("2")){
                    cb2_6.setEnabled(false);
                    cb2_7.setEnabled(false);
                    cb2_8.setEnabled(false);
                    cb2_9.setEnabled(false);
                    cb2_10.setEnabled(false);
                    cb2_11.setEnabled(false);
                    cb2_12.setEnabled(true);
                    cb2_13.setEnabled(true);
                    cb2_14.setEnabled(true);
                    cb2_15.setEnabled(true);
                    cb2_16.setEnabled(true);
                    cb2_17.setEnabled(true);
                }

            }
        });

        r2_5.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                if(checkedId==-1){
                    error2=1;
                }else{
                    ans2_5=String.valueOf(group.indexOfChild((RadioButton) findViewById(checkedId))) ;
                }
                if(ans2_5.equals("0")){
                    StateChange(r2_6,0);
                    StateChange(r2_8,1);
                    StateChange(r2_9,1);
                    StateChange(r2_12,1);
                    StateChange(r2_15,0);
                    cb2_1.setEnabled(true);
                    cb2_2.setEnabled(true);
                    cb2_3.setEnabled(true);
                    cb2_4.setEnabled(true);
                    cb2_5.setEnabled(true);

                }else if(ans2_5.equals("1")){
                    StateChange(r2_6,0);
                    StateChange(r2_8,1);
                    StateChange(r2_9,1);
                    StateChange(r2_12,1);
                    StateChange(r2_15,0);
                    cb2_1.setEnabled(false);
                    cb2_2.setEnabled(false);
                    cb2_3.setEnabled(false);
                    cb2_4.setEnabled(false);
                    cb2_5.setEnabled(false);

                }else if(ans2_5.equals("2")){
                    StateChange(r2_6,0);
                    StateChange(r2_8,0);
                    StateChange(r2_9,1);
                    StateChange(r2_12,1);
                    StateChange(r2_15,0);
                    cb2_1.setEnabled(true);
                    cb2_2.setEnabled(true);
                    cb2_3.setEnabled(true);
                    cb2_4.setEnabled(true);
                    cb2_5.setEnabled(true);

                }else if(ans2_5.equals("3")){
                    StateChange(r2_6,1);
                    StateChange(r2_8,0);
                    StateChange(r2_9,0);
                    StateChange(r2_12,1);
                    StateChange(r2_15,0);
                    cb2_1.setEnabled(false);
                    cb2_2.setEnabled(false);
                    cb2_3.setEnabled(false);
                    cb2_4.setEnabled(false);
                    cb2_5.setEnabled(false);

                }else if(ans2_5.equals("4")){
                    StateChange(r2_6,0);
                    StateChange(r2_8,0);
                    StateChange(r2_9,0);
                    StateChange(r2_12,0);
                    StateChange(r2_15,1);
                    cb2_1.setEnabled(false);
                    cb2_2.setEnabled(false);
                    cb2_3.setEnabled(false);
                    cb2_4.setEnabled(false);
                    cb2_5.setEnabled(false);

                }


            }
        });

        r2_6.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                if(checkedId==-1){
                    error2=1;
                }else{
                    ans2_6=String.valueOf(group.indexOfChild((RadioButton) findViewById(checkedId))) ;
                }
            }
        });

        r2_8.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                if(checkedId==-1){
                    error2=1;
                }else{
                    ans2_8=String.valueOf(group.indexOfChild((RadioButton) findViewById(checkedId))) ;
                }
            }
        });

        r2_9.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                if(checkedId==-1){
                    error2=1;
                }else{
                    ans2_9=String.valueOf(group.indexOfChild((RadioButton) findViewById(checkedId))) ;
                }
            }
        });

        r2_12.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                if(checkedId==-1){
                    error2=1;
                }else{
                    ans2_12=String.valueOf(group.indexOfChild((RadioButton) findViewById(checkedId))) ;
                }
            }
        });

        r2_13.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                if(checkedId==-1){
                    error2=1;
                }else{
                    ans2_13=String.valueOf(group.indexOfChild((RadioButton) findViewById(checkedId))) ;
                }

            }
        });

        r2_14.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                if(checkedId==-1){
                    error2=1;
                }else{
                    ans2_14=String.valueOf(group.indexOfChild((RadioButton) findViewById(checkedId))) ;
                }
            }
        });

        r2_15.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                if(checkedId==-1){
                    error2=1;
                }else{
                    ans2_15=String.valueOf(group.indexOfChild((RadioButton) findViewById(checkedId))) ;
                }
            }
        });

        r3_0.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                if(checkedId==-1){
                    error1=1;
                }else{
                    ans3_0=String.valueOf(group.indexOfChild((RadioButton) findViewById(checkedId))) ;
                }
                if(ans3_0.equals("0")){
                    StateChange(r3_15,0);
                    StateChange(r3_1,1);
                    StateChange(r3_2,1);
                    StateChange(r3_3,1);
                    StateChange(r3_4,1);
                    StateChange(r3_5,1);
                    StateChange(r3_6,1);
                    StateChange(r3_8,1);
                    StateChange(r3_9,1);
                    StateChange(r3_12,1);
                    StateChange(r3_13,1);
                    StateChange(r3_14,1);
                    cb3_1.setEnabled(true);
                    cb3_2.setEnabled(true);
                    cb3_3.setEnabled(true);
                    cb3_4.setEnabled(true);
                    cb3_5.setEnabled(true);
                    cb3_6.setEnabled(true);
                    cb3_7.setEnabled(true);
                    cb3_8.setEnabled(true);
                    cb3_9.setEnabled(true);
                    cb3_10.setEnabled(true);
                    cb3_11.setEnabled(true);
                    cb3_12.setEnabled(true);
                    cb3_13.setEnabled(true);
                    cb3_14.setEnabled(true);
                    cb3_15.setEnabled(true);
                    cb3_16.setEnabled(true);
                    cb3_17.setEnabled(true);


                }else if(ans3_0.equals("1")){
                    StateChange(r3_15,1);
                    StateChange(r3_1,0);
                    StateChange(r3_2,0);
                    StateChange(r3_3,0);
                    StateChange(r3_4,0);
                    StateChange(r3_5,0);
                    StateChange(r3_6,0);
                    StateChange(r3_8,0);
                    StateChange(r3_9,0);
                    StateChange(r3_12,0);
                    StateChange(r3_13,0);
                    StateChange(r3_14,0);
                    cb3_1.setEnabled(false);
                    cb3_2.setEnabled(false);
                    cb3_3.setEnabled(false);
                    cb3_4.setEnabled(false);
                    cb3_5.setEnabled(false);
                    cb3_6.setEnabled(false);
                    cb3_7.setEnabled(false);
                    cb3_8.setEnabled(false);
                    cb3_9.setEnabled(false);
                    cb3_10.setEnabled(false);
                    cb3_11.setEnabled(false);
                    cb3_12.setEnabled(false);
                    cb3_13.setEnabled(false);
                    cb3_14.setEnabled(false);
                    cb3_15.setEnabled(false);
                    cb3_16.setEnabled(false);
                    cb3_17.setEnabled(false);
                }
            }
        });

        r3_1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                if(checkedId==-1){
                    error3=1;
                }else{
                    ans3_1=String.valueOf(group.indexOfChild((RadioButton) findViewById(checkedId))) ;
                }
            }
        });

        r3_2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                if(checkedId==-1){
                    error3=1;
                }else{
                    ans3_2=String.valueOf(group.indexOfChild((RadioButton) findViewById(checkedId))) ;
                }

            }
        });

        r3_3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                if(checkedId==-1){
                    error3=1;
                }else{
                    ans3_3=String.valueOf(group.indexOfChild((RadioButton) findViewById(checkedId))) ;
                }
                if(ans3_3.equals("1")){
                    StateChange(r3_4,0);
                    StateChange(r3_5,0);
                    StateChange(r3_6,0);
                    StateChange(r3_8,0);
                    StateChange(r3_9,0);
                    StateChange(r3_12,0);
                    StateChange(r3_13,0);
                    StateChange(r3_14,0);
                    StateChange(r3_15,1);
                    cb3_1.setEnabled(false);
                    cb3_2.setEnabled(false);
                    cb3_3.setEnabled(false);
                    cb3_4.setEnabled(false);
                    cb3_5.setEnabled(false);
                    cb3_6.setEnabled(false);
                    cb3_7.setEnabled(false);
                    cb3_8.setEnabled(false);
                    cb3_9.setEnabled(false);
                    cb3_10.setEnabled(false);
                    cb3_11.setEnabled(false);
                    cb3_12.setEnabled(false);
                    cb3_13.setEnabled(false);
                    cb3_14.setEnabled(false);
                    cb3_15.setEnabled(false);
                    cb3_16.setEnabled(false);
                    cb3_17.setEnabled(false);
                }else{
                    StateChange(r3_4,1);
                    StateChange(r3_5,1);
                    StateChange(r3_6,1);
                    StateChange(r3_8,1);
                    StateChange(r3_9,1);
                    StateChange(r3_12,1);
                    StateChange(r3_13,1);
                    StateChange(r3_14,1);
                    StateChange(r3_15,0);
                    cb3_1.setEnabled(true);
                    cb3_2.setEnabled(true);
                    cb3_3.setEnabled(true);
                    cb3_4.setEnabled(true);
                    cb3_5.setEnabled(true);
                    cb3_6.setEnabled(true);
                    cb3_7.setEnabled(true);
                    cb3_8.setEnabled(true);
                    cb3_9.setEnabled(true);
                    cb3_10.setEnabled(true);
                    cb3_11.setEnabled(true);
                    cb3_12.setEnabled(true);
                    cb3_13.setEnabled(true);
                    cb3_14.setEnabled(true);
                    cb3_15.setEnabled(true);
                    cb3_16.setEnabled(true);
                    cb3_17.setEnabled(true);
                }
            }
        });

        r3_4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                if(checkedId==-1){
                    error3=1;
                }else{
                    ans3_4=String.valueOf(group.indexOfChild((RadioButton) findViewById(checkedId))) ;
                }
                if(ans3_4.equals("0")){
                    cb3_6.setEnabled(true);
                    cb3_7.setEnabled(true);
                    cb3_8.setEnabled(true);
                    cb3_9.setEnabled(true);
                    cb3_10.setEnabled(true);
                    cb3_11.setEnabled(true);
                    cb3_12.setEnabled(false);
                    cb3_13.setEnabled(false);
                    cb3_14.setEnabled(false);
                    cb3_15.setEnabled(false);
                    cb3_16.setEnabled(false);
                    cb3_17.setEnabled(false);

                }else if(ans3_4.equals("2")) {
                    cb3_6.setEnabled(false);
                    cb3_7.setEnabled(false);
                    cb3_8.setEnabled(false);
                    cb3_9.setEnabled(false);
                    cb3_10.setEnabled(false);
                    cb3_11.setEnabled(false);
                    cb3_12.setEnabled(true);
                    cb3_13.setEnabled(true);
                    cb3_14.setEnabled(true);
                    cb3_15.setEnabled(true);
                    cb3_16.setEnabled(true);
                    cb3_17.setEnabled(true);
                }
            }
        });

        r3_5.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                if(checkedId==-1){
                    error3=1;
                }else{
                    ans3_5=String.valueOf(group.indexOfChild((RadioButton) findViewById(checkedId))) ;
                }
                if(ans3_5.equals("0")){
                    StateChange(r3_6,0);
                    StateChange(r3_8,1);
                    StateChange(r3_9,1);
                    StateChange(r3_12,1);
                    StateChange(r3_15,0);
                    cb3_1.setEnabled(true);
                    cb3_2.setEnabled(true);
                    cb3_3.setEnabled(true);
                    cb3_4.setEnabled(true);
                    cb3_5.setEnabled(true);

                }else if(ans3_5.equals("1")){
                    StateChange(r3_6,0);
                    StateChange(r3_8,1);
                    StateChange(r3_9,1);
                    StateChange(r3_12,1);
                    StateChange(r3_15,0);
                    cb3_1.setEnabled(false);
                    cb3_2.setEnabled(false);
                    cb3_3.setEnabled(false);
                    cb3_4.setEnabled(false);
                    cb3_5.setEnabled(false);

                }else if(ans3_5.equals("2")){
                    StateChange(r3_6,0);
                    StateChange(r3_8,0);
                    StateChange(r3_9,1);
                    StateChange(r3_12,1);
                    StateChange(r3_15,0);
                    cb3_1.setEnabled(true);
                    cb3_2.setEnabled(true);
                    cb3_3.setEnabled(true);
                    cb3_4.setEnabled(true);
                    cb3_5.setEnabled(true);

                }else if(ans3_5.equals("3")){
                    StateChange(r3_6,1);
                    StateChange(r3_8,0);
                    StateChange(r3_9,0);
                    StateChange(r3_12,1);
                    StateChange(r3_15,0);
                    cb3_1.setEnabled(false);
                    cb3_2.setEnabled(false);
                    cb3_3.setEnabled(false);
                    cb3_4.setEnabled(false);
                    cb3_5.setEnabled(false);

                }else if(ans3_5.equals("4")){
                    StateChange(r3_6,0);
                    StateChange(r3_8,0);
                    StateChange(r3_9,0);
                    StateChange(r3_12,0);
                    StateChange(r3_15,1);
                    cb3_1.setEnabled(false);
                    cb3_2.setEnabled(false);
                    cb3_3.setEnabled(false);
                    cb3_4.setEnabled(false);
                    cb3_5.setEnabled(false);

                }

            }
        });

        r3_6.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                if(checkedId==-1){
                    error3=1;
                }else{
                    ans3_6=String.valueOf(group.indexOfChild((RadioButton) findViewById(checkedId))) ;
                }
            }
        });

        r3_8.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                if(checkedId==-1){
                    error3=1;
                }else{
                    ans3_8=String.valueOf(group.indexOfChild((RadioButton) findViewById(checkedId))) ;
                }
            }
        });

        r3_9.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                if(checkedId==-1){
                    error3=1;
                }else{
                    ans3_9=String.valueOf(group.indexOfChild((RadioButton) findViewById(checkedId))) ;
                }
            }
        });

        r3_12.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                if(checkedId==-1){
                    error3=1;
                }else{
                    ans3_12=String.valueOf(group.indexOfChild((RadioButton) findViewById(checkedId))) ;
                }
            }
        });

        r3_13.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                if(checkedId==-1){
                    error3=1;
                }else{
                    ans3_13=String.valueOf(group.indexOfChild((RadioButton) findViewById(checkedId))) ;
                }

            }
        });

        r3_14.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                if(checkedId==-1){
                    error3=1;
                }else{
                    ans3_14=String.valueOf(group.indexOfChild((RadioButton) findViewById(checkedId))) ;
                }
            }
        });

        r3_15.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                if(checkedId==-1){
                    error3=1;
                }else{
                    ans3_15=String.valueOf(group.indexOfChild((RadioButton) findViewById(checkedId))) ;
                }
            }
        });



        //    edt1_1,edt1_6,edt1_7,edt1_8,edt1_10,edt1_11,edt1_12,edt1_14,edt1_15,
        edt1_0_new =(EditText)v1.findViewById(R.id.editText0_new_1);
        edt1_2=(EditText)v1.findViewById(R.id.editText2_6);
        edt1_6=(EditText)v1.findViewById(R.id.editText6_3);
        edt1_7=(EditText)v1.findViewById(R.id.editText7_5);
        edt1_8=(EditText)v1.findViewById(R.id.editText8_4);
        edt1_10=(EditText)v1.findViewById(R.id.editText10_6);
        edt1_11=(EditText)v1.findViewById(R.id.editText11_6);
        edt1_12=(EditText)v1.findViewById(R.id.editText12_6);
        edt1_14=(EditText)v1.findViewById(R.id.editText14_4);
        edt1_15=(EditText)v1.findViewById(R.id.editText15_5);

        edt2_0_new =(EditText)v2.findViewById(R.id.editText0_new_2);
        edt2_2=(EditText)v2.findViewById(R.id.editText2_2_6);
        edt2_6=(EditText)v2.findViewById(R.id.editText2_6_3);
        edt2_7=(EditText)v2.findViewById(R.id.editText2_7_5);
        edt2_8=(EditText)v2.findViewById(R.id.editText2_8_4);
        edt2_10=(EditText)v2.findViewById(R.id.editText2_10_6);
        edt2_11=(EditText)v2.findViewById(R.id.editText2_11_6);
        edt2_12=(EditText)v2.findViewById(R.id.editText2_12_6);
        edt2_14=(EditText)v2.findViewById(R.id.editText2_14_4);
        edt2_15=(EditText)v2.findViewById(R.id.editText2_15_5);

        edt3_0_new =(EditText)v3.findViewById(R.id.editText0_new_3);
        edt3_2=(EditText)v3.findViewById(R.id.editText3_2_6);
        edt3_6=(EditText)v3.findViewById(R.id.editText3_6_3);
        edt3_7=(EditText)v3.findViewById(R.id.editText3_7_5);
        edt3_8=(EditText)v3.findViewById(R.id.editText3_8_4);
        edt3_10=(EditText)v3.findViewById(R.id.editText3_10_6);
        edt3_11=(EditText)v3.findViewById(R.id.editText3_11_6);
        edt3_12=(EditText)v3.findViewById(R.id.editText3_12_6);
        edt3_14=(EditText)v3.findViewById(R.id.editText3_14_4);
        edt3_15=(EditText)v3.findViewById(R.id.editText3_15_5);


    }
    public void processCheckbox(){
        if(cb1_1.isChecked()){
            ans1_7=ans1_7+"0#";
        }
        if(cb1_2.isChecked()){
            ans1_7=ans1_7+"1#";
        }
        if(cb1_3.isChecked()){
            ans1_7=ans1_7+"2#";
        }
        if(cb1_4.isChecked()){
            ans1_7=ans1_7+"3#";
        }
        if(cb1_5.isChecked()){
            ans1_7=ans1_7+"4#";
        }
        if(cb1_6.isChecked()){
            ans1_10=ans1_10+"0#";
        }
        if(cb1_7.isChecked()){
            ans1_10=ans1_10+"1#";
        }
        if(cb1_8.isChecked()){
            ans1_10=ans1_10+"2#";
        }
        if(cb1_9.isChecked()){
            ans1_10=ans1_10+"3#";
        }
        if(cb1_10.isChecked()){
            ans1_10=ans1_10+"4#";
        }
        if(cb1_11.isChecked()){
            ans1_10=ans1_10+"5#";
        }
        if(cb1_12.isChecked()){
            ans1_11=ans1_11+"0#";
        }
        if(cb1_13.isChecked()){
            ans1_11=ans1_11+"1#";
        }
        if(cb1_14.isChecked()){
            ans1_11=ans1_11+"2#";
        }
        if(cb1_15.isChecked()){
            ans1_11=ans1_11+"3#";
        }
        if(cb1_16.isChecked()){
            ans1_11=ans1_11+"4#";
        }
        if(cb1_17.isChecked()){
            ans1_11=ans1_11+"5#";
        }


        if(cb2_1.isChecked()){
            ans2_7=ans2_7+"0#";
        }
        if(cb2_2.isChecked()){
            ans2_7=ans2_7+"1#";
        }
        if(cb2_3.isChecked()){
            ans2_7=ans2_7+"2#";
        }
        if(cb2_4.isChecked()){
            ans2_7=ans2_7+"3#";
        }
        if(cb2_5.isChecked()){
            ans2_7=ans2_7+"4#";
        }
        if(cb2_6.isChecked()){
            ans2_10=ans2_10+"0#";
        }
        if(cb2_7.isChecked()){
            ans2_10=ans2_10+"1#";
        }
        if(cb2_8.isChecked()){
            ans2_10=ans2_10+"2#";
        }
        if(cb2_9.isChecked()){
            ans2_10=ans2_10+"3#";
        }
        if(cb2_10.isChecked()){
            ans2_10=ans2_10+"4#";
        }
        if(cb2_11.isChecked()){
            ans2_10=ans2_10+"5#";
        }
        if(cb2_12.isChecked()){
            ans2_11=ans2_11+"0#";
        }
        if(cb2_13.isChecked()){
            ans2_11=ans2_11+"1#";
        }
        if(cb2_14.isChecked()){
            ans2_11=ans2_11+"2#";
        }
        if(cb2_15.isChecked()){
            ans2_11=ans2_11+"3#";
        }
        if(cb2_16.isChecked()){
            ans2_11=ans2_11+"4#";
        }
        if(cb2_17.isChecked()){
            ans2_11=ans2_11+"5#";
        }

        if(cb3_1.isChecked()){
            ans3_7=ans3_7+"0#";
        }
        if(cb3_2.isChecked()){
            ans3_7=ans3_7+"1#";
        }
        if(cb3_3.isChecked()){
            ans3_7=ans3_7+"2#";
        }
        if(cb3_4.isChecked()){
            ans3_7=ans3_7+"3#";
        }
        if(cb3_5.isChecked()){
            ans3_7=ans3_7+"4#";
        }
        if(cb3_6.isChecked()){
            ans3_10=ans3_10+"0#";
        }
        if(cb3_7.isChecked()){
            ans3_10=ans3_10+"1#";
        }
        if(cb3_8.isChecked()){
            ans3_10=ans3_10+"2#";
        }
        if(cb3_9.isChecked()){
            ans3_10=ans3_10+"3#";
        }
        if(cb3_10.isChecked()){
            ans3_10=ans3_10+"4#";
        }
        if(cb3_11.isChecked()){
            ans3_10=ans3_10+"5#";
        }
        if(cb3_12.isChecked()){
            ans3_11=ans3_11+"0#";
        }
        if(cb3_13.isChecked()){
            ans3_11=ans3_11+"1#";
        }
        if(cb3_14.isChecked()){
            ans3_11=ans3_11+"2#";
        }
        if(cb3_15.isChecked()){
            ans3_11=ans3_11+"3#";
        }
        if(cb3_16.isChecked()){
            ans3_11=ans3_11+"4#";
        }
        if(cb3_17.isChecked()){
            ans3_11=ans3_11+"5#";
        }



    }
    public void processEdittext(){
        et1_0_new = edt1_0_new.getText().toString();
        et1_2=edt1_2.getText().toString();
        et1_6=edt1_6.getText().toString();
        et1_7=edt1_7.getText().toString();
        et1_8=edt1_8.getText().toString();
        et1_10=edt1_10.getText().toString();
        et1_11=edt1_11.getText().toString();
        et1_12=edt1_12.getText().toString();
        et1_14=edt1_14.getText().toString();
        et1_15=edt1_15.getText().toString();

        //Log.d("edittext","et1_7="+et1_7+",");
        //Log.d("edittext","ans"+et1_7.equals(""));
        //Log.d("edittext","ans"+et1_7.equals(" "));


        et2_0_new = edt2_0_new.getText().toString();
        et2_2=edt2_2.getText().toString();
        et2_6=edt2_6.getText().toString();
        et2_7=edt2_7.getText().toString();
        et2_8=edt2_8.getText().toString();
        et2_10=edt2_10.getText().toString();
        et2_11=edt2_11.getText().toString();
        et2_12=edt2_12.getText().toString();
        et2_14=edt2_14.getText().toString();
        et2_15=edt2_15.getText().toString();

        et3_0_new = edt3_0_new.getText().toString();
        et3_2=edt3_2.getText().toString();
        et3_6=edt3_6.getText().toString();
        et3_7=edt3_7.getText().toString();
        et3_8=edt3_8.getText().toString();
        et3_10=edt3_10.getText().toString();
        et3_11=edt3_11.getText().toString();
        et3_12=edt3_12.getText().toString();
        et3_14=edt3_14.getText().toString();
        et3_15=edt3_15.getText().toString();




    }

    public void StateChange(RadioGroup rp, int state){

        boolean rp_state;

        if(state==1){
            rp_state=true;
        }else{
            rp_state=false;  //make it disable
        }

        for(int i=0;i<rp.getChildCount();i++){
            rp.getChildAt(i).setEnabled(rp_state);
        }

    }





    public long getCurrentTimeInMillis(){
        //get timzone
        TimeZone tz = TimeZone.getDefault();
        java.util.Calendar cal = java.util.Calendar.getInstance(tz);
        long t = cal.getTimeInMillis();
        return t;
    }


    public void updateData(){

        ContentValues values = new ContentValues();

        values.put("Data",AnswertoJson().toString());
        long time_millis = getCurrentTimeInMillis();
        String time = DateFormat.format("yyyy-MM-dd HH:mm:ss z", time_millis).toString();
        values.put("isRespond",time);
        values.put("RespondTime",String.valueOf(time_millis));

        try{

            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            int row =db.update("questionnaire",values,"rowid="+UsageService.index_questionnaire,null);
            Log.d("db","after add questionnaire to sqlite");

            if(row!=1){
                Log.d("db","update questionnaire data error");
            }else{
                Log.d("db","update questionnaire data correct");
            }
        }finally {
            values.clear();
            DatabaseManager.getInstance().closeDatabase();

        }
    }





    public void LoadQuestionnaire(){

        viewList = new ArrayList<View>();

        if(number_page==0){
            AlertDialog.Builder builder =new AlertDialog.Builder(questionnaire.this);
            builder.setTitle("Mobile Receptivity")
                    .setMessage("訊息已過期")
                    .setPositiveButton("了解", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // 左方按鈕方法
                            dialog.dismiss();
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
            questionnaire.this.finish();
            dialog.show();
        }else if(number_page==1){
            Title1.setText("標題："+UsageService.RecentList.get(before.get(0)).get(8));
            Content1.setText("內容："+UsageService.RecentList.get(before.get(0)).get(9));
            Time1.setText("時間："+UsageService.RecentList.get(before.get(0)).get(2));
            viewList.add(v1);
            viewList.add(v4);
            updateinfo();


        }else if(number_page==2){
            Title1.setText("標題："+UsageService.RecentList.get(before.get(0)).get(8));
            Content1.setText("內容："+UsageService.RecentList.get(before.get(0)).get(9));
            Time1.setText("時間："+UsageService.RecentList.get(before.get(0)).get(2));
            Title2.setText("標題："+UsageService.RecentList.get(before.get(1)).get(8));
            Content2.setText("內容："+UsageService.RecentList.get(before.get(1)).get(9));
            Time2.setText("時間："+UsageService.RecentList.get(before.get(1)).get(2));

            viewList.add(v1);
            viewList.add(v2);
            viewList.add(v4);
            updateinfo();


        }else{
            Title1.setText("標題："+UsageService.RecentList.get(before.get(0)).get(8));
            Content1.setText("內容："+UsageService.RecentList.get(before.get(0)).get(9));
            Time1.setText("時間："+UsageService.RecentList.get(before.get(0)).get(2));

            Title2.setText("標題："+UsageService.RecentList.get(before.get(1)).get(8));
            Content2.setText("內容："+UsageService.RecentList.get(before.get(1)).get(9));
            Time2.setText("時間："+UsageService.RecentList.get(before.get(1)).get(2));

            Title3.setText("標題："+UsageService.RecentList.get(before.get(2)).get(8));
            Content3.setText("內容："+UsageService.RecentList.get(before.get(2)).get(9));
            Time3.setText("時間："+UsageService.RecentList.get(before.get(2)).get(2));

            viewList.add(v1);
            viewList.add(v2);
            viewList.add(v3);
            viewList.add(v4);
            updateinfo();
        }




    }

    public void RandomData(){




        before.clear();
        int find;
        ArrayList<Integer> record = new ArrayList<>();
        if(UsageService.RecentList.size()>0){
            while(before.size()!=3 && record.size()!=UsageService.RecentList.size()){  //家判斷是
                find=0;
                int random=(int)(Math.random()* UsageService.RecentList.size());
                if(!record.contains(random)){
                    record.add(random);
                }
                for(int i=0;i<before.size();i++){
                    if(random==before.get(i)){
                        find=1;
                        break;
                    }
                    long diff=abs((Long.parseLong(UsageService.RecentList.get(random).get(20))
                            - Long.parseLong(UsageService.RecentList.get(before.get(i)).get(20)))/(1000*60));
                    if(diff<5){
                        find=1;
                        break;
                    }
                }
                if(find==0){
                    before.add(random);
                }
            }

        }
        number_page=before.size();
        Log.d("number page","page number= "+number_page);

        //Log.d("number_page","package is = "+MainActivity.RecentList.get(before.get(1)));
        //Log.d("number_page","package is = "+MainActivity.RecentList.get(before.get(2)));



    }


    class MyViewPagerAdapter extends PagerAdapter {
        private List<View> mListViews;

        public MyViewPagerAdapter(List<View> mListViews) {
            this.mListViews = mListViews;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object)   {
            container.removeView((View) object);
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = mListViews.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return  mListViews.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0==arg1;
        }
    }




}
