import java.sql.Date;

/**
 * Created by reclaimer on 6/5/16.
 */
public class Task {
    private String id;
    private String name;
    private Date targetDate;
    private boolean isComplete;

    public Task(String id, String name, Date targetDate){
        this.id = id;
        this.name = name;
        this.targetDate = targetDate; // TODO: Add to parameters figuring out format
        this.isComplete = false;
    }

    public Task(String id, String name, Date targetDate, boolean status){
        this.id = id;
        this.name = name;
        this.targetDate = targetDate; // TODO: Add to parameters figuring out format
        this.isComplete = status;
    }

    public Task(){}

    // --------------------------------------- GETTERS AND SETTERS -----------------------------------------------------

    // Task ID
    public String getId(){
        return this.id;
    }

    // Task name
    public String getName(){
        return this.name;
    }

    public void setName(String newTaskName){
        this.name = newTaskName;
    }

    // Target date
    public Date getTargetDate(){
        return this.targetDate;
    }

    public void setTargetDate(Date newDate){
        this.targetDate = newDate;
    }

    public boolean getStatus(){
        return this.isComplete;
    }

    public void setStatus(boolean status){
        this.isComplete = status;
    }
}
