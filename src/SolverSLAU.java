import org.jblas.DoubleMatrix;

/**
 * Created by igoryan on 26.10.15.
 */
public interface SolverSLAU {
    public final double eps = Math.pow(10, -6);
    public void solving();
    public DoubleMatrix getX();
    public int getN();
    public void printX();
}
