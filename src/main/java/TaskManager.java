import java.sql.*;
import static spark.Spark.*;

/**
 * Created by reclaimer on 5/15/16.
 */


public class TaskManager {
    public static void main(String[] args) {

        // -------------------------------------------TESTING-----------------------------------------------------------
        //Create variables for access to local host
        String dbURL = "jdbc:mysql://localhost:3306/TaskDB?autoReconnect=true&useSSL=false";
        String username = "root";
        String password = "---";

        //Attempt to connect to local host
        try (Connection conn = DriverManager.getConnection(dbURL, username, password)) {

            // Check for connection
            if (conn != null) {
                System.out.println("Connected.");

                //Create variables for prepared statement
                String sqlInsert = "INSERT INTO tasks (task_name) VALUES (?)";
                String sqlDel = "DELETE FROM tasks WHERE task_id = (?)";
                String sqlUpdate = "UPDATE tasks SET task_name = (?) WHERE task_id = (?)";

                //Update table
                updateTask(sqlDel, "Get this demo working", Statement.RETURN_GENERATED_KEYS, conn);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Breakout into seperate methods; interface
    public static void updateTask(String sql, String task, int taskID, Connection conn){
        //Initialize prepared statement
        try (PreparedStatement statement = conn.prepareStatement(sql, taskID)) {
            //Update table initialize inserted rows variable
            statement.setString(1, task);
            int rowsInserted = statement.executeUpdate();

            if(rowsInserted > 0){
                System.out.println("Success");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
