package mathieu.larcher.channelmessaging;

/**
 * Created by mathieularchet on 08/02/2016.
 */
public class Login {

    private String response;
    private int code;
    private String accesstoken;

    public Login(){

    }

    public String getToken() {
        return accesstoken;
    }

    public String getResponse() {
        return response;
    }

    public int getCode() {
        return code;
    }
}
