import java.sql.Date;

/**
 * Created by reclaimer on 6/5/16.
 */
public class Task {
    private String id;
    private String description;
    private java.util.Date targetDate;
    private boolean isComplete;

    public Task(String id, String description, java.util.Date targetDate){
        this.id = id;
        this.description = description;
        this.targetDate = targetDate; // TODO: Add to parameters figuring out format
        this.isComplete = false;
    }

    public Task(String id, String description, java.util.Date targetDate, boolean status){
        this.id = id;
        this.description = description;
        this.targetDate = targetDate; // TODO: Add to parameters figuring out format
        this.isComplete = status;
    }

    public Task(){}

    // --------------------------------------- GETTERS AND SETTERS -----------------------------------------------------

    // Task ID
    public String getId(){
        return this.id;
    }

    // Task Description
    public String getDescription(){
        return this.description;
    }

    public void setDescription(String newTaskName){
        this.description = newTaskName;
    }

    // Target Date
    public java.util.Date getTargetDate(){
        return this.targetDate;
    }

    public void setTargetDate(java.util.Date newDate){
        this.targetDate = newDate;
    }

    // Task Status
    public boolean getStatus(){
        return this.isComplete;
    }

    public void setStatus(boolean status){
        this.isComplete = status;
    }
}
