package project.senior.android.com.meetingtime;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "Register";

    private EditText tfEmail;
    private EditText tfPassword;
    private TextView signInLink;
    private Button bRegister;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        tfEmail = (EditText) findViewById(R.id.tfEmail);
        tfPassword = (EditText) findViewById(R.id.tfPassword);

        signInLink = (TextView) findViewById(R.id.tvSignin);

        bRegister = (Button) findViewById(R.id.bRegister);

        bRegister.setOnClickListener(this);
        signInLink.setOnClickListener(this);
    }

    private void registerUser(){
        String email = tfEmail.getText().toString().trim();
        String password = tfPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            //email is empty
            Toast.makeText(this, "Please enter Email",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(password)) {
            //password is empty
            Toast.makeText(this, "Please enter a Password", Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //start homepage activity
                            Intent homepageIntent = new Intent(RegisterActivity.this, HomepageActivity.class);
                            RegisterActivity.this.startActivity(homepageIntent);
                        }else{
                            //show error message
                            Toast.makeText(RegisterActivity.this,"Could not register. Please try again.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {
        if(view == bRegister){
            registerUser();
        }
        if(view == signInLink){
            //open sign in activity
        }
    }
}
