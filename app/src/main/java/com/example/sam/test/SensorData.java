package com.example.sam.test;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

/**
 * Created by sam on 2017/1/3.
 */

public class SensorData implements SensorEventListener {

    private SensorManager mSensorManager;
    public String LOG_TAG="SHIT SENSOR FUCK";

    public static String mAccele_x="";
    public static String mAccele_y="";
    public static String mAccele_z="";
    public static String mGyroscope_x="";
    public static String mGyroscope_y="";
    public static String mGyroscope_z="";
    public static String mGravity_x="";
    public static String mGravity_y="";
    public static String mGravity_z="";
    public static String mLinearAcceleration_x="";
    public static String mLinearAcceleration_y="";
    public static String mLinearAcceleration_z="";
    public static String mRotationVector_x_sin="";
    public static String mRotationVector_y_sin="";
    public static String mRotationVector_z_sin="";
    public static String mRotationVector_cos="";
    public static String mMagneticField_x="";
    public static String mMagneticField_y="";
    public static String mMagneticField_z="";
    public static String mProximity="";
    public static String mAmbientTemperature="";
    public static String mLight="";
    public static String mPressure="";
    public static String mRelativeHumidity="";
    public static String mHeartRate="";
    public static String mStepCount="";
    public static String mStepDetect="";


