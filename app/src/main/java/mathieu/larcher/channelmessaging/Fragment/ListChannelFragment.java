package mathieu.larcher.channelmessaging.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.HashMap;

import mathieu.larcher.channelmessaging.app_message.MasterChannel;
import mathieu.larcher.channelmessaging.R;
import mathieu.larcher.channelmessaging.adapter.ChannelAdapter;
import mathieu.larcher.channelmessaging.network.RequestProvider;
import mathieu.larcher.channelmessaging.network.onWSRequestListener;

/**
 * Created by mathieularchet on 01/03/2016.
 */
public class ListChannelFragment extends Fragment implements onWSRequestListener, AdapterView.OnItemClickListener {


    ListView lst_view_fragment;
    private String token;
    private HashMap<String, String> mapParams = new HashMap<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_list_channel,container);

        lst_view_fragment = (ListView) v.findViewById(R.id.channelFragment);
        lst_view_fragment.setOnItemClickListener((AdapterView.OnItemClickListener) this.getActivity());

        SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, 0);
        token = settings.getString("token", null);
        mapParams.put("accesstoken", token);

        RequestProvider np = new RequestProvider(mapParams, "http://www.raphaelbischof.fr/messaging/?function=getchannels", 0);
        np.setOnWSRequestListener(this);
        np.execute();

        return v;
    }

    public static final String PREFS_NAME = "MyPrefsFile";

    @Override
    public void onCompletedRequest(String result, int requestCode) {

        Gson gson = new Gson();
        MasterChannel jsonObj = gson.fromJson(result, MasterChannel.class);

        ChannelAdapter adapter = new ChannelAdapter(this.getActivity(), jsonObj.getChannels());

        lst_view_fragment.setAdapter(adapter);
    }

    @Override
    public void onErrorRequest(String error) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


    }
}
