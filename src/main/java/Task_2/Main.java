package Task_2;

import Task_2.gui.Frame;
import Task_2.models.DataBase;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;


public class Main {
    public static void main(String[] args)
    throws IOException {
        var objectMapper = new ObjectMapper();
        File data = new File("src/main/resources/data.json");

        var dataBase = objectMapper.readValue(data, DataBase.class);

        try {
            var frame = new Frame(dataBase);
            frame.run();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            objectMapper.writeValue(data, dataBase);
        }
    }
}