    public SensorData(Context context){

        mSensorManager = (SensorManager) context.getSystemService(context.SENSOR_SERVICE);
        registerSensor();


    }
    @Override
    public void onSensorChanged(SensorEvent event) {
    // TODO Auto-generated method stub
        /**Motion Sensor**/
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            getAccelerometer(event);
        }
        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE){
            getGyroscope(event);
        }
        if (event.sensor.getType() == Sensor.TYPE_GRAVITY){
            getGravity(event);
        }
        if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION){
            getLinearAcceleration(event);
        }
        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR){
            getRotationVector(event);
        }

        /**Position Sensor**/
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY){
            //Log.d(LOG_TAG, "in [onSensorChange] Proximity: " +  event.values[0] );
            getProximity(event);
        }
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
            //Log.d(LOG_TAG, "in [onSensorChange] Proximity: " +  event.values[0] );
            getMagneticField(event);
        }
        if (event.sensor.getType() == Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR){
            //Log.d(LOG_TAG, "in [onSensorChange] Proximity: " +  event.values[0] );
            getMagneticField(event);
        }

        /**Environment Sensor**/
        if (event.sensor.getType() == Sensor.TYPE_LIGHT){
            getLight(event);
        }
        if (event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE){
            getAmbientTemperature(event);
        }
        if (event.sensor.getType() == Sensor.TYPE_PRESSURE){
            getPressure(event);
        }
        if (event.sensor.getType() == Sensor.TYPE_RELATIVE_HUMIDITY){
            getRelativeHumidity(event);
        }

        /**health related**/
        if (event.sensor.getType() == Sensor.TYPE_HEART_RATE){
            getHeartRate (event);
        }
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER){
            getStepCounter(event);
        }
        if (event.sensor.getType() == Sensor.TYPE_STEP_DETECTOR){
            getStepDetector(event);
        }


    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    // TODO Auto-generated method stub

    }

    public void registerSensor() {

        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                mSensorManager.SENSOR_DELAY_NORMAL);

        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
                mSensorManager.SENSOR_DELAY_NORMAL);

        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY),
                mSensorManager.SENSOR_DELAY_NORMAL);

        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR),
                mSensorManager.SENSOR_DELAY_NORMAL);

        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION),
                mSensorManager.SENSOR_DELAY_NORMAL);

        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY),
                mSensorManager.SENSOR_DELAY_NORMAL);

        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE),
                mSensorManager.SENSOR_DELAY_NORMAL);

        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT),
                mSensorManager.SENSOR_DELAY_NORMAL);

        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE),
                mSensorManager.SENSOR_DELAY_NORMAL);

        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY),
                mSensorManager.SENSOR_DELAY_NORMAL);

        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE),
                mSensorManager.SENSOR_DELAY_NORMAL);

        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER),
                mSensorManager.SENSOR_DELAY_NORMAL);

        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR),
                mSensorManager.SENSOR_DELAY_NORMAL);
    }


    /**get Accelerometer values**/
    private void getAccelerometer(SensorEvent event) {


        //Log.d(LOG_TAG, "getting accelerometer:" + mAccele_x + " : " +  mAccele_y +  " : " + mAccele_y);

        mAccele_x = String.valueOf(event.values[0]);	// Acceleration force along the x axis (including gravity). m/s2
        mAccele_y = String.valueOf(event.values[1]);	// Acceleration force along the y axis (including gravity). m/s2
        mAccele_z = String.valueOf(event.values[2]);	// Acceleration force along the z axis (including gravity). m/s2

    }


    /**get Gyroscope values**/
    private void getGyroscope(SensorEvent event) {

        mGyroscope_x = String.valueOf(event.values[0]);	// Rate of rotation around the x axis. rad/s
        mGyroscope_y = String.valueOf(event.values[1]);	// Rate of rotation around the y axis. rad/s
        mGyroscope_z = String.valueOf(event.values[2]);	// Rate of rotation around the z axis. rad/s


    }


    /**get gravity values**/
    private void getGravity(SensorEvent event) {
        mGravity_x = String.valueOf(event.values[0]);	// Force of gravity along the x axis m/s2
        mGravity_y = String.valueOf(event.values[1]);	// Force of gravity along the y axis m/s2
        mGravity_z = String.valueOf(event.values[2]);	// Force of gravity along the z axis m/s2

    }
    /**get linear acceleration values**/
    private void getLinearAcceleration(SensorEvent event) {
        mLinearAcceleration_x = String.valueOf(event.values[0]);	//Acceleration force along the x axis (excluding gravity).  m/s2
        mLinearAcceleration_y = String.valueOf(event.values[1]);	//Acceleration force along the y axis (excluding gravity).  m/s2
        mLinearAcceleration_z = String.valueOf(event.values[2]);	//Acceleration force along the z axis (excluding gravity).  m/s2

    }

    /**get rotation vector values**/
    private void getRotationVector(SensorEvent event) {
        mRotationVector_x_sin = String.valueOf(event.values[0]);	// Rotation vector component along the x axis (x * sin(�c/2))  Unitless
        mRotationVector_y_sin = String.valueOf(event.values[1]);	// Rotation vector component along the y axis (y * sin(�c/2)). Unitless
        mRotationVector_z_sin = String.valueOf(event.values[2]);	//  Rotation vector component along the z axis (z * sin(�c/2)). Unitless
        mRotationVector_cos = String.valueOf(event.values[3]);		// Scalar component of the rotation vector ((cos(�c/2)).1 Unitless

    }



    /**get magnetic field values**/
    private void getMagneticField(SensorEvent event){
        mMagneticField_x = String.valueOf(event.values[0]);	// Geomagnetic field strength along the x axis.
        mMagneticField_y = String.valueOf(event.values[1]);	// Geomagnetic field strength along the y axis.
        mMagneticField_z = String.valueOf(event.values[2]);	// Geomagnetic field strength along the z axis.

    }

    /**get proximity values**/
    private void getProximity(SensorEvent event){

//        Log.d(LOG_TAG, "getting proximity" + mProximity);

        mProximity = String.valueOf(event.values[0]);

    }

    private void getAmbientTemperature(SensorEvent event){
        /* Environment Sensors */
        mAmbientTemperature = String.valueOf(event.values[0]);


    }

    private void getLight(SensorEvent event){

        //Log.d(LOG_TAG, "getting light" + mLight);

        mLight = String.valueOf(event.values[0]);

    }

    private void getPressure(SensorEvent event){
        mPressure = String.valueOf(event.values[0]);

    }

    private void getRelativeHumidity(SensorEvent event){
        mRelativeHumidity = String.valueOf(event.values[0]);

    }

    private void getHeartRate (SensorEvent event) {
        mHeartRate = String.valueOf(event.values[0]);

    }

    private void getStepCounter (SensorEvent event) {
        mStepCount = String.valueOf(event.values[0]);


    }

    private void getStepDetector (SensorEvent event) {
        mStepDetect = String.valueOf(event.values[0]);

    }
}
