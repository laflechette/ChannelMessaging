package mathieu.larcher.channelmessaging;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.HashMap;

import mathieu.larcher.channelmessaging.network.RequestProvider;
import mathieu.larcher.channelmessaging.network.onWSRequestListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, onWSRequestListener {

    private TextView txt_view_login;
    private TextView txt_view_mdp;
    private EditText txt_edit_login;
    private EditText txt_edit_mdp;
    private Button btn_envoyer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        txt_edit_login = (EditText) findViewById(R.id.txt_edit_login);
        txt_edit_mdp = (EditText) findViewById(R.id.txt_edit_mdp);
        txt_view_mdp = (TextView) findViewById(R.id.txt_view_mdp);
        txt_view_login = (TextView) findViewById(R.id.txt_view_login);
        btn_envoyer = (Button) findViewById(R.id.btn_envoyer);

        btn_envoyer.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        String myLogin = txt_edit_login.getText().toString();
        String myPassword = txt_edit_mdp.getText().toString();

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", myLogin);
        params.put("password", myPassword);

        RequestProvider np = new RequestProvider(params, "http://www.raphaelbischof.fr/messaging/?function=connect", 0);
        np.setOnWSRequestListener(this);
        np.execute();
    }

    @Override
    public void onCompletedRequest(String result, int requestCode) {

        Gson gson = new Gson();
        Login jsonObj = gson.fromJson(result, Login.class);

        if (jsonObj.getCode() == 200) {

            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("token", jsonObj.getToken());
            editor.commit();

            Intent intent = new Intent(this, ListChannelActivity.class);
            startActivity(intent);

            Toast.makeText(getApplicationContext(), jsonObj.getToken(), Toast.LENGTH_SHORT).show();
        }
        else {
            onErrorRequest(result);
        }



    }

    @Override
    public void onErrorRequest(String error) {

        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();

    }

    public static final String PREFS_NAME = "MyPrefsFile";
}
