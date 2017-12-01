package project.senior.android.com.meetingtime;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TimeSelectionActivity extends AppCompatActivity {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference groupRef = database.getReference().child("groups");
    DatabaseReference userRef = database.getReference().child("users");
    DatabaseReference eventRef = database.getReference().child("events");

    private EditText title;
    private ListView timeList;
    List<String> listAvailTimes;
    ArrayList<String> groupMembers = new ArrayList<String>();
    private EditText location;
    private RadioGroup rg;

    private String groupName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_time);

        //Needed to get the name of the group from the previous activity
        groupName = getIntent().getExtras().getString("Group");

        title = (EditText) findViewById(R.id.tfTitle);
        timeList = (ListView) findViewById(R.id.lvTimes);
        location = (EditText) findViewById(R.id.tfLocation);
        rg = (RadioGroup) findViewById(R.id.rg);

        listAvailTimes = new ArrayList<>();

        //Gets all of the members of the group and
        // starts the process of getting the users
        //events to compare the times.
        getGroupMembers();


        //Random Times to test with
        listAvailTimes.add("8:00AM - 9:00AM");
        listAvailTimes.add("1:00PM - 2:30PM");
        listAvailTimes.add("10:00AM - 11:15AM");

        updateTimesList(listAvailTimes);

        timeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                final String time = (String) timeList.getItemAtPosition(position);
                Toast.makeText(TimeSelectionActivity.this, time, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void updateTimesList(List<String> times){
        ListAdapter adapter = new ArrayAdapter<>(TimeSelectionActivity.this,
                android.R.layout.simple_list_item_1, times);
        timeList.setAdapter(adapter);
    }

    public void getGroupMembers(){
        groupRef.child(groupName).child("members").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dsp : dataSnapshot.getChildren()){
                    groupMembers.add(String.valueOf(dsp.getValue()));
                    //just for testing
                    for (String data:groupMembers){
                        Log.d("Members", groupMembers.toString());
                        //Need to get the groupMembers UUID for their events.
                        getGroupMembersEvents(data);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getGroupMembersEvents(final String email){
        //Get the email from the list and find the associated UUID
        Log.d("Hello",email);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot user : dataSnapshot.getChildren()){
                    HashMap<String, String> value = (HashMap<String, String>) user.getValue();
                    String compEmail = value.get("email");
                    if (compEmail.equals(email)){
                        String UUID = value.get("UUID");
                        getUsersEvents(UUID);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getUsersEvents(String UUID){
        eventRef.child(UUID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}