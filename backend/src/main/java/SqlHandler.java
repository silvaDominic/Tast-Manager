import java.sql.*;
import java.sql.Date;

/**
 * Created by reclaimer on 6/6/16.
 */
public class SqlHandler implements TaskManager {
    private String dbURL;
    private String username;
    private String password;
    private static final String changeTaskName = "UPDATE tasks SET task_name = (?) WHERE task_id = (?)";
    private static final String changeTargetDate = "UPDATE tasks SET target_date = (?) WHERE task_id = (?)";
    private static final String markTaskComplete = "UPDATE tasks SET completed = (?) WHERE task_id = (?)";
    private static final String createTask = "INSERT INTO tasks (task_name, completed) VALUES (?, ?)";
    private static final String deleteTask = "DELETE FROM tasks WHERE task_id = (?)";

    public SqlHandler(String dbURL, String username, String password){
        this.dbURL = dbURL;
        this.username = username;
        this.password = password;
    }

    @Override
    public void changeTaskName(String newName, int id) {
        try (Connection conn = DriverManager.getConnection(this.dbURL, this.username, this.password)) {
            PreparedStatement statement = conn.prepareStatement(changeTaskName, id);
            statement.setString(1, newName);
            statement.setInt(2, id);
            statement.executeUpdate();
            System.out.println("Successfully changed name of task in DB");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void changeTaskDate(Date newDate, int id) {
        try (Connection conn = DriverManager.getConnection(this.dbURL, this.username, this.password)) {
            PreparedStatement statement = conn.prepareStatement(changeTargetDate, id);
            statement.setDate(1, newDate);
            statement.setInt(2, id);
            statement.executeUpdate();
            System.out.println("Successfully changed target date of task in DB");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteTask(int id) {
        try (Connection conn = DriverManager.getConnection(this.dbURL, this.username, this.password)) {
            PreparedStatement statement = conn.prepareStatement(deleteTask, id);
            statement.setInt(1, id);
            statement.executeUpdate();
            System.out.println("Successfully deleted task from DB");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void createTask(String newTask, int id) {
        try (Connection conn = DriverManager.getConnection(this.dbURL, this.username, this.password)) {
            if (conn != null) {
                PreparedStatement statement = conn.prepareStatement(createTask, id);
                statement.setString(1, newTask);
                statement.setBoolean(2, false);
                statement.executeUpdate();
                System.out.println("Successfully added task to DB");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void markComplete(int id) {
        try (Connection conn = DriverManager.getConnection(this.dbURL, this.username, this.password)) {
            PreparedStatement statement = conn.prepareStatement(markTaskComplete, id);
            statement.setBoolean(1, true);
            statement.setInt(2, id);
            statement.executeUpdate();
            System.out.println("Successfully marked task complete in DB");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
