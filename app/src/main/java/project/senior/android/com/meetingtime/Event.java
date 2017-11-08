package project.senior.android.com.meetingtime;

import java.util.Date;

/**
 * Created by NCASE on 10/29/2017.
 */

public class Event {
    private String title;
    private int startTime;
    private int endTime;
    private String location;
    private String eventColor;
    private Date date;

    public Event(String title,
                  Date date, int startTime,
                  int endTime, String eventColor){
        this.title = title;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.eventColor = eventColor;
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
        return title;
    }
    public Date getDate(){
        return date;
    }
    public int getStartTime(){
        return startTime;
    }
    public int getEndTime(){
        return endTime;
    }
    public String getEventColor(){
        return eventColor;
    }
    public String getLocation(){return location;}
}

