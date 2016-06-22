import java.sql.Date;
import java.util.ArrayList;

/**
 * Created by reclaimer on 6/6/16.
 */
interface TaskManager {
    public void changeTaskDescription(String id, String newDescription);

    public void changeTaskDate(String id, Date newDate);

    public void deleteTask(String id);

    public Task createTask(String newTask, Date targetDate);

    public void setTaskStatus(String id, boolean status);

    public ArrayList<Task> getAllTasks();

    public Task getTask(String id);

    public void clearTasks();
}