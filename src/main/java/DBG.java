import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Formatter;

public class DebugLogging {

   // private static FileWriter writer = new FileWriter("output.log",true);

    public static void Log(String format,Object...objects) {
        try (FileWriter writer = new FileWriter("output.log", true)) {
            Formatter formatter = new Formatter();
            formatter.format(format, objects);
            System.out.println(formatter);
            writer.write(formatter.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
