import java.io.IOException;

public class Main {


    public static void main (String args[]){

        DebugLogging.Log("%s","Hello world!");
        if ((args.length>0)&&(args[0].compareToIgnoreCase("commit")==0)) {
            ExecutionEngine executionEngine = new ExecutionEngine();
            try {
                executionEngine.Execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
           /* ExecutionEngine executionEngine = new ExecutionEngine();
            try {
                executionEngine.Execute();
            } catch (IOException e) {
                e.printStackTrace();
            }*/
        }

       }
}

