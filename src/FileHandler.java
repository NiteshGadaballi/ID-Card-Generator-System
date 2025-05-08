import java.io.FileWriter;
import java.io.IOException;

public class FileHandler {
    private static final String FILE_PATH = "output/data.txt";

    public static void saveRecord(String name, String id, String designation, String contact) {
        try (FileWriter writer = new FileWriter(FILE_PATH, true)) {
            writer.write(name + "," + id + "," + designation + "," + contact + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
