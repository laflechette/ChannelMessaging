package mathieu.larcher.channelmessaging;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.HashMap;

import mathieu.larcher.channelmessaging.adapter.ChannelAdapter;
import mathieu.larcher.channelmessaging.network.RequestProvider;
import mathieu.larcher.channelmessaging.network.onWSRequestListener;

public class ChannelActivity extends Activity implements onWSRequestListener, AdapterView.OnItemClickListener {

    private String token;
    private HashMap<String, String> mapParams = new HashMap<>();
    ListView lst_view_channel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_channel);

        lst_view_channel = (ListView) findViewById(R.id.lst_view_channel);
        lst_view_channel.setOnItemClickListener(this);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        token = settings.getString("token", null);
        mapParams.put("accesstoken", token);

        RequestProvider np = new RequestProvider(mapParams, "http://www.raphaelbischof.fr/messaging/?function=getchannels");
        np.setOnWSRequestListener(this);
        np.execute();

    }

    public static final String PREFS_NAME = "MyPrefsFile";

    @Override
    public void onCompletedRequest(String result) {

        Gson gson = new Gson();
        MasterChannel jsonObj = gson.fromJson(result, MasterChannel.class);

        ChannelAdapter Adapter = new ChannelAdapter(this, jsonObj.getChannels());

        lst_view_channel.setAdapter(new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, jsonObj.getChannels()));

        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onErrorRequest(String error) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
