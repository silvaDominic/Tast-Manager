import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by reclaimer on 6/12/16.
 */
public class TaskRepository {

    private Map<Integer, Task> tasks = new HashMap<>();

    public TaskRepository(){
    }

    public ArrayList<Task> getAllTasks(){
        return new ArrayList<Task>(tasks.values());
    }

    public Task getTask(int id){
        return tasks.get(id);
    }

    public void addTask(Task task){
        this.tasks.put(task.getId(), task);
    }
}