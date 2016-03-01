package mathieu.larcher.channelmessaging;

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

    public Message(){

    }

    public String getUserName() { return username; }

    public String getMessage() {
        return message;
    }

    public Date getDate() { return date; }

    public String getImageUrl() { return imageUrl; }
}
