package project.senior.android.com.meetingtime;

import android.content.Intent;
import android.drm.ProcessedData;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener{

    public static final String TAG = "LoginActivity";
    public static final int RC_SIGN_IN = 9001;

    private FirebaseAuth mAuth;
    private String mCustomToken;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText tfUsername = (EditText) findViewById(R.id.tfUsername);
        final EditText tfPassword = (EditText) findViewById(R.id.tfPassword);
        final Button bSignin = (Button) findViewById(R.id.bSignin);
        final TextView registerLink = (TextView) findViewById(R.id.tvRegister);

        findViewById(R.id.google_signin_button).setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);
            }
        });

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result){
        Log.d(TAG, "handleSignInResult:"+ result.isSuccess());
        if(result.isSuccess()){
            GoogleSignInAccount account = result.getSignInAccount();
            Intent homepageIntent = new Intent(LoginActivity.this, HomepageActivity.class);
            LoginActivity.this.startActivity(homepageIntent);
        }else {
            Toast.makeText(LoginActivity.this, "Failed to sign in",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.google_signin_button:
                signIn();
                break;
        }
    }
    private void signIn(){
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG,"onConnectionFailed:" + connectionResult);
    }
}