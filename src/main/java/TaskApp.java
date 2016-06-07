import java.sql.*;
import static spark.Spark.*;

/**
 * Created by reclaimer on 5/15/16.
 */


public class TaskApp {
    public static void main(String[] args) {

        // -------------------------------------------TESTING-----------------------------------------------------------
        //Create variables for access to local host
        String dbURL = "jdbc:mysql://localhost:3306/TaskDB?autoReconnect=true&useSSL=false";
        String username = "root";
        String password = "roo7CLAUD1tis8";

        String testTask1 = "Create me";
        String testTask2 = "Delete me";
        String testTask3 = "Change me";

        SqlHandler sql = new SqlHandler(dbURL, username, password);
        sql.changeTaskName("New name", 3);
    }
}
