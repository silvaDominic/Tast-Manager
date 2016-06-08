import java.util.Date;

/**
 * Created by reclaimer on 6/5/16.
 */
public class Task {
    private int taskId;
    private String taskName;
    private Date targetDate;
    private boolean isComplete;

    public Task(int taskId, String taskName){
        this.taskId = taskId;
        this.taskName = taskName;
        this.targetDate = targetDate;
        this.isComplete = false;
    }

    // --------------------------------------- GETTERS AND SETTERS -----------------------------------------------------

    // Task ID
    public int getTaskID(){
        return this.taskId;
    }

    // Task name
    public String getTaskName(){
        return this.taskName;
    }

    public void setTaskName(String newTaskName){
        this.taskName = newTaskName;
    }

    // Target date
    public Date getTargetDate(){
        return this.targetDate;
    }

    public void setTargetDate(Date newDate){
        this.targetDate = newDate;
    }
}
