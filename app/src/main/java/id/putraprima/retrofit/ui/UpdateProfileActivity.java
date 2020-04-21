package id.putraprima.retrofit.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.AdRequest;

import androidx.appcompat.app.AppCompatActivity;

import id.putraprima.retrofit.R;
import id.putraprima.retrofit.api.helper.ServiceGenerator;
import id.putraprima.retrofit.api.models.ApiError;
import id.putraprima.retrofit.api.models.Data;
import id.putraprima.retrofit.api.models.ErrorUtils;
import id.putraprima.retrofit.api.models.ProfileRequest;
import id.putraprima.retrofit.api.models.ProfileResponse;
import id.putraprima.retrofit.api.services.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateProfileActivity extends AppCompatActivity {
    private static final Object AD_UNIT_ID = "ca-app-pub-7195319550073101/9004812509";
    EditText edtUsername, edtEmail;

    private InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

//        MobileAds.initialize(this, new OnInitializationCompleteListener() {
//            @Override
//            public void onInitializationComplete(InitializationStatus initializationStatus) {
//            }
//        });
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-2969005929544887~5562059812");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
//        mInterstitialAd.setAdListener(new AdListener(){
//            @Override
//            public void onAdLoaded(){
//                mInterstitialAd.show();
//            }
//        });
        edtUsername = findViewById(R.id.edtUsername);
        edtEmail = findViewById(R.id.edtEmail);
    }

    public static String token;
    public static final String SHARED_PREFS = "sharedPrefs";
    public void handleUpdate(View view) {
        String name = edtUsername.getText().toString();
        String email = edtEmail.getText().toString();
        SharedPreferences preferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        ProfileRequest pr = new ProfileRequest(email,name);
        ApiInterface service = ServiceGenerator.createService(ApiInterface.class);
        Call<Data<ProfileResponse>> call = service
                .doUpdProf("Bearer" + preferences.getString(token,""),pr);
        call.enqueue(new Callback<Data<ProfileResponse>>() {
            @Override
            public void onResponse(Call<Data<ProfileResponse>> call, Response<Data<ProfileResponse>> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(UpdateProfileActivity.this, "Berhasil mengubah profile ", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(UpdateProfileActivity.this, ProfileActivity.class);
                    startActivity(i);
                } else if(name.length() == 0){
                    ApiError error = ErrorUtils.parseError(response);
                    edtUsername.setError(error.getError().getName().get(0));
                }else if(email.length() == 0){
                    ApiError error = ErrorUtils.parseError(response);
                    edtEmail.setError(error.getError().getEmail().get(0));
                }else{
                    ApiError error = ErrorUtils.parseError(response);
                    edtUsername.setError(error.getError().getName().get(0));
                    edtEmail.setError(error.getError().getEmail().get(0));
                }
            }

            @Override
            public void onFailure(Call<Data<ProfileResponse>> call, Throwable t) {
                Toast.makeText(UpdateProfileActivity.this, "Gagal",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
