import java.sql.*;
import static spark.Spark.*;

/**
 * Created by reclaimer on 5/15/16.
 */


public class TaskManager {
    public static void main(String[] args) {
        // TEST/JDBC Refresher
        // get connection
        try (Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/TaskDB?autoReconnect=true&useSSL=false", "root", "---")){
            // create statement
            Statement statement = myConn.createStatement();

            String sql = "INSERT INTO tasks (task_name) VALUES (?)";
            PreparedStatement preState = myConn.prepareStatement(sql);
            preState.setString(1, "Finish this tutorial");
            int rowsInserted = preState.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Success");
            }
            // execute sql query
            ResultSet myRS = statement.executeQuery("SELECT * FROM tasks");
            // process result set
            while (myRS.next()) {
                System.out.println(myRS.getString("task_id") + myRS.getString("task_name"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
