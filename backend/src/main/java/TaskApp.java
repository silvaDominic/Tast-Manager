import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

import static spark.Spark. *;

/**
 * Created by reclaimer on 5/15/16.
 */

public class TaskApp {
    public static void main(String[] args) {

        // -------------------------------------------TESTING-----------------------------------------------------------
        //Create variables for access to local host
        String dbURL = "jdbc:mysql://localhost:3306/TaskDB?autoReconnect=true&useSSL=false";
        String username = "root";
        String password = "---";

        ArrayList<Task> taskList = new ArrayList<>();

        taskList.add(new Task(1, "Can"));
        taskList.add(new Task(2, "You"));
        taskList.add(new Task(3, "See"));
        taskList.add(new Task(4, "Me?"));

        SqlHandler sql = new SqlHandler(dbURL, username, password);
        for (Task task : taskList){
            sql.createTask(task.getTaskName(), task.getTaskID());
            sql.markComplete(task.getTaskID());
            System.out.println(task.getTaskID());
        }

        get("/tasks", (request, response) -> {
            return convertToJSON(taskList);
        });
    }

    private static ArrayList convertToJSON(ArrayList list){
        // Initialize and instantiate Object Mapper and JSON object
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<String> jsonString = new ArrayList<String>();

        // Iterate over each item in list and convert it to a String,
        // add it to jsonString list
        for (Object item: list){
            try {
                String innerString = mapper.writeValueAsString(item);
                jsonString.add(innerString);
            } catch (JsonProcessingException e) { //Handle possible invalid types
                e.printStackTrace();
            }
        }
        return jsonString;
    }
}
