
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Formatter;

public class DBG {

    public static void Log(String format,Object...objects) {
        try (FileWriter writer = new FileWriter("output.log", true)) {
            Calendar cal = Calendar.getInstance();
            Formatter time = new Formatter();
            time.format("[%tc] ",cal);
            Formatter content = new Formatter();
            content.format(format, objects);
            System.out.println(time.toString()+content.toString());
            writer.write(time.toString()+content.toString()+"\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void Log(String message) {
        try (FileWriter writer = new FileWriter("output.log", true)) {
            Calendar cal = Calendar.getInstance();
            Formatter time = new Formatter();
            time.format("[%tc] ",cal);
            Formatter content = new Formatter();
            content.format(message);
            System.out.println(time.toString()+content.toString());
            writer.write(time.toString()+content.toString()+"\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void LogWithoutTime(String message) {
        try (FileWriter writer = new FileWriter("output.log", true)) {
            System.out.println(message);
            writer.write(message+"\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
