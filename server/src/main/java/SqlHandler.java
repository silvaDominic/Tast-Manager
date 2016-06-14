import java.sql.*;
import java.sql.Date;
import java.util.ArrayList;

/**
 * Created by reclaimer on 6/6/16.
 */
public class SqlHandler implements TaskManager {
    private String dbURL;
    private String username;
    private String password;
    private static final String selectTask = "SELECT * FROM tasks WHERE id = (?)";
    private static final String selectAllTasks = "SELECT * FROM tasks";
    private static final String changeTaskName = "UPDATE tasks SET name = (?) WHERE id = (?)";
    private static final String changeTargetDate = "UPDATE tasks SET target_date = (?) WHERE id = (?)";
    private static final String changeTaskStatus = "UPDATE tasks SET completed = (?) WHERE id = (?)";
    private static final String createTask = "INSERT INTO tasks (name, target_date, completed) VALUES (?, ?, ?)";
    private static final String deleteTask = "DELETE FROM tasks WHERE id = (?)";

    public SqlHandler(String dbURL, String username, String password){
        this.dbURL = dbURL;
        this.username = username;
        this.password = password;
    }

    @Override
    public void changeTaskName(int id, String newName) {
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
    public void changeTaskDate(int id, Date newDate) {
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
    public void createTask(int id, String newTask, Date targetDate) {
        try (Connection conn = DriverManager.getConnection(this.dbURL, this.username, this.password)) {
            if (conn != null) {
                PreparedStatement statement = conn.prepareStatement(createTask, id);
                statement.setString(1, newTask);
                statement.setDate(2, targetDate);
                statement.setBoolean(3, false);
                statement.executeUpdate();
                System.out.println("Successfully added task to DB");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void taskStatus(int id, boolean status) {
        try (Connection conn = DriverManager.getConnection(this.dbURL, this.username, this.password)) {
            PreparedStatement statement = conn.prepareStatement(changeTaskStatus, id);
            statement.setBoolean(1, status);
            statement.setInt(2, id);
            statement.executeUpdate();
            System.out.println("Successfully marked task complete in DB");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<Task> getAllTasks(){
        ArrayList<Task> tasks = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(this.dbURL, this.username, this.password)) {
            Statement statement = conn.createStatement();
            ResultSet taskSet = statement.executeQuery(selectAllTasks);
            while(taskSet.next()){
                Task task = new Task(taskSet.getInt(1), taskSet.getString(2), taskSet.getDate(3));
                task.setTargetDate(taskSet.getDate(3));
                tasks.add(task);
            }
            System.out.println("Successfully retrieved all tasks");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    @Override
    public Task getTask(int id){
        Task task = null;
        try (Connection conn = DriverManager.getConnection(this.dbURL, this.username, this.password)) {
            PreparedStatement statement = conn.prepareStatement(selectTask, id);
            statement.setInt(1, id);
            ResultSet taskSet = statement.executeQuery();
            taskSet.next();
            task = new Task(taskSet.getInt(1), taskSet.getString(2), taskSet.getDate(3));
            System.out.println("Successfully retrieved tasks: " + id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return task;
    }
}