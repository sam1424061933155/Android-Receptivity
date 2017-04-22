package com.example.sam.test;

import android.content.Context;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by sam on 2016/12/17.
 */

public class LocationManager implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    /** Tag for logging. */
    private static final String LOG_TAG = "LocationManager";


    /**constants**/

    public final long UpdateIntervalInMilliSeconds = 10000;

    public final long  FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 10000;



    //the accuracy of location update
    public static final int LOCATION_UPDATE_LOCATION_PRIORITY = LocationRequest.PRIORITY_HIGH_ACCURACY;



    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    private  LocationRequest mLocationRequest;

    private  Context mContext;
    /**
     * Provides the entry point to Google Play services.
     */
    protected GoogleApiClient mGoogleApiClient;

    /**
     * Represents a geographical location.
     */
    protected  Location mCurrentLocation;

    /**
     * Tracks the status of the location updates request. The value is true when Context Manager
     * requests Minuku to request locaiton
     */
    protected  Boolean mRequestingLocationUpdates = true ;

    protected static String longitude;

    protected static String latitude;

    protected static String accuracy;

    protected static String altitude;

    protected static String direction;





    public LocationManager(Context c){

        super();

        mContext = c;
        // Kick off the process of building a GoogleApiClient and requesting the LocationServices
        // API.
        buildGoogleApiClient();
    }


    /**
     * Request a connection to Location Services. This call returns immediately,
     * but the request is not complete until onConnected() or onConnectionFailure() is called.
     */
    private void connentClient() {
        mGoogleApiClient.connect();
    }


    private void disconnectClient() {
        // Disconnect the client
        Log.d("LOG_TAG","discconect");
        mGoogleApiClient.disconnect();
    }

    /**
     * Builds a GoogleApiClient. Uses the {@code #addApi} method to request the
     * LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        Log.i(LOG_TAG, "Building GoogleApiClient");
        if(mGoogleApiClient==null){
            mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            createLocationRequest();
            connentClient();
        }

    }

    /**
     * Sets up the location request. Android has two location request settings:
     * {@code ACCESS_COARSE_LOCATION} and {@code ACCESS_FINE_LOCATION}. These settings control
     * the accuracy of the current location. This sample uses ACCESS_FINE_LOCATION, as defined in
     * the AndroidManifest.xml.
     * <p/>
     * When the ACCESS_FINE_LOCATION setting is specified, combined with a fast update
     * interval (5 seconds), the Fused Location Provider API returns location updates that are
     * accurate to within a few feet.
     * <p/>
     * These settings are appropriate for mapping applications that show real-time location
     * updates.
     */
    protected void createLocationRequest() {

        mLocationRequest = new LocationRequest();

        //set intervals for the locaiton request
        mLocationRequest.setInterval(UpdateIntervalInMilliSeconds);


        // Sets the fastest rate for active location updates.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        //set accuracy
        mLocationRequest.setPriority(LOCATION_UPDATE_LOCATION_PRIORITY);
    }


    /**
     * Requests location updates from the FusedLocationApi.
     */
    protected void startLocationUpdates() {
        // The final argument to {@code requestLocationUpdates()} is a LocationListener
        if (mGoogleApiClient.isConnected()){
            //Log.d(LOG_TAG, "[testLocationUpdate] send out location udpate request");

            try{
                LocationServices.FusedLocationApi.requestLocationUpdates(
                        mGoogleApiClient, mLocationRequest, this);
            }catch (SecurityException e) {

            }

            //Log.d(LOG_TAG, "[testLocationUpdate] after send out location udpate request");
        }

    }

    /**this function is where we got the updated location information**/
    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;

        Log.d(LOG_TAG, "[onLocationChanged] get location " +
                mCurrentLocation.getLatitude() + " , " +
                mCurrentLocation.getLongitude() + " , " +
                mCurrentLocation.getAccuracy() + ","+
                mCurrentLocation.getProvider());

        longitude = String.valueOf(mCurrentLocation.getLongitude());
        latitude = String.valueOf(mCurrentLocation.getLatitude());
        accuracy = String.valueOf(mCurrentLocation.getAccuracy());
        altitude = String.valueOf(mCurrentLocation.getAltitude());
        direction= String.valueOf(mCurrentLocation.getBearing());
    }



    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(LOG_TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }


    /*
    * Called by Location Services when the request to connect the
    * client finishes successfully. At this point, you can
    * request the current location or start periodic updates
    */
    @Override
    public void onConnected(Bundle dataBundle) {

        Log.d(LOG_TAG,"[test location update] the location servce is connected, going to request locaiton updates");

        // If the initial location was never previously requested, we use
        // FusedLocationApi.getLastLocation() to get it. If it was previously requested, we store
        // its value in mCurrentLocation.

        // Because we cache the value of the initial location in the Bundle, it means that if the
        // user launches the activity,
        // moves to a new location, and then changes the device orientation, the original location
        // is displayed as the activity is re-created.
        if (mCurrentLocation == null) {
            try{
                Log.d(LOG_TAG,"[test location update] mCurrentLocation is null");

                mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            }
            catch (SecurityException e){

            }
        }

        // If Minuku requests location updates before GoogleApiClient connects, we set
        // mRequestingLocationUpdates to true (See requestLocationUpdates). Here, we check
        // the value of mRequestingLocationUpdates and if it is true, we start location updates.
        if (mRequestingLocationUpdates) {
            Log.d(LOG_TAG,"[test location update] startLocationUpdate");

            startLocationUpdates();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(LOG_TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }




}

