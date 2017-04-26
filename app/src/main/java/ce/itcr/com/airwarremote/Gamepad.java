package ce.itcr.com.airwarremote;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;





public class Gamepad extends Activity {
    private static TCPClient mTcpClient;
    private static String ip;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamepad);
        Bundle bundle = getIntent().getExtras();
        ip = bundle.getString("direccion_ip");
        // connect to the server
        new connectTask().execute("");
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
            mTcpClient.SERVERIP=ip;
            mTcpClient.run();

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

        }
    }
}
