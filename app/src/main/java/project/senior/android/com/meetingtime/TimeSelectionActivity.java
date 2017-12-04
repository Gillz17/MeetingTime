package project.senior.android.com.meetingtime;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TimeSelectionActivity extends AppCompatActivity {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference groupRef = database.getReference().child("groups");
    DatabaseReference userRef = database.getReference().child("users");
    DatabaseReference eventRef = database.getReference().child("events");
    DatabaseReference timeRef = database.getReference().child("times");

    private EditText title;
    private ListView timeList;
    List<String> listAvailTimes;
    ArrayList<String> groupMembers = new ArrayList<String>();
    private TextView info;
    private EditText location;
    private RadioGroup rg;
    private Button createEventButton;

    private String groupName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_time);

        //Needed to get the name of the group from the previous activity
        groupName = getIntent().getExtras().getString("Group");

        title = (EditText) findViewById(R.id.tfTitle);
        timeList = (ListView) findViewById(R.id.lvTimes);

        info = (TextView) findViewById(R.id.tvInfo);
        location = (EditText) findViewById(R.id.tfLocation);
        rg = (RadioGroup) findViewById(R.id.rg);
        createEventButton = (Button) findViewById(R.id.bAddEvent);

        listAvailTimes = new ArrayList<>();

        //Gets all of the members of the group and
        //starts the process of getting the users
        //events to compare the times.
        getGroupMembers();

        //Gets the list of times that were added to the database inorder to schedule the meetings
        timeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dsp : dataSnapshot.getChildren()){
                    listAvailTimes.add(String.valueOf(dsp.getValue()));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        timeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            String startTime = "";
            String endTime = "";
            int i = 0;
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (i == 0){
                    startTime = (String) timeList.getItemAtPosition(position);
                    i++;
                }else if(i == 1){
                    endTime = (String) timeList.getItemAtPosition(position);
                    i = 0;
                }
                info.setText("New meeting time: " + startTime + " - " + endTime);
            }
        });

        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TimeSelectionActivity.this,"Under Construction",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    public void updateTimesList(List<String> times){
        //Updates the list of available times
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
                        getGroupMembersUUID(data);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("GetGroupMembers", "Error getting the members of the group");
            }
        });
    }

    public void getGroupMembersUUID(final String email){
        final ArrayList<String> UUIDs = new ArrayList<>();
        //Get the email from the list and find the associated UUID
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot user : dataSnapshot.getChildren()){
                    HashMap<String, String> value = (HashMap<String, String>) user.getValue();
                    String compEmail = value.get("email");
                    if (compEmail.equals(email)) {
                        UUIDs.add(value.get("UUID"));
                        getUsersEvents(UUIDs);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getUsersEvents(ArrayList<String> UUIDs){
        for (final String data:UUIDs){
            eventRef.child(data).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot){
                    for(DataSnapshot child : dataSnapshot.getChildren()) {
                        HashMap<String, String> value = (HashMap<String, String>) child.getValue();
                        String name = value.get("title");
                        String date = value.get("date");
                        String startTime = value.get("startTime");
                        String endTime = value.get("endTime");
                        String location = value.get("location");
                        String color = value.get("eventColor");
                        Log.d("Event", name + date + startTime + endTime + location + color );

                        findTimes(startTime, endTime);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("getUserUUID", "Error getting the user " + data + "'s events" );
                }
            });
        }
    }

    public void findTimes (String startTime, String endTime){
        int i = 0;
        int j = 0;

        boolean found = false;
        Iterator<String> iter = listAvailTimes.iterator();
        String curItem="";
        int pos=0;

        while (iter.hasNext())
        {
            pos++;
            curItem =(String) iter .next();
            if (curItem.equals(startTime)) {
                i = pos-1;
                found = true;
            }
            if (curItem.equals(endTime)){
                j = pos-1;
                found = true;
                break;
            }
        }
        if (!found) {
            pos = 0;
            Toast.makeText(TimeSelectionActivity.this, "Time not found", Toast.LENGTH_LONG).show();
        }else {
            ArrayList<String> toRemove = new ArrayList<>();
            for(int x = i; x< j; x++){
                toRemove.add(listAvailTimes.get(x));
            }
            listAvailTimes.removeAll(toRemove);
            //Prints the list of available times
            updateTimesList(listAvailTimes);
        }

    }
}