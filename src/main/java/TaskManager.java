import java.sql.Date;
import java.util.ArrayList;

/**
 * Created by reclaimer on 6/6/16.
 */
interface TaskManager {
    public void changeTaskName(String id, String newName);

    public void changeTaskDate(String id, Date newDate);

    public void deleteTask(String id);

    public void createTask(String newTask, Date targetDate);

    public void taskStatus(String id, boolean status);

    public ArrayList<Task> getAllTasks();

    public Task getTask(String id);

    public void clearTasks();
}
