import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
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
        String password = "roo7CLAUD1tis8";

        ArrayList<Task> taskList = new ArrayList<>();
        SqlHandler sql = new SqlHandler(dbURL, username, password);

        post("/tasks", (request, response) -> {
            try {
                ObjectMapper mapper = new ObjectMapper();
                Task task = mapper.readValue(request.body(), Task.class);
                sql.createTask(task.getName(), task.getId());
                taskList.add(task);
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

/*        put("/tasks/:id", (request, response) -> {
            try{
                request.params(":id");
                request.queryParams("name");
                request.queryParams("targetDate");
                request.queryParams("isCompleted");

                sql.changeTaskName();
            } catch
            // TODO: How should dates be handled?
        });*/

        delete("/tasks/:id", (request, response) -> {
            int id = Integer.parseInt(request.params(":id"));
            sql.deleteTask(id);
            return response;
        });

        get("/tasks", (request, response) -> TasklistToJSON(taskList));

        get("/tasks/:id", (request, response) -> {
            return ;
        });
    }

    // TODO: implement put and get(id) calls

    // NOT SURE IF I EVEN NEED THIS
    private static Task jsonToTask(String json){
        Task task = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            task = mapper.readValue(json, Task.class);
            System.out.println(task.getId());

        } catch (JsonGenerationException jsonGenErr){
            task = null;
            jsonGenErr.printStackTrace();
        } catch (JsonMappingException jsonMapErr) {
            task = null;
            jsonMapErr.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return task;
    }

    private static String TaskToJSON(Object obj){
        ObjectMapper mapper = new ObjectMapper();
        String json = "null";
        try {
            json = mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }

    private static ArrayList TasklistToJSON(ArrayList list){
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
