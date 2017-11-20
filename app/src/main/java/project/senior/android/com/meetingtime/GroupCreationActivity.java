package project.senior.android.com.meetingtime;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

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

/**
 * Created by NCASE on 11/19/2017.
 */

public class GroupCreationActivity extends AppCompatActivity{
    private ListView mListView;
    private Button mCreate;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    final String UUID = user.getUid();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mUsers = database.getReference("users");

    List<String> listUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_groups);

        mListView = (ListView) findViewById(R.id.listUsers);
        mCreate = (Button) findViewById(R.id.bCreateGroup);
        listUsers = new ArrayList<>();

        getUserGroups(UUID);

        mCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ADD TO SEND DATA TO DATABASE
            }
        });
    }


    public void getUserGroups(String UUID) {
        mUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listUsers.clear();
                for(DataSnapshot child : dataSnapshot.getChildren()){
                    HashMap<String, String> value = (HashMap<String, String>)child.getValue();
                    String user = value.get("name");
                    listUsers.add(user);
                }
                ListAdapter adapter = new ArrayAdapter<>(GroupCreationActivity.this,
                        android.R.layout.simple_list_item_1, listUsers);
                mListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
