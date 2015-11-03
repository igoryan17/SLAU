import org.jblas.DoubleMatrix;
import org.jblas.Solve;

/**
 * Created by igoryan on 25.10.15.
 */
public class Builder {
    protected final int N;
    protected final int countElem;
    protected DoubleMatrix X;
    protected DoubleMatrix A;
    protected DoubleMatrix f;
    protected double h;
    protected final int dimensionX;

    public Builder(int n) {
        N = n;
        dimensionX = N - 2;
        countElem = N * N - 4 * (N - 1);
        X = new DoubleMatrix(countElem, 1);
        A = new DoubleMatrix(countElem, countElem);
        f = new DoubleMatrix(countElem, 1);
        h = Math.pow(N, -1);
        buildA();
        buildF();
    }

    public void buildF() {
        double temp = 0;
        for (int i = 1; i < (N - 1); i++) {
            for (int j = 1; j < (N - 1); j++) {
                temp = h * (j * (1 - j * h) + i * (1 - i * h));
                f.put(pos(i, j), 0, temp);
            }
        }
    }

    public void buildA() {
        for (int i = 0; i < countElem; i++) {
            for (int j = 0; j < countElem; j++) {
                handler(i, j);
            }

        }
    }

    public void handler(int i, int j) {
        if (i == j) {
            A.put(i, j, -4 * Math.pow(h, -2));
            return;
        }
        int row = rowNumber(i);
        int column = columnNumber(i);
        if (row > 1) {
            if (j == pos(row - 1, column)) {
                A.put(i, j, Math.pow(h, -2));
                return;
            }
        }
        if (column < dimensionX) {
            if (j == pos(row, column + 1)) {
                A.put(i, j, Math.pow(h, -2));
                return;
            }
        }
        if (row < dimensionX) {
            if (j == pos(row + 1, column)) {
                A.put(i, j, Math.pow(h, -2));
                return;
            }
        }
        if (column > 1) {
            if (j == pos(row, column - 1)) {
                A.put(i, j, Math.pow(h, -2));
                return;
            }
        }
        A.put(i, j, 0);
    }

    public void printA() {
        for (int i = 0; i < countElem; i++) {
            for (int j = 0; j < countElem; j++) {
                System.out.print((A.get(i, j)) + " ");
            }
            System.out.println();
        }
    }

    public void printF() {
        for (int i = 0; i < countElem; i++) {
            System.out.println(f.get(i, 0));
        }
    }

    /*
    return row position of element matrix X, which multiplies on element line matrix A number j
     */
    public int rowNumber(int j) {
        return (int) Math.ceil(((double) (j + 1) / (dimensionX)));
    }

    /*
    work how rowNumber
     */
    public int columnNumber(int j) {
        int modulo = (j + 1) % (dimensionX);
        return (modulo == 0) ? (dimensionX) : modulo;
    }

    public int pos(int i, int j) {
        return (i - 1) * (dimensionX) + j - 1;
    }

    public double getH() {
        return h;
    }

    public double getConditionNumber() {
        return A.norm1() * Solve.pinv(A).norm1();
    }

    public int getN() {
        return N;
    }
}
