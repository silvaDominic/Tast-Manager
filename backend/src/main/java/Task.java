import java.util.Date;

/**
 * Created by reclaimer on 6/5/16.
 */
public class Task {
    private int id;
    private String name;
    private Date targetDate;
    private boolean isComplete;

    public Task(int id, String name){
        this.id = id;
        this.name = name;
        this.targetDate = targetDate;
        this.isComplete = false;
    }

    public Task(){}

    // --------------------------------------- GETTERS AND SETTERS -----------------------------------------------------

    // Task ID
    public int getId(){
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
}
