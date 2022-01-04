package particule;
/*
    Comme les oiseaux ou les essaims

    Problèmatique minimiser la function Somme de x² (i=0 -> dim=4)

*/


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Essaim {

    // CONSTANTE
    static final byte DIMENSION = 4;
    static final int POPULATION_SIZE = 40;
    static final int MAX_ITTERATION = 1_000;

    static final float MIN_POS = -5.12f;
    static final float MAX_POS = 5.12f;
    static final float MIN_V = -4;
    static final float MAX_V = 4;

    static final float C1 = 1.5f;
    static final float C2 = 1.5f;
    // VARIABLE
    static ArrayList<Particule> essaim = new ArrayList<Particule>();

    static Particule meilleurParticule;

    public static void main(String[] args) {
        Timestamp start = new Timestamp(System.currentTimeMillis());
        System.out.println("START");

        // INITIALISATION GEN FIRST POPULATION
        for (int i = 0; i < POPULATION_SIZE; i++) {
            Particule particule = new Particule(DIMENSION);
            particule.positionCourante = randomize_float_array_between(MIN_POS, MAX_POS, DIMENSION);
            particule.meilleurPosition = particule.positionCourante.clone();
            particule.vitesse = randomize_float_array_between(MIN_V, MAX_V, DIMENSION);
            // FIRST EVALUATION
            particule.costPositionCourante = evaluation(particule.positionCourante);
            particule.costMeilleurPosition = particule.costPositionCourante;
            essaim.add(i, particule);
        }

        int currentIteration = 0;
        meilleurParticule = getMeilleurParticule();
        // MAX_DEPLACEMENT
        while (currentIteration <= MAX_ITTERATION) {
            meilleurParticule = getMeilleurParticule();
            // DEPLACEMENT VERS LA MEILLEUR DES PARTICULES
            for (int i = 0; i < POPULATION_SIZE; i++) {
                Particule particule = essaim.get(i);
                for (int d = 0; d < DIMENSION; d++) {
                    float R1 = C1 * getRandInt(0, 1);
                    float R2 = C2 * getRandInt(0, 1);
                    float vitesseValue = particule.vitesse[d]
                            + R1 * (particule.meilleurPosition[d] - particule.positionCourante[d])
                            + R2 * (meilleurParticule.meilleurPosition[d] - particule.positionCourante[d]);
                    particule.vitesse[d] = valueBorne(vitesseValue, MIN_V, MAX_V);
                    float positionValue = particule.positionCourante[d] + particule.vitesse[d];
                    particule.positionCourante[d] = valueBorne(positionValue, MIN_POS, MAX_POS);
                }
            }
            // EVALUTATION TIME :)
            for (int i = 0; i < POPULATION_SIZE; i++) {
                Particule particule = essaim.get(i);
                particule.costPositionCourante = evaluation(particule.positionCourante);
                if (particule.costMeilleurPosition > particule.costPositionCourante) {
                    particule.costMeilleurPosition = particule.costPositionCourante;
                    particule.meilleurPosition = particule.positionCourante.clone();
                }
            }
            currentIteration++;
        }

        Timestamp end = new Timestamp(System.currentTimeMillis());
        long diff = end.getTime() - start.getTime();
        System.out.println("Process in " + diff + " ms");
        printResult();
    }

    private static void printResult() {
        System.out.println("MEILLEUR PARTICULE");
        System.out.println("COUT CURRENT : " + meilleurParticule.costPositionCourante);
        System.out.println(Arrays.toString(meilleurParticule.positionCourante));
        System.out.println("COUT BEST: " + meilleurParticule.costMeilleurPosition);
        System.out.println(Arrays.toString(meilleurParticule.meilleurPosition));
    }


    private static float valueBorne(float value, float min, float max) {
        if (min > value) {
            return min;
        } else if (value > max) {
            return max;
        } else {
            return value;
        }
    }

    private static Particule getMeilleurParticule() {
        Particule meilleurParticule = essaim.get(0);
        for (int i = 1; i < POPULATION_SIZE; i++) {
            Particule particule = essaim.get(i);
            if (meilleurParticule.costMeilleurPosition > particule.costMeilleurPosition) {
                meilleurParticule = particule;
            }
        }
        return meilleurParticule;
    }

    // Function objectif
    private static float evaluation(float[] array) {
        float eval = 0;
        for (float x : array) {
            eval += Math.pow(x, 2);
        }
        return eval;
    }

    static float[] randomize_float_array_between(float min, float max, int dimension) {
        float array[] = new float[dimension];
        for (int i = 0; i < dimension; i++) {
            array[i] = getRandFloat(min, max);
        }
        return array;
    }

    private static float getRandFloat(float min, float max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        Random r = new Random();
        return r.nextFloat() * (max - min) + min;
    }

    private static int getRandInt(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }
}