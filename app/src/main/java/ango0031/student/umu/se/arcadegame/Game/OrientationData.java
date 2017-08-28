package ango0031.student.umu.se.arcadegame.Game;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import ango0031.student.umu.se.arcadegame.Game.Constants;

/**
 * Created by Anton on 15/08/2017.
 */

public class OrientationData implements SensorEventListener {
    private SensorManager manager;
    private Sensor accelorometer;
    private Sensor magnometer;

    private float[] accelOutput;
    private float[] magOutput;

    private float[] orientation = new float[3];
    private float[] startOrientation = null;


    public OrientationData() {
        manager = (SensorManager) Constants.CURRENT_CONTEXT.getSystemService(Context.SENSOR_SERVICE);
        accelorometer = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnometer = manager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    public void newGame() {
        startOrientation = null;
    }

    public float[] getOrientation() {
        return orientation;
    }

    public float[] getStartOrientation() {
        return startOrientation;
    }

    public void register() {
        manager.registerListener(this, accelorometer, SensorManager.SENSOR_DELAY_GAME);
        manager.registerListener(this, magnometer, SensorManager.SENSOR_DELAY_GAME);
    }

    public void pause() {
        manager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int type = event.sensor.getType();
        if (type == Sensor.TYPE_ACCELEROMETER)
            accelOutput = event.values;
        else if (type == Sensor.TYPE_MAGNETIC_FIELD)
            magOutput = event.values;
        if (accelOutput != null && magOutput != null) {
            float[] R = new float[9];
            float[] I = new float[9];
            boolean succes = SensorManager.getRotationMatrix(R, I, accelOutput, magOutput);
            if (succes) {
                SensorManager.getOrientation(R, orientation);
                if (startOrientation == null) {
                    startOrientation = new float[orientation.length];
                    System.arraycopy(orientation, 0, startOrientation, 0, orientation.length);
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
