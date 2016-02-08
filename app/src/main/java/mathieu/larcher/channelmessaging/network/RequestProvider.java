package mathieu.larcher.channelmessaging.network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.os.AsyncTask;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by mathieularchet on 02/02/2016.
 */
public class RequestProvider extends AsyncTask<String, Integer, String> {

    private ArrayList<onWSRequestListener> listeners = new ArrayList<onWSRequestListener>();
    private String requestURL = null;
    private HashMap<String, String> mapParams;

    public RequestProvider(HashMap<String, String> params, String requestURL) {

        this.mapParams = params;
        this.requestURL = requestURL;
    }


    public void setOnWSRequestListener(onWSRequestListener listener){

        this.listeners.add(listener);
    }

    private void RequestCompleted(String result){

        for(onWSRequestListener oneListener:listeners){

            oneListener.onCompletedRequest(result);
        }
    }


    private void RequestErrors(String error){

        for(onWSRequestListener oneListener:listeners){

            oneListener.onErrorRequest(error);
        }
    }

    @Override
    protected String doInBackground(String... params) {

        String response = "";
        try {
            URL url = new URL(requestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection(); conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(mapParams));
            writer.flush();
            writer.close();
            os.close();
            int responseCode=conn.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream())); while ((line=br.readLine()) != null) {
                    response+=line; }
            } else {
                response=""; }
        } catch (Exception e) {
            e.printStackTrace(); }
        return response;
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first) first = false;
            else result.append("&");
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8")); result.append("="); result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return result.toString();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        RequestCompleted(s);
    }
}
