package mathieu.larcher.channelmessaging.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;

import mathieu.larcher.channelmessaging.app_message.MasterMessage;
import mathieu.larcher.channelmessaging.R;
import mathieu.larcher.channelmessaging.adapter.MessageAdapter;
import mathieu.larcher.channelmessaging.network.RequestProvider;
import mathieu.larcher.channelmessaging.network.onWSRequestListener;

/**
 * Created by mathieularchet on 07/03/2016.
 */
public class MessageFragment extends Fragment implements View.OnClickListener, onWSRequestListener {

    private static final int REQUEST_MESSAGE = 1;
    private static final int REQUEST_SEND_MESSAGE = 0;
    private String token;
    private HashMap<String, String> mapParams = new HashMap<>();
    private Handler handler;
    public int channelID;
    private SharedPreferences settings;
    ListView lst_view_fragment;
    Button btnSend;
    EditText txt_field;
    ImageView img;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_message, container);

        channelID = getActivity().getIntent().getIntExtra("channelsID", 1);

        lst_view_fragment = (ListView) v.findViewById(R.id.listView);

        txt_field = (EditText) v.findViewById(R.id.txt_view_message);
        btnSend = (Button) v.findViewById(R.id.btn_send);
        img = (ImageView) v.findViewById(R.id.img_profile);

        btnSend.setOnClickListener(this);

        settings = getActivity().getSharedPreferences(PREFS_NAME, 0);
        token = settings.getString("token", null);
        mapParams.put("accesstoken", token);

        mapParams.put("channelid", "" + channelID);

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

        return v;
    }

    @Override
    public void onCompletedRequest(String result, int requestCode) {

        if (REQUEST_MESSAGE == requestCode){

            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

            MasterMessage jsonObj = gson.fromJson(result, MasterMessage.class);

            MessageAdapter adapter = new MessageAdapter(this.getActivity(), jsonObj.getMessages());

            lst_view_fragment.setAdapter(adapter);
        }
    }

    public void refreshAllMessages(){

        token = settings.getString("token", null);
        mapParams.put("accesstoken", token);

        if(getActivity() != null) {
            mapParams.put("channelid", "" + getActivity().getIntent().getIntExtra("channelsID", 1));
            RequestProvider np = new RequestProvider(mapParams, "http://www.raphaelbischof.fr/messaging/?function=getmessages", REQUEST_MESSAGE);
            np.setOnWSRequestListener(this);
            np.execute();
        }
    }

    public static final String PREFS_NAME = "MyPrefsFile";

    @Override
    public void onErrorRequest(String error) {

    }

    @Override
    public void onClick(View v) {

        mapParams.clear();

        SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, 0);
        token = settings.getString("token", null);
        mapParams.put("accesstoken", token);

        String txtMessage = txt_field.getText().toString();

        mapParams.put("message", txtMessage);
        mapParams.put("channelid", "" + getActivity().getIntent().getIntExtra("channelsID", 1));

        RequestProvider np = new RequestProvider(mapParams, "http://www.raphaelbischof.fr/messaging/?function=sendmessage", REQUEST_SEND_MESSAGE);
        np.setOnWSRequestListener(this);
        np.execute();

        txt_field.setText("");
    }
}
