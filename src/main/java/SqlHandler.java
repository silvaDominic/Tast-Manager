import java.sql.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

/**
 * Created by reclaimer on 6/6/16.
 */
public class SqlHandler implements TaskManager {
    private String dbURL;
    private String username;
    private String password;
    private Calendar calendar;
    private static final String selectTask = "SELECT * FROM tasks WHERE id = (?)";
    private static final String selectAllTasks = "SELECT * FROM tasks";
    private static final String changeTaskName = "UPDATE tasks SET name = (?) WHERE id = (?)";
    private static final String changeTargetDate = "UPDATE tasks SET target_date = (?) WHERE id = (?)";
    private static final String changeTaskStatus = "UPDATE tasks SET completed = (?) WHERE id = (?)";
    private static final String createTask = "INSERT INTO tasks (id, name, date_created, target_date, completed) VALUES (?, ?, ?, ?, ?)";
    private static final String deleteTask = "DELETE FROM tasks WHERE id = (?)";
    private static final String clearTable = "SELECT * FROM TaskDB.tasks;";

    public SqlHandler(String dbURL, String username, String password){
        this.dbURL = dbURL;
        this.username = username;
        this.password = password;
        this.calendar = Calendar.getInstance();
    }

    @Override
    public void changeTaskName(String id, String newName) {
        try (Connection conn = DriverManager.getConnection(this.dbURL, this.username, this.password)) {
            PreparedStatement statement = conn.prepareStatement(changeTaskName);
            statement.setString(1, newName);
            statement.setString(2, id);
            statement.executeUpdate();
            System.out.println("Successfully changed name of task in DB");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void changeTaskDate(String id, Date newDate) {
        try (Connection conn = DriverManager.getConnection(this.dbURL, this.username, this.password)) {
            PreparedStatement statement = conn.prepareStatement(changeTargetDate);
            statement.setDate(1, newDate);
            statement.setString(2, id);
            statement.executeUpdate();
            System.out.println("Successfully changed target date of task in DB");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteTask(String id) {
        try (Connection conn = DriverManager.getConnection(this.dbURL, this.username, this.password)) {
            PreparedStatement statement = conn.prepareStatement(deleteTask);
            statement.setString(1, id);
            statement.executeUpdate();
            System.out.println("Successfully deleted task from DB");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createTask(String newTask, Date targetDate) {
        try (Connection conn = DriverManager.getConnection(this.dbURL, this.username, this.password)) {
            if (conn != null) {
                PreparedStatement statement = conn.prepareStatement(createTask);
                statement.setString(1, UUID.randomUUID().toString());
                statement.setString(2, newTask);
                statement.setDate(3, new java.sql.Date(calendar.getTime().getTime()));
                statement.setDate(4, targetDate);
                statement.setBoolean(5, false);
                statement.executeUpdate();
                System.out.println("Successfully added task to DB");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void taskStatus(String id, boolean status) {
        try (Connection conn = DriverManager.getConnection(this.dbURL, this.username, this.password)) {
            PreparedStatement statement = conn.prepareStatement(changeTaskStatus);
            statement.setBoolean(1, status);
            statement.setString(2, id);
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
                Task task = new Task(taskSet.getString(1), taskSet.getString(2), taskSet.getDate(3));
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
    public Task getTask(String id){
        Task task = null;
        try (Connection conn = DriverManager.getConnection(this.dbURL, this.username, this.password)) {
            PreparedStatement statement = conn.prepareStatement(selectTask);
            statement.setString(1, id);
            ResultSet taskSet = statement.executeQuery();
            taskSet.next();
            task = new Task(taskSet.getString(1), taskSet.getString(2), taskSet.getDate(3));
            System.out.println("Successfully retrieved tasks: " + id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return task;
    }

    @Override
    public void clearTasks(){
        try (Connection conn = DriverManager.getConnection(this.dbURL, this.username, this.password)) {
            Statement statement = conn.createStatement();
            statement.executeQuery(clearTable);
            System.out.println("Successfully cleared all tasks");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}