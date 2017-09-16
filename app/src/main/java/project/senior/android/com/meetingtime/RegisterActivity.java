package project.senior.android.com.meetingtime;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText tfName = (EditText) findViewById(R.id.tfName);
        final EditText tfUsername = (EditText) findViewById(R.id.tfUsername);
        final EditText tfPassword = (EditText) findViewById(R.id.tfPassword);
        final Button bRegister = (Button) findViewById(R.id.bRegister);

        final TextView signinLink = (TextView) findViewById(R.id.tvSignin);

        signinLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signinIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                RegisterActivity.this.startActivity(signinIntent);
            }
        });



    }
}
