/**
 * Created by zmc17 on 10/29/2017.
 */

public class Users {
    public String username;
    public String email;

    public void User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public void User(String username, String email) {
        this.username = username;
        this.email = email;
    }
}
