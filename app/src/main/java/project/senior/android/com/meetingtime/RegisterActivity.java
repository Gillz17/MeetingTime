package project.senior.android.com.meetingtime;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "Register";

    private EditText tfName;
    private EditText tfEmail;
    private EditText tfPassword;
    private EditText tfPassConfirm;
    private TextView signInLink;
    private Button bRegister;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    private DatabaseReference mUsers;
    private DatabaseReference mEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        tfName = (EditText) findViewById(R.id.tfName);
        tfEmail = (EditText) findViewById(R.id.tfEmail);
        tfPassword = (EditText) findViewById(R.id.tfPassword);
        tfPassConfirm = (EditText) findViewById(R.id.tfPassConfirm);

        signInLink = (TextView) findViewById(R.id.tvSignin);

        bRegister = (Button) findViewById(R.id.bRegister);

        bRegister.setOnClickListener(this);
        signInLink.setOnClickListener(this);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mUsers = FirebaseDatabase.getInstance().getReference("users");
    }

    private void registerUser(){
        final String name = tfName.getText().toString().trim();
        final String email = tfEmail.getText().toString().trim();
        String password = tfPassword.getText().toString().trim();
        String confirm = tfPassConfirm.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            //email is empty
            tfEmail.setError("Enter email");
            return;
        }else{
            tfEmail.setError(null);
        }
        if(!email.contains("@")){
            tfEmail.setError("Enter a valid email");
        }else{
            tfEmail.setError(null);
        }
        if(!email.contains(".com") && !email.contains(".edu") && !email.contains(".org")
                && !email.contains(".gov")  && !email.contains(".net")){
            tfEmail.setError("Enter a valid email");
        }else{
            tfEmail.setError(null);
        }
        if(!confirm.equals(password)){
            tfPassConfirm.setError("Passwords do not match");
        }else{
            tfPassConfirm.setError(null);
        }
        if(TextUtils.isEmpty(password)) {
            //password is empty
            tfPassword.setError("Enter password");
        }else{
            tfPassword.setError(null);
        }
        if(password.length() < 6){
            //Firebase requires a length of 6 characters, otherwise it will not register properly.
            //So the code checks to make sure this is the case
            //if the password is less than 6, the application will inform the user of this requirement.
            tfPassword.setError("Password must be at least 6 characters");
        }else{
            tfPassword.setError(null);
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            String UUID = user.getUid();

                            //add user to the database
                            writeNewUser(email, name, UUID);

                            //create new events list
                            createEventList(UUID);

                            //start homepage activity
                            Intent homepageIntent = new Intent(RegisterActivity.this,
                                    HomepageActivity.class);
                            RegisterActivity.this.startActivity(homepageIntent);
                        }else{
                            //show error message
                            Toast.makeText(RegisterActivity.this,
                                    "Could not register. Please try again.",
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
            Intent signInIntent = new Intent(RegisterActivity.this,
                    LoginActivity.class);
            RegisterActivity.this.startActivity(signInIntent);
        }
    }
    private void writeNewUser(String email, String name, String UUID){
        Users user = new Users(email, name, UUID);
        mUsers.child(UUID).setValue(user);
    }
    private void createEventList(String UUID){
        mEvents = FirebaseDatabase.getInstance().getReference("users")
                .child(UUID);
    }
}
