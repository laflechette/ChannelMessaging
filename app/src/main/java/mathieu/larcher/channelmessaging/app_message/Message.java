package mathieu.larcher.channelmessaging.app_message;

import android.media.Image;

import java.util.Date;

/**
 * Created by mathieularchet on 08/02/2016.
 */
public class Message {

    private String username;
    private String message;
    private Date date;
    private String imageUrl;
    private double longitude;
    private double latitude;

    public Message(){

    }

    public String getUserName() { return username; }

    public String getMessage() { return message; }

    public Date getDate() { return date; }

    public String getImageUrl() { return imageUrl; }

    public double getLongitude(){ return longitude; }

    public double getLatitude(){ return latitude; }
}
