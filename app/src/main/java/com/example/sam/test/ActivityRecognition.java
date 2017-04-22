package com.example.sam.test;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by sam on 2016/11/21.
 */

public class ActivityRecognition  implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static  GoogleApiClient mGoogleApiClient;
    private static Context mContext;
    private boolean mRequestingActivityRecognitionUpdates;
    private static long sLatestDetectionTime = -1;

    private static final String LOG_TAG = "ActRcgnManager";

    public ActivityRecognition(Context context) {

        super();

        mContext= context;

        buildGoogleApiClient();

    }
    protected synchronized void buildGoogleApiClient() {

        if (mGoogleApiClient==null){

            mGoogleApiClient =
                    new GoogleApiClient.Builder(mContext)
                            .addApi(com.google.android.gms.location.ActivityRecognition.API)
                            .addConnectionCallbacks(this)
                            .addOnConnectionFailedListener(this)
                            .build();

            mGoogleApiClient.connect();

        }

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        if (connectionResult.hasResolution()) {
            // Log.d(LOG_TAG,"[onConnectionFailed] Conntection to Google Play services is failed");

        } else {
            Log.e(LOG_TAG, "[onConnectionFailed] No Google Play services is available, the error code is "
                    + connectionResult.getErrorCode());
        }

    }

    @Override
    public void onConnected(Bundle connectionHint) {

        Intent intent = new Intent( mContext, ActivityRecognitionService.class );
        PendingIntent pendingIntent = PendingIntent.getService( mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT );
        com.google.android.gms.location.ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates( mGoogleApiClient, 5000, pendingIntent );

    }


    @Override
    public void onConnectionSuspended(int i) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(LOG_TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }


}
