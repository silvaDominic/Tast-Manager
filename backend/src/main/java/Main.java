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

public class Main {
    public static void main(String[] args) {
        new TaskController(new Tasks());
    }
}
