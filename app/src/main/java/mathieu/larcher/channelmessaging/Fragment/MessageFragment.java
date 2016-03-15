package mathieu.larcher.channelmessaging.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mathieu.larcher.channelmessaging.app_gps.GPSActivity;
import mathieu.larcher.channelmessaging.app_gps.MapActivity;
import mathieu.larcher.channelmessaging.app_message.MasterMessage;
import mathieu.larcher.channelmessaging.R;
import mathieu.larcher.channelmessaging.adapter.MessageAdapter;
import mathieu.larcher.channelmessaging.network.RequestProvider;
import mathieu.larcher.channelmessaging.network.onWSRequestListener;

/**
 * Created by mathieularchet on 07/03/2016.
 */
public class MessageFragment extends Fragment implements View.OnClickListener, onWSRequestListener, AdapterView.OnItemClickListener {

    private static final int REQUEST_MESSAGE = 1;
    private static final int REQUEST_SEND_MESSAGE = 0;
    protected Location mCurrentLocation;
    private String token;
    private HashMap<String, String> mapParams = new HashMap<>();
    private Handler handler;
    public int channelID;
    private SharedPreferences settings;
    protected ArrayList<Double> messagesList;


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
        lst_view_fragment.setOnItemClickListener(this);

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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        List<String> listItems = new ArrayList<String>();
        listItems.add("Ajouter un ami");
        listItems.add("Voir sur la carte");

        if ( ((GPSActivity) getActivity()).getmCurrentLocation() != null){

            String latitude = String.valueOf(((GPSActivity)getActivity()).mCurrentLocation.getLatitude());
            String longitude = String.valueOf(((GPSActivity)getActivity()).mCurrentLocation.getLongitude());

            double latitude_double = Double.valueOf(latitude);
            double longitude_double = Double.valueOf(longitude);

            mapParams.put("latitude", latitude);
            mapParams.put("longitude", longitude);
        }

            final CharSequence[] arr = listItems.toArray(new CharSequence[listItems.size()]);
            new AlertDialog.Builder(getActivity())
                    .setIcon(android.R.drawable.ic_dialog_alert)//drawable de l'icone à gauche du titre
                    .setTitle("Choisissez une option")//Titre de l'alert dialog
                    .setItems(arr, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {//which = la position de l'item appuyé
                            if (which == 0) {
                                //Do some stuff (1st item touched)
                            } else {
                                //Do some over stuff (2nd item touched)
                                Intent myIntent = new Intent(getContext(), MapActivity.class);
                                myIntent.putExtra("username", mapParams.get("username")); //Optional parameters
                                myIntent.putExtra("latitude", mapParams.get("latitude")); //Optional parameters
                                myIntent.putExtra("longitude", mapParams.get("longitude")); //Optional parameters
                                startActivity(myIntent);
                            }
                        }
                    })//items de l'alert dialog
                    .show();
        }
    }
