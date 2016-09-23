package activity;

/**
 * Created by zxcvbn on 07/07/2016.
 */
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zxcvbn.bookstore.R;

import model.LoginRespon;
import model.UserLogin;
import provider.GlobalData;
import provider.PrefManager;
import rest.ApiClient;
import rest.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import provider.SessionManager;
import util.Util;

public class LoginFormActivity extends AppCompatActivity {

    private PrefManager prefManager;

    SharedPreferences pref;
    SharedPreferences.Editor editor ;
    DialogManager alert = new DialogManager();
    Util util_;
    EditText username;
    EditText password;
    TextView textView;
    Button sign_in;
    SessionManager session;
    String pwd ;
    String usrnm;
    GlobalData globaldata;
    Animation animFadein;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginform);

        username = (EditText) findViewById(R.id.txt_username_);
        password  = (EditText) findViewById(R.id.txt_pwd_);

        globaldata = (GlobalData) getApplicationContext();

        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();

        session = new SessionManager(getApplicationContext());

        util_ = Util.getInstance(getApplicationContext());

        //check connection
        if(util_.isInternetAvailable()){
            Log.d("Internet","TRUE");
        }else{
            alert.showAlertDialog(LoginFormActivity.this, "Connection Failed", "Please ensure you've a connection", false);

            animFadein = AnimationUtils.loadAnimation(getApplicationContext(),
                    R.anim.bounce);
            View inflatedView = getLayoutInflater().inflate(R.layout.connection, null);
            ImageView ivConnect = (ImageView) inflatedView.findViewById(R.id.ivConnect);
            ivConnect.setAnimation(animFadein);
        }

        //register
        textView = (TextView) findViewById(R.id.register);
        textView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginFormActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        //login
        sign_in = (Button) findViewById(R.id.btn_signin);
        sign_in.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                authenticate();
            }
        });
    }

    public void authenticate(){
        final UserLogin user = new UserLogin();

        //dummy
       /* user.setUsername("siti2");
        user.setPassword("900150983cd24fb0d6963f7d28e17f72");*/

        pwd = util_.md5(password.getText().toString());
        usrnm = username.getText().toString();
        user.setUsername(usrnm);
        user.setPassword(pwd);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<LoginRespon> call = apiService.login(user);
        call.enqueue(new Callback<LoginRespon>() {
            @Override
            public void onResponse(Call<LoginRespon> call, Response<LoginRespon> response) {
                if(response.isSuccessful()){
                    Log.d("ResponseCode",""+response.code());
                    Log.d("ResponseMessage",""+response.message());
                    LoginRespon result = response.body();
                    Log.d("Result",""+result.getData().getToken());
                    //save token
                    session.createLoginSession(usrnm,result.getData().getToken(),pwd);
                    editor.putString("session_key", result.getData().getToken());

                    editor.commit();
                    startMainActivity();

                }else{
                    alert.showAlertDialog(LoginFormActivity.this, "Oo..Something wrong :(", "Username/Password is incorrect", false);
                    Log.d("ResponseCode",""+response.code());
                    Log.d("ResponseMessage",""+response.message());
                    Log.w("Not Success", "Response Body" + response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<LoginRespon> call, Throwable t) {
                Log.w("Failed ", t.toString());
            }
        });
    }

    public static void startLoginActivity(Activity activity) {
        Intent intent = new Intent(activity, LoginFormActivity.class);
        activity.startActivity(intent);
    }

    public void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }



/*
    private void startRegisterActivity() {
        RegisterActivity.startRegActivity(this);
        finish();
    }*/

   /* public void registertv(View v)
    {

        TextView tv= (TextView) findViewById(R.id.register);
        tv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginFormActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }*/

}