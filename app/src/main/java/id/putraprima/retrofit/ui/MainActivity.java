package id.putraprima.retrofit.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.startapp.android.publish.adsCommon.StartAppSDK;

import net.khirr.android.privacypolicy.PrivacyPolicyDialog;

import id.putraprima.retrofit.R;
import id.putraprima.retrofit.api.helper.ServiceGenerator;
import id.putraprima.retrofit.api.models.ApiError;
import id.putraprima.retrofit.api.models.AppVersion;
import id.putraprima.retrofit.api.models.ErrorUtils;
import id.putraprima.retrofit.api.models.LoginRequest;
import id.putraprima.retrofit.api.models.LoginResponse;
import id.putraprima.retrofit.api.services.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    public static final String SHARED_PREFS = "sharedPrefs";
    private TextView appName, appVersion;
    private EditText edtEmail, edtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PrivacyPolicyDialog dialog = new PrivacyPolicyDialog(this, "https://localhost/terms", "https://localhost/privacy");
        StartAppSDK.init(this, "203751826", true);
        setContentView(R.layout.activity_main);
        edtEmail = findViewById(R.id.mainEdtEmail);
        edtPassword = findViewById(R.id.mainEdtPassword);
        setupLayout();
        checkAppVersion();
        final Intent intent = new Intent(this, MainActivity.class);
        dialog.addPoliceLine("This application uses a unique user identifier for advertising purposes, it is shared with third-party companies.");
        dialog.addPoliceLine("This application sends error reports, installation and send it to a server of the Fabric.io company to analyze and process it.");
        dialog.addPoliceLine("This application requires internet access and must collect the following information: Installed applications and history of installed applications, ip address, unique installation id, token to send notifications, version of the application, time zone and information about the language of the device.");
        dialog.addPoliceLine("All details about the use of data are available in our Privacy Policies, as well as all Terms of Service links below.");
        dialog.show();
        dialog.setTitleTextColor(Color.parseColor("#222222"));
        dialog.setAcceptButtonColor(ContextCompat.getColor(this, R.color.colorAccent));
        dialog.setOnClickListener(new PrivacyPolicyDialog.OnClickListener() {
            @Override
            public void onAccept(boolean isFirstTime) {
                Log.e("MainActivity", "Policies accepted");
                startActivity(intent);
                finish();
            }
            @Override
            public void onCancel() {
                Log.e("MainActivity", "Policies not accepted");
                finish();
            }

        });
    }

    private void setupLayout(){
        appName = findViewById(R.id.mainTxtAppName);
        appVersion = findViewById(R.id.mainTxtAppVersion);
    }

    private void checkAppVersion(){
        ApiInterface service = ServiceGenerator.createService(ApiInterface.class);
        Call<AppVersion> call = service.getAppVersion();
        call.enqueue(new Callback<AppVersion>(){

            @Override
            public void onResponse(Call<AppVersion> call, Response<AppVersion> response) {
                AppVersion app = response.body();
                appName.setText(app.getApp());
                appVersion.setText(app.getVersion());
            }
            @Override
            public void onFailure(Call<AppVersion> call, Throwable t) {
                Toast.makeText(MainActivity.this, "failed to connect server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static String token; //variable untuk menyimpan token yang didapat dari server
    public void handleLogin(View view){
        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();
        LoginRequest login = new LoginRequest(email,password);
        ApiInterface service = ServiceGenerator.createService(ApiInterface.class);
        Call<LoginResponse> call = service.doLogin(login);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse respon = response.body();
                if(response.isSuccessful()){
                    SharedPreferences preferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(token, respon.getToken());
                    editor.apply();
//                    Toast.makeText(MainActivity.this,preferences.getString(token,""),Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(MainActivity.this, ProfileActivity.class);
                    startActivity(i);
                }else if (edtEmail.getText().toString().length()==0 ){
                    ApiError error = ErrorUtils.parseError(response);
                    Toast.makeText(MainActivity.this, error.getError().getEmail().get(0), Toast.LENGTH_SHORT).show();

                }else if (edtPassword.getText().toString().length()==0){
                    ApiError error = ErrorUtils.parseError(response);

                    Toast.makeText(MainActivity.this, error.getError().getPassword().get(0), Toast.LENGTH_SHORT).show();
                }
                else{
                    ApiError error = ErrorUtils.parseError(response);
                    Toast.makeText(MainActivity.this, error.getError().getEmail().get(0), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "login is not correct", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void moveRegisterActivity(View view) {
        Intent i = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(i);
    }
}
