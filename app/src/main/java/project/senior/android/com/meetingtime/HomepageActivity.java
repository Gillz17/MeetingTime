package project.senior.android.com.meetingtime;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomepageActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAnalytics mFirebaseAnalytics;
    private String UUID;
    private CalendarView calendar;
    private Button addEvent;
    private ListView calendarEvents;
    private ListView groupList;
    private ListView upcomingList;
    private FloatingActionButton mFloatingActionButton;

    List<String> listUsers;
    List<String> titleList;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference groupRef = database.getReference().child("groups");
    DatabaseReference eventRef = database.getReference().child("events");

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        UUID = user.getUid();

        listUsers = new ArrayList<>();
        titleList = new ArrayList<>();

        addEvent = (Button) findViewById(R.id.add_calendar_event);
        calendar = (CalendarView) findViewById(R.id.calendarView);
        calendarEvents = (ListView) findViewById(R.id.ListEvents);
        groupList = (ListView) findViewById(R.id.list_groups);
        upcomingList = (ListView) findViewById(R.id.ListUpcoming);
        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.fabCreate);

        addEvent.setOnClickListener(this);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addGroupIntent = new Intent(HomepageActivity.this,
                        GroupCreationActivity.class);
                HomepageActivity.this.startActivity(addGroupIntent);
            }
        });

        getCalendarEvents(UUID);

        groupList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final String groupName = (String) groupList.getItemAtPosition(position);
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(HomepageActivity.this,
                            android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(HomepageActivity.this);
                }
                builder.setTitle("Schedule Meeting?")
                        .setMessage("Do you want to schedule a meeting for this group?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with scheduling
                                Intent selectTimeIntent = new Intent(HomepageActivity.this,
                                        TimeSelectionActivity.class);
                                //Needed to send the name of the group to the other activity
                                selectTimeIntent.putExtra("Group", groupName);
                                HomepageActivity.this.startActivity(selectTimeIntent);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Bundle bundle = new Bundle();
                        switch (item.getItemId()) {
                            case R.id.action_groups:
                                getUserGroups(UUID);
                                //change views to group list
                                groupList.setVisibility(View.VISIBLE);
                                upcomingList.setVisibility(View.GONE);
                                calendar.setVisibility(View.GONE);
                                addEvent.setVisibility(View.GONE);
                                calendarEvents.setVisibility(View.GONE);
                                mFloatingActionButton.setVisibility(View.VISIBLE);
                                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME,"groups");
                                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE,
                                        "Group View");
                                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.
                                        SELECT_CONTENT, bundle);
                                break;
                            case R.id.action_schedules:
                                //change views to standard calendar
                                groupList.setVisibility(View.GONE);
                                upcomingList.setVisibility(View.GONE);
                                calendar.setVisibility(View.VISIBLE);
                                addEvent.setVisibility(View.VISIBLE);
                                calendarEvents.setVisibility(View.VISIBLE);
                                mFloatingActionButton.setVisibility(View.GONE);
                                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME,"Calendar");
                                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE,
                                        "Calendar View");
                                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.
                                        SELECT_CONTENT, bundle);
                                getCalendarEvents(UUID);
                                break;
                            case R.id.action_upcoming:
                                //Change view to list of upcoming events
                                groupList.setVisibility(View.GONE);
                                upcomingList.setVisibility(View.VISIBLE);
                                calendar.setVisibility(View.GONE);
                                addEvent.setVisibility(View.VISIBLE);
                                calendarEvents.setVisibility(View.GONE);
                                mFloatingActionButton.setVisibility(View.GONE);
                                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME,"Upcoming");
                                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE,
                                        "Upcoming View");
                                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.
                                        SELECT_CONTENT, bundle);
                                getUpcomingEvents();
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

    public void getCalendarEvents(String UUID){
        eventRef.child(UUID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                titleList.clear();
                for(DataSnapshot child : dataSnapshot.getChildren()) {
                    HashMap<String, String> value = (HashMap<String, String>) child.getValue();
                    String name = value.get("title");
                    titleList.add(name);

                    //Unused to get the full event details
                    String date = value.get("date");
                    String startTime = value.get("startTime");
                    String endTime = value.get("endTime");
                    String location = value.get("location");
                    String color = value.get("eventColor");
                }
                ListAdapter adapter = new ArrayAdapter<>(HomepageActivity.this,
                        android.R.layout.simple_list_item_1, titleList);
                calendarEvents.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getUserGroups(String UUID){
        groupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listUsers.clear();
                for(DataSnapshot child : dataSnapshot.getChildren()) {
                   HashMap<String, String> value = (HashMap<String, String>)child.getValue();
                   String title = value.get("title");
                   listUsers.add(title);
                }

                ListAdapter adapter = new ArrayAdapter<>(HomepageActivity.this,
                        android.R.layout.simple_list_item_1, listUsers);
                groupList.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void getUpcomingEvents(){
        eventRef.child(UUID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                titleList.clear();
                for(DataSnapshot child : dataSnapshot.getChildren()) {
                    HashMap<String, String> value = (HashMap<String, String>) child.getValue();
                    String name = value.get("title");
                    titleList.add(name);

                    //Unused to get the full event details
                    String date = value.get("date");
                    String startTime = value.get("startTime");
                    String endTime = value.get("endTime");
                    String location = value.get("location");
                    String color = value.get("eventColor");
                }
                ListAdapter adapter = new ArrayAdapter<>(HomepageActivity.this,
                        android.R.layout.simple_list_item_1, titleList);
                upcomingList.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}