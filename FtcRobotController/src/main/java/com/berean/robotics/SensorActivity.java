package com.berean.robotics;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

/**
 * Created by bhoward on 9/7/15.
 */
public class SensorActivity extends Activity implements SensorEventListener{


    public float sensor_x_value;
    public float sensor_y_value;
    public float sensor_z_value;

    private static final float NOISE_FACTOR = (float) 2.0;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    private float mLastX = (float) 0.0;
    private float mLastY = (float) 0.0;
    private float mLastZ = (float) 0.0;
    private boolean sensorIsInitialized;


    public SensorActivity() {

    }

    @Override
    public void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        sensorIsInitialized = false;
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

    }
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        if (!sensorIsInitialized) {
            mLastX = x;
            mLastY = y;
            mLastZ = z;
            sensorIsInitialized = true;
        } else
        {
            float deltaX = Math.abs(mLastX - x);
            float deltaY = Math.abs(mLastY - y);
            float deltaZ = Math.abs(mLastZ - z);
            if (deltaX < NOISE_FACTOR) deltaX = (float)0.0;
            if (deltaY < NOISE_FACTOR) deltaY = (float)0.0;
            if (deltaZ < NOISE_FACTOR) deltaZ = (float)0.0;
            mLastX = x;
            mLastY = y;
            mLastZ = z;
            this.sensor_x_value = deltaX;
            this.sensor_y_value = deltaY;
            this.sensor_z_value = deltaZ;
        }
    }
}
