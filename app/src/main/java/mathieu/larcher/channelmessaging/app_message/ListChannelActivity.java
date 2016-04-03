package mathieu.larcher.channelmessaging.app_message;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;

import mathieu.larcher.channelmessaging.Fragment.MessageFragment;
import mathieu.larcher.channelmessaging.R;
import mathieu.larcher.channelmessaging.app_gps.GPSActivity;

public class ListChannelActivity extends GPSActivity implements AdapterView.OnItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel_list);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Channel channel = (Channel)view.getTag();
        int channelID = channel.getChannelID();

        MessageFragment fragB = (MessageFragment) getSupportFragmentManager().findFragmentById(R.id.message_fragment);

        if(fragB == null || !fragB.isInLayout()){

            Intent i = new Intent(getApplicationContext(), MessageActivity.class);
            i.putExtra("channelsID ", channelID);
            startActivity(i);
        }
        else
        {
            fragB.channelID = channelID;
        }
    }
}
