package project.senior.android.com.meetingtime;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.SearchView;

/**
 * Created by NCASE on 11/19/2017.
 */

public class GroupCreationActivity extends AppCompatActivity{
    private SearchView mSearchView;
    private RecyclerView mRecyclerView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_groups);
    }
}
