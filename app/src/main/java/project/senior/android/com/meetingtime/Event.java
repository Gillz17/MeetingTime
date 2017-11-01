package project.senior.android.com.meetingtime;

import java.util.Date;

/**
 * Created by NCASE on 10/29/2017.
 */

public class Event {
    public String title;
    public int startTime;
    public int endTime;
    public String location;
    public String eventColor;
    public Date date;

    public Event(){

    }
    public Event(String title,
                 Date date, int startTime,
                 int endTime, String eventColor,
                 String location){
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
        this.eventColor = eventColor;
        this.location = location;
    }

    public String getTitle(){
        return this.title;
    }
    public Date getDate(){
        return this.date;
    }
    public int getStartTime(){
        return this.startTime;
    }
    public int getEndTime(){
        return this.endTime;
    }
    public String getEventColor(){
        return this.eventColor;
    }
    public String getLocation(){return this.location;}
}

