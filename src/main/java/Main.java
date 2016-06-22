/**
 * Created by reclaimer on 5/15/16.
 */

public class Main {
    public static void main(String[] args) {
        String dbURL = "jdbc:mysql://localhost:3306/TaskDB?autoReconnect=true&useSSL=false";
        String username = "root";
        String password = "roo7CLAUD1tis8";
        new TaskController(dbURL, username, password);
    }
}
