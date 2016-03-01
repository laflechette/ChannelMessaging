package mathieu.larcher.channelmessaging;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.logging.LogRecord;

import mathieu.larcher.channelmessaging.adapter.MessageAdapter;
import mathieu.larcher.channelmessaging.network.RequestProvider;
import mathieu.larcher.channelmessaging.network.onWSRequestListener;

public class MessageActivity extends Activity implements onWSRequestListener, View.OnClickListener{

    private static final int REQUEST_MESSAGE = 1;
    private static final int REQUEST_SEND_MESSAGE = 0;
    private String token;
    private HashMap<String, String> mapParams = new HashMap<>();
    private Handler handler;

    ListView lst_view;
    Button btnSend;
    EditText txt_field;
    ImageView img;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_channel);

        lst_view = (ListView) findViewById(R.id.listView);

        txt_field = (EditText) findViewById(R.id.txt_view_message);
        btnSend = (Button) findViewById(R.id.btn_send);
        img = (ImageView) findViewById(R.id.img_profile);

        btnSend.setOnClickListener(this);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        token = settings.getString("token", null);
        mapParams.put("accesstoken", token);

        mapParams.put("channelid", "" + getIntent().getIntExtra("channelsID", 1));

        RequestProvider np = new RequestProvider(mapParams, "http://www.raphaelbischof.fr/messaging/?function=getmessages", REQUEST_MESSAGE);
        np.setOnWSRequestListener(this);
        np.execute();

        handler = new Handler();

        final Runnable r = new Runnable() {
            public void run() {
                refreshAllMessages();
                handler.postDelayed(this, 1000);
            }
        };

        handler.postDelayed(r, 0);
    }

    public static final String PREFS_NAME = "MyPrefsFile";

    @Override
    public void onCompletedRequest(String result, int requestCode) {

        if (REQUEST_MESSAGE == requestCode){

            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

            MasterMessage jsonObj = gson.fromJson(result, MasterMessage.class);

            MessageAdapter adapter = new MessageAdapter(this, jsonObj.getMessages());

            lst_view.setAdapter(adapter);
        }
    }

    public void refreshAllMessages(){

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        token = settings.getString("token", null);
        mapParams.put("accesstoken", token);

        mapParams.put("channelid", "" + getIntent().getIntExtra("channelsID", 1));

        RequestProvider np = new RequestProvider(mapParams, "http://www.raphaelbischof.fr/messaging/?function=getmessages", REQUEST_MESSAGE);
        np.setOnWSRequestListener(this);
        np.execute();
    }

    @Override
    public void onErrorRequest(String error) {

    }

    @Override
    public void onClick(View v) {

        mapParams.clear();

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        token = settings.getString("token", null);
        mapParams.put("accesstoken", token);

        String txtMessage = txt_field.getText().toString();

        mapParams.put("message", txtMessage);
        mapParams.put("channelid", "" + getIntent().getIntExtra("channelsID", 1));

        RequestProvider np = new RequestProvider(mapParams, "http://www.raphaelbischof.fr/messaging/?function=sendmessage", REQUEST_SEND_MESSAGE);
        np.setOnWSRequestListener(this);
        np.execute();

        txt_field.setText("");
    }
}
