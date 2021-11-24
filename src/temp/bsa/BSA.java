package temp.bsa;

import temp.bsa.models.Particule;

import java.util.Random;

public class BSA {
    // ---------- PARAMETRE ---------- //
    public static final int NOMBRE_DE_VOISIN = 4;
    public static final int[] NOMBRE_DE_PARTICULE = {35, 40};
    public static final int MAX_ITERRATION = 10_000;

    public static final double[] DELTA_VITESSE = {-4.0, 4.0};

    public static final int FIT_VOULUE = 0;
    public static final int DIMENSION = 4;

    public static final double[] BORNE = {-5.12, 5.12};

    public static final int C1 = 2;
    public static final int C2 = 2;
    // ---------- VARIABLES ---------- //
    public static Particule[] particules;

    public static void main(String[] args) {
        initializeParticule();

    }

    public static void initializeParticule() {
        particules = new Particule[NOMBRE_DE_PARTICULE[0]];
        for (int i = 0; i < NOMBRE_DE_PARTICULE[0]; i++) {
            particules[i] = new Particule(DIMENSION);
            for (int d = 0; d < DIMENSION; d++) {
                particules[i].p[d] = getRandomPosition();
                particules[i].pBest[d] = particules[i].p[d];
                particules[i].v[d] = getRandomVitesse();
            }
            particules[i].f = calculFitness(particules[i]);
            particules[i].fBest = particules[i].f;
        }
    }

    private static double calculFitness(Particule particule) {
        double somme = 0;
        for (int i = 0; i < DIMENSION; i++) {
            double x = particule.p[i];
            somme += x * x - 10 * Math.cos(2 * Math.PI * x);
        }
        return 10 * DIMENSION + somme;
    }

    // ---------- UTILS FCT ---------- //
    public static double getRandom(double min, double max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        double rand = min + new Random().nextDouble() * (max - min);
        return rand;
    }

    public static int getRandomZeroOrOne() {
        return new Random().nextInt(2);
    }

    public static int getRandomInt() {
        return new Random().nextInt(2);
    }

    private static double getRandomPosition() {
        return getRandom(BORNE[0], BORNE[1]);
    }

    private static double getRandomVitesse() {
        return getRandom(DELTA_VITESSE[0], DELTA_VITESSE[1]);
    }
}
