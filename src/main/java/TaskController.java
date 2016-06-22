import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static spark.Spark.*;

/**
 * Created by reclaimer on 6/12/16.
 */
public class TaskController {
    //Create variables for access to local host
    private String dbURL = "jdbc:mysql://localhost:3306/TaskDB?autoReconnect=true&useSSL=false";;
    private String username = "root";
    private String password = "roo7CLAUD1tis8";
    private SqlHandler sqlHandler;


    public TaskController(String dbURL, String username, String password){
        this.dbURL = dbURL;
        this.username = username;
        this.password = password;
        this.sqlHandler = new SqlHandler(dbURL, username, password);
        Spark.staticFileLocation("/client");

        post("/tasks", (request, response) -> postHandler(request));

        put("/tasks/:id", (request, response) -> putHandler(request));

        // TODO: Does this need a helper method?
        get("/tasks", (request, response) -> tasklistToJSON(sqlHandler.getAllTasks()));

        get("/tasks/:id", (request, response) -> getHandler(request));

        delete("/tasks/:id", (request, response) -> {
            deleteHandler(request);
            return response;
        });

        // TODO: Does this need a helper method?
        delete("/tasks", (request, response) -> {
            sqlHandler.clearTasks();
            return response;
        });
    }

    // ------------------------------------------ HTTP HANDLERS --------------------------------------------------------

    private String postHandler(Request request){
        Task newTask = null;
        try {
            // Converts JSON string to JSON object
            JSONObject task = new JSONObject(request.body());
            // New date format created for parsing target_date from JSON object
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dateFormat.parse(task.get("target_date").toString());
            // Inserts task into DB and stores as Task object to be returned back to client WITH id
            newTask = sqlHandler.createTask(task.get("task_description").toString(), new java.sql.Date(date.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // Converts Task object to JSON object
        return taskToJSON(newTask);
    }

    private String putHandler(Request request){
        String id = (request.params(":id"));
        Task updatedTask = null;
        try{
            // Converts JSON string to JSON object
            JSONObject task = new JSONObject(request.body());
            if (task.has("task_description")){
                sqlHandler.changeTaskDescription(id, task.get("task_description").toString());
            }
            if (task.has("target_date")){
                // New date format created for parsing target_date from JSON object
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = dateFormat.parse(task.get("target_date").toString());
                sqlHandler.changeTaskDate(id, new java.sql.Date(date.getTime()));
            }
            if (task.has("status")){
                System.out.println(task.get("status"));
                sqlHandler.setTaskStatus(id, Boolean.parseBoolean(task.get("status").toString()));
            }
            // Used to return task object WITH id back to client
            updatedTask = sqlHandler.getTask(id);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // Converts Task object to JSON object
        return taskToJSON(updatedTask);
    }

    private void deleteHandler(Request request){
        String id = (request.params(":id"));
        sqlHandler.deleteTask(id);
    }

    private String getHandler(Request request){
        String id = request.params(":id");
        return taskToJSON(sqlHandler.getTask(id));
    }

    // ------------------------------------------ HELPER FUNCTIONS -----------------------------------------------------

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