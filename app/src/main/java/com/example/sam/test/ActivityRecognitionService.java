package com.example.sam.test;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.util.List;

public class ActivityRecognitionService extends IntentService {

    static String now_activity;


    public ActivityRecognitionService() {
        super("ActivityRecognizedService");
    }

    public ActivityRecognitionService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if(ActivityRecognitionResult.hasResult(intent)) {
            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
            handleDetectedActivities( result.getProbableActivities() );
        }
    }
    private void handleDetectedActivities(List<DetectedActivity> probableActivities) {
        int conf=-1000;
        //Log.d("activity","test activity size= "+probableActivities.size());
        now_activity="";
        for( DetectedActivity activity : probableActivities ) {
            //Log.d("activity","in detectactivity loop");
            if(activity.getConfidence()>conf){
                conf=activity.getConfidence();
                switch( activity.getType() ) {
                    case DetectedActivity.IN_VEHICLE: {
                        now_activity="in_vehicle";
                        //Log.d("activity","test activity is = vehicle"+" confidence= "+activity.getConfidence());
                        break;
                    }
                    case DetectedActivity.ON_BICYCLE: {
                        now_activity="on_bicycle";
                        //Log.d("activity","test activity is = bicycle"+" confidence= "+activity.getConfidence());

                        break;
                    }
                    case DetectedActivity.ON_FOOT: {
                        now_activity="on_foot";
                        //Log.d("activity","test activity is = foot"+" confidence= "+activity.getConfidence());

                        break;
                    }
                    case DetectedActivity.RUNNING: {
                        now_activity="running";
                        //Log.d("activity","test activity is = runnig"+" confidence= "+activity.getConfidence());

                        break;
                    }
                    case DetectedActivity.STILL: {
                        now_activity="still";
                        //Log.d("activity","test activity is = still"+" confidence= "+activity.getConfidence());

                        break;
                    }
                    case DetectedActivity.TILTING: {
                        now_activity="tilting";
                        //Log.d("activity","test activity is = tilting"+" confidence= "+activity.getConfidence());

                        break;
                    }
                    case DetectedActivity.WALKING: {
                        now_activity="walking";
                        //Log.d("activity","test activity is = walking"+" confidence= "+activity.getConfidence());

                        break;
                    }
                    case DetectedActivity.UNKNOWN: {
                        now_activity="unknown";
                        //Log.d("activity","test activity is = unknown"+" confidence= "+activity.getConfidence());

                        break;
                    }
                }
            }
        }
        Log.d("activity","now activity is = "+now_activity+" confidence= "+conf);

    }


}
