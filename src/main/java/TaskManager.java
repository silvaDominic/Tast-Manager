/**
 * Created by reclaimer on 6/6/16.
 */
interface TaskManager {
    public void changeTaskName(String newName, int id);

    public void deleteTask(int id);

    public void createTask(String newTask, int id);

    public boolean isCompleted();
}
