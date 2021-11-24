package temp.bsa.models;

public class Particule {
    public double[] p;
    public double[] pBest;
    public double[] v;
    public double f;
    public double fBest;

    public Particule(int dimension) {
        p = new double[dimension];
        pBest = new double[dimension];
        v = new double[dimension];
    }
}
