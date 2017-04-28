package ce.itcr.com.airwarremote;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.os.Vibrator;

public class Gamepad extends Activity implements
        SensorEventListener {
    private static TCPClient mTcpClient;
    private static String ip;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private TextView txt1;
    private Vibrator V;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamepad);
        Bundle bundle = getIntent().getExtras();
        txt1 = (TextView)findViewById(R.id.txt1);
        ip = bundle.getString("direccion_ip");
        V= (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        // connect to the server
        new connectTask().execute("");
    }
    public void shoot(View view){
        mTcpClient.sendMessage(5);
    }
    public void powerUp(View view){mTcpClient.sendMessage(6);}

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {
        // TODO Auto-generated method stub
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        if (Math.abs(x) > Math.abs(y)) {
            if (x < -4) {
                mTcpClient.sendMessage(1);
            }
            if (x > 4) {
                mTcpClient.sendMessage(3);
                //Log.e("acc","izquierda");
            }
        } else {
            if (y < -3) {
                mTcpClient.sendMessage(4);
                //Log.e("acc","arriba");
            }
            if (y > 3) {
                mTcpClient.sendMessage(2);
                //Log.e("acc","abajo");
            }
        }
        /*if (x > (-2) && x < (2) && y > (-2) && y < (2)) {
            //Log.e("acc","centro");
        }*/
    }
    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    private class connectTask extends AsyncTask<String,String,TCPClient> {

        @Override
        protected TCPClient doInBackground(String... message) {

            //we create a TCPClient object and
            mTcpClient = new TCPClient(new TCPClient.OnMessageReceived() {
                @Override
                public void messageReceived(String message) {
                    //this method calls the onProgressUpdate
                    publishProgress(message);
                }
            });
            mTcpClient.run(ip);

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            txt1.setText(values[0]);
            V.vibrate(100);
        }
    }
}
