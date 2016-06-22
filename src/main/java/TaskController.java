import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import spark.Spark;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import static spark.Spark.*;

/**
 * Created by reclaimer on 6/12/16.
 */
public class TaskController {

    public TaskController(){
        //Create variables for access to local host
        String dbURL = "jdbc:mysql://localhost:3306/TaskDB?autoReconnect=true&useSSL=false";
        String username = "root";
        String password = "roo7CLAUD1tis8";

        Spark.staticFileLocation("/client");

        SqlHandler sql = new SqlHandler(dbURL, username, password);

        post("/tasks", (request, response) -> {
            Task newTask = null;
            try {
                // Converts JSON string to JSON object
                JSONObject task = new JSONObject(request.body());
                // New date format created for parsing target_date from JSON object
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date date = dateFormat.parse(task.get("target_date").toString());
                // Inserts task into DB and stores as Task object to be returned back to client WITH id
                newTask = sql.createTask(task.get("task_description").toString(), new java.sql.Date(date.getTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            // Converts Task object to JSON object
            return taskToJSON(newTask);
        });

        // TODO: Handle individual parts of the json string
        put("/tasks/:id", (request, response) -> {
            Task updatedTask = null;
            String id = "";
            try{
                id = (request.params(":id"));
                // Converts JSON string to JSON object
                JSONObject task = new JSONObject(request.body());
                // New date format created for parsing target_date from JSON object
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date date = dateFormat.parse(task.get("target_date").toString());
                // Change all possible fields in DB
                sql.changeTaskDescription(id, task.get("task_description").toString());
                sql.changeTaskDate(id, new java.sql.Date(date.getTime()));
                sql.setTaskStatus(id, Boolean.parseBoolean(task.get("status").toString()));
                // Used to return task object WITH id back to client
                updatedTask = sql.getTask(id);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            // Converts Task object to JSON object
            return taskToJSON(updatedTask);
        });

        delete("/tasks/:id", (request, response) -> {
            String id = (request.params(":id"));
            sql.deleteTask(id);
            return response;
        });

        delete("/tasks", (request, response) -> {
            sql.clearTasks();
            return response;
        });

        get("/tasks", (request, response) -> {
            return tasklistToJSON(sql.getAllTasks());
        });

        get("/tasks/:id", (request, response) -> {
            String id = request.params(":id");
            return taskToJSON(sql.getTask(id));
        });
    }

    private String taskToJSON(Object obj){

        // Initialize and instantiate Object Mapper and JSON object
        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        try {
            json = mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }

    private ArrayList tasklistToJSON(ArrayList list){
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

// TODO: Make handlers for Http methods