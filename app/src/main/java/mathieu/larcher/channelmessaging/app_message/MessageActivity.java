package mathieu.larcher.channelmessaging.app_message;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import mathieu.larcher.channelmessaging.R;
import mathieu.larcher.channelmessaging.app_gps.GPSActivity;

public class MessageActivity extends GPSActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_channel);

    }
}
