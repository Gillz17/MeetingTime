package project.senior.android.com.meetingtime;

import java.util.List;

public class Group {
    private String title;
    private String owner;
    private List<String> members;

    public Group (String title, String owner, List<String> members){
        this.title = title;
        this.owner = owner;
        this.members = members;
    }
    public Group(String title, String owner){
        this.title = title;
        this.owner = owner;
    }

    public String getTitle() {
        return title;
    }
    public List<String> getMembers() {
        return members;
    }
    public String getOwner() {
        return owner;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setOwner(String owner) {
        this.owner = owner;
    }
}
