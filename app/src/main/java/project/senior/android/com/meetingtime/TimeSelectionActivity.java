package project.senior.android.com.meetingtime;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class TimeSelectionActivity extends AppCompatActivity {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference groupRef = database.getReference().child("groups");
    DatabaseReference userRef = database.getReference().child("users");
    DatabaseReference eventRef = database.getReference().child("events");

    private ListView timeList;
    List<String> listAvailTimes;

    private String UUID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_time);

        timeList = (ListView) findViewById(R.id.lvTimes);

        UUID = user.getUid();

        listAvailTimes = new ArrayList<>();
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
                Intent HomepageIntent = new Intent(TimeSelectionActivity.this,
                        HomepageActivity.class);
                TimeSelectionActivity.this.startActivity(HomepageIntent);
            }
        });
    }

    public void updateTimesList(List<String> times){
        ListAdapter adapter = new ArrayAdapter<>(TimeSelectionActivity.this,
                android.R.layout.simple_list_item_1, times);
        timeList.setAdapter(adapter);
    }
}