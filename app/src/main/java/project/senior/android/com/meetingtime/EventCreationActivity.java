package project.senior.android.com.meetingtime;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Time;
import java.util.Calendar;

/**
 * Created by NCASE on 10/29/2017.
 */

public class EventCreationActivity extends AppCompatActivity{
    private EditText title;
    private TextView date;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TextView startTime;
    private TimePickerDialog.OnTimeSetListener mStartOnTimeSetListener;
    private TextView endTime;
    private TimePickerDialog.OnTimeSetListener mEndOnTimeSetListener;
    private EditText location;
    private RadioButton red;
    private RadioButton blue;
    private RadioButton green;
    private RadioButton yellow;
    private RadioButton purple;
    private RadioButton orange;
    private RadioButton pink;
    private RadioButton brown;
    private Button createEvent;

    private String pickedDate;
    private String pickedStartTime;
    private String pickedEndTime;

    @Override
    protected void onCreate(final Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        title = (EditText) findViewById(R.id.tfTitle);
        date = (TextView) findViewById(R.id.tvDate);
        startTime = (TextView) findViewById(R.id.tvStartTime);
        endTime = (TextView) findViewById(R.id.tvEndTime);
        location = (EditText) findViewById(R.id.tfLocation);
        red = (RadioButton) findViewById(R.id.rbRed);
        blue = (RadioButton) findViewById(R.id.rbBlue);
        green = (RadioButton) findViewById(R.id.rbGreen);
        yellow = (RadioButton) findViewById(R.id.rBYellow);
        purple = (RadioButton) findViewById(R.id.rbPurple);
        orange = (RadioButton) findViewById(R.id.rbOrange);
        pink = (RadioButton) findViewById(R.id.rbPink);
        brown = (RadioButton) findViewById(R.id.rbBrown);
        createEvent = (Button) findViewById(R.id.bCreateEvent);

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
                Toast.makeText(EventCreationActivity.this,
                        pickedDate + " / " + pickedStartTime + " / " + pickedEndTime,
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}