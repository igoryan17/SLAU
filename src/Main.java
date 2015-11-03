import org.jblas.DoubleMatrix;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by igoryan on 26.10.15.
 */
public class Main {
    public static void main(String[] args) {
        File outPut = new File("out.txt");
        if (!outPut.exists()) {
            try {
                outPut.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(outPut);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for (int i = 5; i < 40; i++) {
            SolverSLAU solver = new JacobiMethod(i);
            long start = System.nanoTime();
            solver.solving();
            long finish = System.nanoTime();
            printWriter.println(i + " " + (finish - start) / 1000);
        }
        printWriter.close();
    }
}
