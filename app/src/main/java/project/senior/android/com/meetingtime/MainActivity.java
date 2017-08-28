package project.senior.android.com.meetingtime;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;

public class MainActivity extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;
    private TextView group;
    private CalendarView calendar;
    private TextView upcoming;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        group = (TextView) findViewById(R.id.text_groups);
        calendar = (CalendarView) findViewById(R.id.calendarView);
        upcoming = (TextView) findViewById(R.id.text_upcoming);

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
                                calendar.setVisibility(View.GONE);
                                upcoming.setVisibility(View.GONE);
                                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME,"groups");
                                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Group View");
                                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.
                                        SELECT_CONTENT, bundle);
                                break;
                            case R.id.action_schedules:
                                group.setVisibility(View.GONE);
                                calendar.setVisibility(View.VISIBLE);
                                upcoming.setVisibility(View.GONE);
                                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME,"Calendar");
                                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Calendar View");
                                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.
                                        SELECT_CONTENT, bundle);
                                break;
                            case R.id.action_upcoming:
                                group.setVisibility(View.GONE);
                                calendar.setVisibility(View.GONE);
                                upcoming.setVisibility(View.VISIBLE);
                                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME,"Upcoming");
                                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Upcoming View");
                                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.
                                        SELECT_CONTENT, bundle);
                                break;
                        }
                        return true;
                    }
                });
    }
}