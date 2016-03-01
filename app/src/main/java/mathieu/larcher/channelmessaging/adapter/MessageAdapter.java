package mathieu.larcher.channelmessaging.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import mathieu.larcher.channelmessaging.Channel;
import mathieu.larcher.channelmessaging.Message;
import mathieu.larcher.channelmessaging.R;

/**
 * Created by mathieularchet on 08/02/2016.
 */
public class MessageAdapter extends BaseAdapter {

    private final Context context;
    ArrayList<Message> allMessages;

    public MessageAdapter(Context context, ArrayList<Message> messages) {
        this.allMessages = messages;
        this.context = context;
    }

    @Override
    public int getCount() {
        return allMessages.size();
    }

    @Override
    public Message getItem(int position) {
        return allMessages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.adapter_message, parent, false);

        TextView userName = (TextView) rowView.findViewById(R.id.txt_user_id);
        TextView message = (TextView) rowView.findViewById(R.id.txt_message);
        TextView date = (TextView) rowView.findViewById(R.id.txt_date);

        String url = allMessages.get(position).getImageUrl();

        Picasso.with(context).load(url).transform(new CropSquareTransformation()).into((ImageView) rowView.findViewById(R.id.img_profile));

        rowView.setTag(allMessages.get(position));

        Date dateBrut =  allMessages.get(position).getDate();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");


        userName.setText(allMessages.get(position).getUserName());
        message.setText(allMessages.get(position).getMessage());
        date.setText(dateFormat.format(dateBrut));

        return rowView;
    }

    public class CropSquareTransformation implements Transformation {
        @Override public Bitmap transform(Bitmap source) {

            Bitmap output = Bitmap.createBitmap(source.getWidth(),
                    source.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);

            final int color = 0xff424242;
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, source.getWidth(), source.getHeight());
            final RectF rectF = new RectF(rect);
            final float roundPx = 55;

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(source, rect, rect, paint);

            source.recycle();

            return output;
        }

        @Override public String key() { return "round()"; }
    }

}
