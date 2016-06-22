import java.sql.Date;
import java.util.ArrayList;

/**
 * Created by reclaimer on 6/6/16.
 */
interface TaskManager {
    public Task createTask(String newTask, Date targetDate);

    public void deleteTask(String id);

    public void clearTasks();

    public Task getTask(String id);

    public ArrayList<Task> getAllTasks();

    public void changeTaskDescription(String id, String newDescription);

    public void changeTaskDate(String id, Date newDate);

    public void setTaskStatus(String id, boolean status);
}