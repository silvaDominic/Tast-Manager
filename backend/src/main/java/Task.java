/**
 * Created by reclaimer on 6/5/16.
 */
public class Task {
    private int taskId;
    private String taskName;

    public Task(int taskId, String taskName){
        this.taskId = taskId;
        this.taskName = taskName;
    }

    public int getTaskID(){
        return taskId;
    }

    public String getTaskName(){
        return taskName;
    }
}
