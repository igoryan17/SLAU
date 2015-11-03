import org.jblas.DoubleMatrix;

import javax.swing.*;
import java.awt.*;

/**
 * Created by igoryan on 26.10.15.
 */
public class Painter extends JFrame {
    private int size;
    private int step;
    private DoubleMatrix X;
    private int N;
    private final int deepColor = 255;

    Painter(SolverSLAU solverSLAU) {
        super("Matrix");
        setContentPane(new drawPane());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        N = solverSLAU.getN();
        step = 400 / N;
        if (step == 0) {
            step = 1;
        }
        size = step * N;
        setSize(size, size);
        setVisible(true);
        X = solverSLAU.getX();
    }

    class drawPane extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            draw(g);
        }
    }

    private void draw(Graphics g) {
        Color zeroColor = new Color(1, 0, 0);
        for (int i = 0; i < N; i++) {
            g.setColor(zeroColor);
            g.fillRect(0, i * step, step, step);
            g.fillRect(i * step, 0, step, step);
            g.fillRect((N - 1) * step, i * step, step, step);
            g.fillRect(i * step, (N - 1) * step, step, step);
        }
        double max = Math.abs(X.get(0, 0));
        for (int i = 1; i < X.rows; i++) {
            double elem = Math.abs(X.get(i, 0));
            if (elem >= max) {
                max = elem;
            }

        }
        final double scaleColor = (double) deepColor / max;
        //System.out.println("scale color = " + scaleColor);
        //System.out.println("max = " + max);
        for (int i = 0; i < X.rows; i++) {
            double elem = X.get(i, 0);
            int color = (int) ((elem >= 0) ? Math.round(elem * scaleColor) : Math.round(-elem * scaleColor));
            if (elem >= 0) {
                g.setColor(new Color(color, 0 , 0));
            }
            if (elem < 0) {
                g.setColor(new Color(0, 0, color));
            }
            int x = (i % (N - 2) + 1) * step;
            int y = ((i / (N - 2)) + 1) * step;
            g.fillRect(x, y, step, step);
        }
    }
}