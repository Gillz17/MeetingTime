package project.senior.android.com.meetingtime;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomepageActivity extends AppCompatActivity implements View.OnClickListener {
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAnalytics mFirebaseAnalytics;
    private String UUID;
    private CalendarView calendar;
    private Button addEvent;
    private RecyclerView calendarEvents;
    private ListView groupList;
    private RecyclerView upcomingList;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        addEvent = (Button) findViewById(R.id.add_calendar_event);
        calendar = (CalendarView) findViewById(R.id.calendarView);
        calendarEvents = (RecyclerView) findViewById(R.id.RecyclerView);
        groupList = (ListView) findViewById(R.id.list_groups);
        upcomingList = (RecyclerView) findViewById(R.id.RecyclerViewUpcoming);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UUID = user.getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mRef = database.getReference("UUID");

        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
                                groupList.setVisibility(View.VISIBLE);
                                upcomingList.setVisibility(View.GONE);
                                calendar.setVisibility(View.GONE);
                                addEvent.setVisibility(View.GONE);
                                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME,"groups");
                                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE,
                                        "Group View");
                                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.
                                        SELECT_CONTENT, bundle);
                                break;
                            case R.id.action_schedules:
                                groupList.setVisibility(View.GONE);
                                upcomingList.setVisibility(View.GONE);
                                calendar.setVisibility(View.VISIBLE);
                                addEvent.setVisibility(View.VISIBLE);
                                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME,"Calendar");
                                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE,
                                        "Calendar View");
                                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.
                                        SELECT_CONTENT, bundle);
                                break;
                            case R.id.action_upcoming:
                                groupList.setVisibility(View.GONE);
                                upcomingList.setVisibility(View.VISIBLE);
                                calendar.setVisibility(View.GONE);
                                addEvent.setVisibility(View.VISIBLE);
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