package particule;
/*
    Comme les oiseaux ou les essaims

    Problèmatique minimiser la function Somme de x² (i=0 -> dim=4)

*/


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Random;

public class Essaim {

    // CONSTANTE
    static final byte DIMENSION = 4;
    static final byte POPULATION_SIZE = 40;
    static final int MAX_DEPLACEMENT = 50;

    static final float MIN_POS = -5.12f;
    static final float MAX_POS = 5.12f;
    static final float MIN_V = -4;
    static final float MAX_V = 4;

    static final float C1 = 1.5f;
    static final float C2 = 1.5f;
    // VARIABLE
    static ArrayList<Particule> population = new ArrayList<Particule>();

    public static void main(String[] args) {
        Timestamp start = new Timestamp(System.currentTimeMillis());
        System.out.println("START");
        // INITIALISATION GEN FIRST POPULATION
        for (int i = 0; i < POPULATION_SIZE; i++) {
            Particule particule = new Particule(DIMENSION);
            particule.positionCourante = randomize_float_array_between(MIN_POS, MAX_POS, DIMENSION);
            particule.meilleurPosition = particule.positionCourante;
            particule.velocite = randomize_float_array_between(MIN_V, MAX_V, DIMENSION);
            // FIRST EVALUATION
            particule.costPositionCourante = evaluation(particule.positionCourante);
            particule.costMeilleurPosition = particule.costPositionCourante;
            population.add(i, particule);
        }

        int currentIteration = 0;
        // MAX_DEPLACEMENT
        while (currentIteration <= MAX_DEPLACEMENT) {
            currentIteration++;
            Particule meilleurParticule = getMeilleurParticule();
            // DEPLACEMENT VER LA MEILLEUR DES PARTICULES
            for (int i = 0; i < POPULATION_SIZE; i++) {
                Particule particule = population.get(i);
                for (int j = 0; j < DIMENSION; j++) {
                    float R1 = C1 * getRandFloat(0, 1);
                    float R2 = C2 * getRandFloat(0, 1);
                    particule.velocite[i] = valueBorne(particule.velocite[i] + R1 * (particule.meilleurPosition[i] - particule.positionCourante[i]) + R2 * (meilleurParticule.meilleurPosition[i] - meilleurParticule.positionCourante[i]), MIN_V, MAX_V);
                    particule.positionCourante[i] = valueBorne(particule.positionCourante[i] + particule.velocite[i], MIN_POS, MAX_POS);
                }
            }
            // EVALUTATION TIME :)
            for (int i = 0; i < POPULATION_SIZE; i++) {
                Particule particule = population.get(i);
                particule.costPositionCourante = evaluation(particule.positionCourante);
                if (particule.costMeilleurPosition > particule.costPositionCourante) {
                    particule.costMeilleurPosition = particule.costPositionCourante;
                    particule.meilleurPosition = particule.positionCourante;
                }
            }
        }


        Timestamp end = new Timestamp(System.currentTimeMillis());
        long diff = end.getTime() - start.getTime();
        System.out.println("Process in " + diff + " ms");

    }

    private static float valueBorne(float value, float min, float max) {
        if (value < min) {
            return min;
        } else if (value > max) {
            return max;
        } else {
            return value;
        }
    }

    private static Particule getMeilleurParticule() {
        Particule meilleurParticule = population.get(0);
        for (int i = 1; i < POPULATION_SIZE; i++) {
            Particule particule = population.get(i);
            if (particule.costPositionCourante < particule.costPositionCourante) {
                meilleurParticule = particule;
            }
        }
        return meilleurParticule;
    }

    private static float evaluation(float[] array) {
        float eval = 0;
        for (int i = 0; i < DIMENSION; i++) {
            eval += array[i];
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
}