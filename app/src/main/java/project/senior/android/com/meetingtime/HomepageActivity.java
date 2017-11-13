package project.senior.android.com.meetingtime;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.api.services.calendar.Calendar;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class HomepageActivity extends AppCompatActivity implements View.OnClickListener {
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAnalytics mFirebaseAnalytics;
    private CalendarCustomView calendar;
    private TextView group;
    private TextView upcoming;
    private Button addEvent;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        group = (TextView) findViewById(R.id.text_groups);
        upcoming = (TextView) findViewById(R.id.text_upcoming);
        addEvent = (Button) findViewById(R.id.add_calendar_event);
        calendar = (CalendarCustomView) findViewById(R.id.custom_calendar);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String UUID = user.getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mRef = database.getReference("users");

        addEvent.setOnClickListener(this);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Bundle bundle = new Bundle();
                        switch (item.getItemId()) {
                            case R.id.action_groups:
                                group.setVisibility(View.VISIBLE);
                                upcoming.setVisibility(View.GONE);
                                calendar.setVisibility(View.GONE);
                                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME,"groups");
                                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE,
                                        "Group View");
                                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.
                                        SELECT_CONTENT, bundle);
                                break;
                            case R.id.action_schedules:
                                group.setVisibility(View.GONE);
                                upcoming.setVisibility(View.GONE);
                                calendar.setVisibility(View.VISIBLE);
                                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME,"Calendar");
                                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE,
                                        "Calendar View");
                                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.
                                        SELECT_CONTENT, bundle);
                                break;
                            case R.id.action_upcoming:
                                group.setVisibility(View.GONE);
                                upcoming.setVisibility(View.VISIBLE);
                                calendar.setVisibility(View.GONE);
                                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME,"Upcoming");
                                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE,
                                        "Upcoming View");
                                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.
                                        SELECT_CONTENT, bundle);
                                break;
                        }
                        return true;
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.actions_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.logout:
                signOut();
                return true;
            case R.id.delete:
                deleteAccount();
                return true;
        }
        return true;
    }

    @Override
    public void onClick(View view){
        if(view == addEvent){
            Intent addEventIntent = new Intent(HomepageActivity.this,
                    EventCreationActivity.class);
            HomepageActivity.this.startActivity(addEventIntent);
        }
    }

    private void signOut(){
        startActivity(new Intent(getApplicationContext(),LoginActivity.class));

    }
    private void deleteAccount(){
        //Need way to delete account either through Firebase or Google
        //based on how the user signed in.
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }
    @Override
    //Needed to protect the login feature
    //If the user enters the app and signs out they would be
    //able to enter again by pressing the back button
    //But with this they cannot do that
    public void onBackPressed() {
    }
}