package project.senior.android.com.meetingtime;

import java.util.Date;
import java.util.List;

/**
 * Created by NCASE on 10/29/2017.
 */

public class Event {
    private String title;
    private String startTime;
    private String endTime;
    private String location;
    private String eventColor;
    private String date;
    private List<Event> mEvents;

    public Event(String title,
                  String date, String startTime,
                  String endTime, String eventColor){
        this.title = title;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.eventColor = eventColor;
    }

    public Event(String title,
                 String date, String startTime,
                 String endTime, String eventColor,
                 String location){
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
        this.eventColor = eventColor;
        this.location = location;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle(){
        return title;
    }
    public String  getDate(){
        return date;
    }
    public String  getStartTime(){
        return startTime;
    }
    public String  getEndTime(){
        return endTime;
    }
    public String getEventColor(){
        return eventColor;
    }
    public String getLocation(){return location;}
    public List<Event> getEvents(){
        return mEvents;
    }
}

