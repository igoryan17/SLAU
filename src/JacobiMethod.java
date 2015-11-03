import org.jblas.DoubleMatrix;
import org.jblas.Solve;

/**
 * Created by igoryan on 26.10.15.
 */
public class JacobiMethod extends Builder implements SolverSLAU {
    private DoubleMatrix P;
    private DoubleMatrix D;
    private DoubleMatrix inverseD;
    private DoubleMatrix g;
    private DoubleMatrix X;
    private DoubleMatrix r;
    private long practicIterations;

    public JacobiMethod(int n) {
        super(n);
        X = new DoubleMatrix(countElem, 1);
        D = new DoubleMatrix(countElem, countElem);
        for (int i = 0; i < countElem; i++) {
            D.put(i, i, A.get(i, i));
        }
        inverseD = Solve.pinv(D);
        P = (inverseD.mmul(A.sub(D))).mmuli(-1);
        g = inverseD.mmul(f);
        r = new DoubleMatrix(f.elementsAsList());
        r = f;
        checkMatrix();
    }

    @Override
    public void solving() {
        DoubleMatrix Xk = X;
        double normMinus;
        do {
            Xk = X;
            X = P.mmul(X).addi(g);
            practicIterations++;
            normMinus = Xk.subi(X).norm2();
            r = A.mmul(X).subi(f);
        }
        while (Math.abs(normMinus) > eps);
        //System.out.println("iterations = " + practicIterations);
        //System.out.println("nevyazka = " + r.norm1());
    }

    @Override
    public DoubleMatrix getX() {
        return X;
    }

    @Override
    public void printX() {
        for (int i = 0; i < X.rows; i++) {
            System.out.println(X.get(i, 0));
        }
    }

    private boolean checkMatrix() {
        boolean flag = true;
        for (int i = 0; i < A.rows; i++) {
            for (int j = 0; j < A.columns; j++) {
                if (i != j) {
                    for (int k = 0; k < A.rows; k++) {
                        if (Math.abs(A.get(k, k)) < Math.abs(A.get(i, j))) {
                            flag = false;
                            break;
                        }
                    }
                }
                if (!flag) {
                    break;
                }
            }
        }
        if (!flag) {
            System.out.println("not diagonal majority");
        }
        return flag;
    }

    private double normC(DoubleMatrix A) {
        MyThread[] threads = new MyThread[A.rows];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new MyThread(A.getRow(i).data, i);
            threads[i].run();
        }
        for (MyThread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        double norm = 0;
        for (MyThread thread : threads) {
            if (thread.getSum() > norm) {
                norm = thread.getSum();
            }
        }
        return norm;
    }

    class MyThread extends Thread {
        double[] row;
        int number;
        double sum = 0;

        public MyThread(double[] row, int number) {
            super();
            this.row = row;
            this.number = number;
        }

        @Override
        public void run() {
            for (int i = 0; i < row.length; i++) {
                if (i!= number) {
                    sum+= Math.abs(row[i] / row[number]);
                }
            }
        }

        public double getSum() {
            return sum;
        }
    }
}
