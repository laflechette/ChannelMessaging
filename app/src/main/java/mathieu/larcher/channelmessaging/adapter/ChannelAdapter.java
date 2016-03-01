package mathieu.larcher.channelmessaging.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import mathieu.larcher.channelmessaging.Channel;
import mathieu.larcher.channelmessaging.R;

/**
 * Created by mathieularchet on 08/02/2016.
 */
public class ChannelAdapter extends BaseAdapter {

    private final Context context;
    ArrayList<Channel> allChannels;

    public ChannelAdapter(Context context, ArrayList<Channel> channels) {
        this.allChannels = channels;
        this.context = context;
    }

    @Override
    public int getCount() {
        return allChannels.size();
    }

    @Override
    public Channel getItem(int position) {
        return allChannels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.adapter_list_channel, parent, false);

        TextView nameChannel = (TextView) rowView.findViewById(R.id.txt_view_name);
        TextView nbUser = (TextView) rowView.findViewById(R.id.txt_view_number);

        rowView.setTag(allChannels.get(position));

        nameChannel.setText(allChannels.get(position).getName());
        nbUser.setText("Nombre d'utilisateurs connectés : " + allChannels.get(position).getConnectedusers());

        return rowView;
    }
}
