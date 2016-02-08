package mathieu.larcher.channelmessaging;

/**
 * Created by mathieularchet on 08/02/2016.
 */
public class Channel {

    private int channelID;
    private String name;
    private int connectedusers;

    public Channel(){

    }

    public int getChannelID() {
        return channelID;
    }

    public String getName() {
        return name;
    }

    public int getConnectedusers() {
        return connectedusers;
    }
}
