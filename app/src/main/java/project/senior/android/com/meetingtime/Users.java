package project.senior.android.com.meetingtime;

/**
 * Created by zmc17 on 10/29/2017.
 */

public class Users {
    public String name;
    public String email;
    public String UUID;

    public Users() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }
    public Users(String email){
        this.email = email;
    }

    public Users(String email, String name, String UUID) {
        this.name = name;
        this.email = email;
        this.UUID = UUID;
    }
}
