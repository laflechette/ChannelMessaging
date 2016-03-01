package mathieu.larcher.channelmessaging.network;

/**
 * Created by mathieularchet on 02/02/2016.
 */
public interface onWSRequestListener {

    public void onCompletedRequest(String result, int requestCode);
    public void onErrorRequest(String error);
}
