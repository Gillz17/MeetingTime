package project.senior.android.com.meetingtime;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class EventCreationActivity extends AppCompatActivity{
    private EditText title;
    private TextView date;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TextView startTime;
    private TimePickerDialog.OnTimeSetListener mStartOnTimeSetListener;
    private TextView endTime;
    private TimePickerDialog.OnTimeSetListener mEndOnTimeSetListener;
    private EditText location;
    private RadioButton radioColorButton;
    private Button createEvent;
    private RadioGroup rg;

    private String pickedDate;
    private String pickedStartTime;
    private String pickedEndTime;
    private String color;

    private DatabaseReference mEvents;


    @Override
    protected void onCreate(final Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        title = (EditText) findViewById(R.id.tfTitle);
        date = (TextView) findViewById(R.id.tvDate);
        startTime = (TextView) findViewById(R.id.tvStartTime);
        endTime = (TextView) findViewById(R.id.tvEndTime);
        location = (EditText) findViewById(R.id.tfLocation);
        rg = (RadioGroup) findViewById(R.id.rg);
        createEvent = (Button) findViewById(R.id.bCreateEvent);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String UUID = user.getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mRef = database.getReference("users");

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        EventCreationActivity.this,
                        android.R.style.ThemeOverlay_Material_Dialog_Alert,
                        mDateSetListener,
                        year,month,day);
                dialog.show();
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month +1;

                String newDate = month + "/" + day + "/" + year;
                date.setText(newDate);
                pickedDate = newDate;
            }
        };
        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int hour = cal.get(Calendar.HOUR_OF_DAY);
                int minute = cal.get(Calendar.MINUTE);

                boolean is24HourView = false;

                TimePickerDialog dialog = new TimePickerDialog(
                        EventCreationActivity.this,
                        android.R.style.ThemeOverlay_Material_Dialog_Alert,
                        mStartOnTimeSetListener,
                        hour, minute, is24HourView);
                dialog.show();
            }
        });
        mStartOnTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                String AmOrPM = "AM";
                if(hour == 0){
                    hour = 12;
                    AmOrPM = "AM";
                }
                if(hour > 11){
                    hour = hour - 12;
                    AmOrPM = "PM";
                }
                String minutes;
                if(minute == 0){
                    minutes = "00";
                }else if(minute < 10) {
                    minutes = "0" + minute;
                }else{
                    minutes = String.valueOf(minute);
                }
                String time = hour + ":" + minutes + AmOrPM;
                startTime.setText(time);
                pickedStartTime = time;
            }
        };
        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int hour = cal.get(Calendar.HOUR_OF_DAY);
                int minute = cal.get(Calendar.MINUTE);

                boolean is24HourView = false;

                TimePickerDialog dialog = new TimePickerDialog(
                        EventCreationActivity.this,
                        android.R.style.ThemeOverlay_Material_Dialog_Alert,
                        mEndOnTimeSetListener,
                        hour, minute, is24HourView);
                dialog.show();
            }
        });
        mEndOnTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                String AmOrPM = "AM";
                if(hour == 0){
                    hour = 12;
                    AmOrPM = "AM";
                }
                if(hour > 11){
                    hour = hour - 12;
                    AmOrPM = "PM";
                }
                String minutes;
                if(minute == 0){
                    minutes = "00";
                }else if(minute < 10) {
                    minutes = "0" + minute;
                }else{
                    minutes = String.valueOf(minute);
                }
                String time = hour + ":" + minutes + AmOrPM;
                endTime.setText(time);
                pickedEndTime = time;
            }
        };

        createEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = title.getText().toString().trim();
                final String loca = location.getText().toString().trim();

                int selectedId = rg.getCheckedRadioButtonId();
                radioColorButton = (RadioButton) findViewById(selectedId);
                if(radioColorButton == findViewById(R.id.rbRed)){
                    color = "red";
                }else if(radioColorButton == findViewById(R.id.rbOrange)){
                    color = "orange";
                }else if(radioColorButton == findViewById(R.id.rbYellow)){
                    color = "yellow";
                }else if(radioColorButton == findViewById(R.id.rbGreen)){
                    color = "green";
                }else if(radioColorButton == findViewById(R.id.rbBlue)){
                    color = "blue";
                }else if(radioColorButton == findViewById(R.id.rbPurple)){
                    color = "purple";
                }

                createNewEvent(UUID, name, pickedDate, pickedStartTime, pickedEndTime, loca, color);
                Intent Return = new Intent(EventCreationActivity.this,
                        HomepageActivity.class);
                EventCreationActivity.this.startActivity(Return);
            }
        });
    }
    private void createNewEvent(String UUID, String name, String pickedDate, String pickedStartTime,
                                String pickedEndTime, String loca, String color){
        Event event = new Event(name, pickedDate, pickedStartTime, pickedEndTime, color, loca);
        mEvents = FirebaseDatabase.getInstance().getReference("users")
                .child("User UUID = " + UUID);
        mEvents.child("events").push().setValue(event);
    }
}