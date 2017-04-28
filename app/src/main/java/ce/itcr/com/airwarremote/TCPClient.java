package ce.itcr.com.airwarremote;

/**
 * Created by Sebas Mora on 28/03/2017.
 */

import android.os.StrictMode;
import android.util.Log;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;


class TCPClient {

    private String serverMessage;
    private static final int SERVERPORT = 8085;
    private OnMessageReceived mMessageListener = null;
    private boolean mRun = false;
    private int SDK_INT = android.os.Build.VERSION.SDK_INT;
    private DataOutputStream writer;
    private BufferedReader in;

    /**
     *  Constructor of the class. OnMessagedReceived listens for the messages received from server
     */
    TCPClient(OnMessageReceived listener) {
        mMessageListener = listener;
    }

    /**
     * Sends the message entered by client to the server
     * @param message text entered by client
     */
    void sendMessage(int message){
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        try{
            if (writer != null) {
                writer.writeInt(message);
                writer.flush();
            }
        /*else{
            if (writer != null && !writer.checkError()) {
                writer.println(message);
                writer.flush();
            }
        }*/
    }catch (IOException e){
            System.out.print("error en entero");
    }
    }

    public void stopClient(){
        mRun = false;
    }

    void run(String SERVERIP) {

        mRun = true;

        try {
            InetAddress serverAddr = InetAddress.getByName(SERVERIP);

            Log.e("TCP Client", "C: Connecting...");

            //create a socket to make the connection with the server

            try (Socket socket = new Socket(serverAddr, SERVERPORT)) {

                //send the message to the server
                writer = new DataOutputStream(socket.getOutputStream());

                Log.e("TCP Client", "C: Sent.");

                Log.e("TCP Client", "C: Done.");

                //receive the message which the server sends back
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                //in this while the client listens for the messages sent by the server
                while (mRun) {
                    serverMessage = in.readLine();

                    if (serverMessage != null && mMessageListener != null) {
                        //call the method messageReceived from MyActivity class
                        mMessageListener.messageReceived(serverMessage);
                    }
                    serverMessage = null;

                }

                Log.e("RESPONSE FROM SERVER", "S: Received Message: '" + serverMessage + "'");

            } catch (Exception e) {

                Log.e("TCP", "S: Error", e);

            }
            //the socket must be closed. It is not possible to reconnect to this socket
            // after it is closed, which means a new socket instance has to be created.


        } catch (Exception e) {

            Log.e("TCP", "C: Error", e);

        }

    }

    //Declare the interface. The method messageReceived(String message) will must be implemented
    interface OnMessageReceived {
        void messageReceived(String message);
    }
}