import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.json.JSONObject;
import spark.Spark;

import java.io.IOException;
import java.sql.Date;
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
        String password = "---";

        Spark.staticFileLocation("/client");

        SqlHandler sql = new SqlHandler(dbURL, username, password);

        post("/tasks", (request, response) -> {
            JSONObject task = new JSONObject(request.body());
            SimpleDateFormat toDate = new SimpleDateFormat("dd-MM-yyyy");
            java.util.Date date = toDate.parse(task.get("target_date").toString()); // TODO: Why does program stop here?
            sql.createTask(task.get("task_name").toString(), new java.sql.Date(date.getTime()));
            return response;
        });

        put("/tasks/:id", (request, response) -> {
            try{
                ObjectMapper mapper = new ObjectMapper();
                Task task = mapper.readValue(request.body(), Task.class);
                sql.changeTaskName(task.getId(), task.getName());
                sql.changeTaskDate(task.getId(), task.getTargetDate());
                sql.taskStatus(task.getId(), task.getStatus());
            } catch (JsonGenerationException jsonGenErr){
                jsonGenErr.printStackTrace();
            } catch (JsonMappingException jsonMapErr) {
                jsonMapErr.printStackTrace();
            } catch (JsonParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
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
            return TasklistToJSON(sql.getAllTasks());
        });

        get("/tasks/:id", (request, response) -> {
            String id = request.params(":id");
            return TaskToJSON(sql.getTask(id));
        });
    }

    private String TaskToJSON(Object obj){
        // Initialize and instantiate Object Mapper and JSON object
        ObjectMapper mapper = new ObjectMapper();
        String json = "null";
        try {
            json = mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }

    private ArrayList TasklistToJSON(ArrayList list){
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