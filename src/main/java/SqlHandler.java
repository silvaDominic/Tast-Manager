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
    private static final String SELECT_TASK = "SELECT * FROM tasks WHERE id = (?)";
    private static final String SELECT_ALL_TASKS = "SELECT * FROM tasks";
    private static final String CHANGE_TASK_DESCRIPTION = "UPDATE tasks SET description = (?) WHERE id = (?)";
    private static final String CHANGE_TARGET_DATE = "UPDATE tasks SET target_date = (?) WHERE id = (?)";
    private static final String CHANGE_TASK_STATUS = "UPDATE tasks SET completed = (?) WHERE id = (?)";
    private static final String CREATE_TASK = "INSERT INTO tasks (id, description, date_created, target_date, completed) VALUES (?, ?, ?, ?, ?)";
    private static final String DELETE_TASK = "DELETE FROM tasks WHERE id = (?)";
    private static final String CLEAR_TABLE = "SELECT * FROM TaskDB.tasks;";

    public SqlHandler(String dbURL, String username, String password){
        this.dbURL = dbURL;
        this.username = username;
        this.password = password;
        this.calendar = Calendar.getInstance();
    }

    @Override
    public Task createTask(String newTask, Date targetDate) {
        String id = "";
        try (Connection conn = DriverManager.getConnection(this.dbURL, this.username, this.password)) {
            if (conn != null) {
                PreparedStatement statement = conn.prepareStatement(CREATE_TASK);
                id = UUID.randomUUID().toString();
                statement.setString(1, id);
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
        return getTask(id);
    }

    @Override
    public void deleteTask(String id) {
        try (Connection conn = DriverManager.getConnection(this.dbURL, this.username, this.password)) {
            PreparedStatement statement = conn.prepareStatement(DELETE_TASK);
            statement.setString(1, id);
            statement.executeUpdate();
            System.out.println("Successfully deleted task from DB");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clearTasks(){
        try (Connection conn = DriverManager.getConnection(this.dbURL, this.username, this.password)) {
            Statement statement = conn.createStatement();
            statement.executeQuery(CLEAR_TABLE);
            System.out.println("Successfully cleared all tasks");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Task getTask(String id){
        Task task = null;
        try (Connection conn = DriverManager.getConnection(this.dbURL, this.username, this.password)) {
            PreparedStatement statement = conn.prepareStatement(SELECT_TASK);
            statement.setString(1, id);
            ResultSet taskSet = statement.executeQuery();
            taskSet.next();
            task = new Task(taskSet.getString(1), taskSet.getString(2), taskSet.getDate(4), taskSet.getBoolean(5));
            System.out.println("Successfully retrieved tasks: " + id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return task;
    }

    @Override
    public ArrayList<Task> getAllTasks(){
        ArrayList<Task> tasks = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(this.dbURL, this.username, this.password)) {
            Statement statement = conn.createStatement();
            ResultSet taskSet = statement.executeQuery(SELECT_ALL_TASKS);
            while(taskSet.next()){
                Task task = new Task(taskSet.getString(1), taskSet.getString(2), taskSet.getDate(4), taskSet.getBoolean(5));
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
    public void changeTaskDescription(String id, String newDescription) {
        if (newDescription == null) {return;}
        try (Connection conn = DriverManager.getConnection(this.dbURL, this.username, this.password)) {
            PreparedStatement statement = conn.prepareStatement(CHANGE_TASK_DESCRIPTION);
            statement.setString(1, newDescription);
            statement.setString(2, id);
            statement.executeUpdate();
            System.out.println("Successfully changed name of task in DB");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void changeTaskDate(String id, Date newDate) {
        if (newDate == null) {return;}
        try (Connection conn = DriverManager.getConnection(this.dbURL, this.username, this.password)) {
            PreparedStatement statement = conn.prepareStatement(CHANGE_TARGET_DATE);
            statement.setDate(1, newDate);
            statement.setString(2, id);
            statement.executeUpdate();
            System.out.println("Successfully changed target date of task in DB");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setTaskStatus(String id, boolean status) {
        try (Connection conn = DriverManager.getConnection(this.dbURL, this.username, this.password)) {
            PreparedStatement statement = conn.prepareStatement(CHANGE_TASK_STATUS);
            statement.setBoolean(1, status);
            statement.setString(2, id);
            statement.executeUpdate();
            System.out.println("Successfully marked task complete in DB");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}