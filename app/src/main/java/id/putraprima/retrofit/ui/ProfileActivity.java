package id.putraprima.retrofit.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import id.putraprima.retrofit.R;
import id.putraprima.retrofit.api.helper.ServiceGenerator;
import id.putraprima.retrofit.api.models.Data;
import id.putraprima.retrofit.api.models.UserInfo;
import id.putraprima.retrofit.api.services.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {
    TextView txtEmail, txtName;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        context = getApplicationContext();
        txtEmail = findViewById(R.id.txtEmail);
        txtName = findViewById(R.id.txtName);
        getMe();
    }

    public static String token;
    public static final String SHARED_PREFS = "sharedPrefs";
    private void getMe(){
        SharedPreferences preferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        ApiInterface service = ServiceGenerator.createService(ApiInterface.class);
        Call<Data<UserInfo>> call = service.doMe("Bearer" + preferences.getString(token,""));
        call.enqueue(new Callback<Data<UserInfo>>() {
            @Override
            public void onResponse(Call<Data<UserInfo>> call, Response<Data<UserInfo>> response) {
                Data<UserInfo> info = response.body();
                Toast.makeText(ProfileActivity.this, info.getData().getEmail(), Toast.LENGTH_SHORT).show();
                txtEmail.setText(info.getData().getEmail());
                txtEmail.setVisibility(View.VISIBLE);
                txtName.setText(info.getData().getName());
                txtName.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<Data<UserInfo>> call, Throwable t) {

            }
        });

    }

    public void handleUpdate(View view) {
        Intent i = new Intent(ProfileActivity.this, UpdateProfileActivity.class);
        startActivity(i);
    }

    public void handleLogout(View view) {
        Intent i = new Intent(ProfileActivity.this, MainActivity.class);
        startActivity(i);
    }

    public void handleUpdatePass(View view) {
        Intent i = new Intent(ProfileActivity.this, UpdatePasswordActivity.class);
        startActivity(i);
    }
}
