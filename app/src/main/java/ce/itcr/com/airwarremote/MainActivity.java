package ce.itcr.com.airwarremote;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {
    private EditText IP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IP = (EditText)findViewById(R.id.ip_entry);
    }
    public void connect (View V){
        Intent i = new Intent(this,Gamepad.class);
        i.putExtra("direccion_ip",IP.getText().toString());
        startActivity(i);
    }
}
