package chessgame;

/*
        CHESS
        TABLEAU DE 4 X 4
        ON VEUT PLACER LES 4 REINES SANS CONFLIT

        -- THE BEST RESULT IS : --
        | - | - | R | - |
        | R | - | - | - |
        | - | - | - | R |
        | - | R | - | - |
        BEST CANDIDATE : 2031
        EVAL : 0

    FOR 10 x 10
        -- THE BEST RESULT IS : --
    | - | - | - | R | - | - | - | - | - | - |
    | - | - | - | - | - | - | - | - | - | R |
    | R | - | - | - | - | - | - | - | - | - |
    | - | - | - | - | - | - | - | - | R | - |
    | - | - | - | - | - | R | - | - | - | - |
    | - | - | R | - | - | - | - | - | - | - |
    | - | - | - | - | - | - | R | - | - | - |
    | - | R | - | - | - | - | - | - | - | - |
    | - | - | - | - | - | - | - | R | - | - |
    | - | - | - | - | R | - | - | - | - | - |
    BEST CANDIDATE : 3908526174
    EVAL : 0

Process finished with exit code 0


        COLS Y POS REINE INDEX  [0,1,2,3]
        ROW X POS REINE         [1,4,0,2]
 */

import java.sql.Timestamp;
import java.util.*;

public class ChessGame {
    static final int REINE = 10;
    static final byte POPULATION_SIZE = 40;
    static final int MAX_GENERATION = 200;
    static final byte CHANCE_OF_MUTATION = 50;
    static final byte CHANCE_OF_CROSSOVER = 80;
    static ArrayList<Byte[]> population = new ArrayList<Byte[]>();
    static boolean isPerfectResultFound = false;

    public static void main(String[] args) {
        Timestamp start = new Timestamp(System.currentTimeMillis());

        // GEN FIRST POPULATION
        generatePopulation(0);

        // FIRST EVALUATION
        populationSelection();

        // GENERATION
        for (int i = 0; i < MAX_GENERATION; i++) {
            // MUTATION & CROSS_OVER
            for (int j = 1; j < POPULATION_SIZE / 2; j++) {
                ArrayList<Byte[]> individus = new ArrayList<Byte[]>();
                individus.add(population.get(getRand(0, POPULATION_SIZE - 1)));
                individus.add(population.get(getRand(0, POPULATION_SIZE - 1)));
                while (evaluation(individus.get(0)) == evaluation(individus.get(1))) {
                    individus.remove(1);
                    individus.add(population.get(getRand(0, POPULATION_SIZE - 1)));
                }
                /*if (getRand(0, 100) < CHANCE_OF_CROSSOVER) {
                    population.add(croisement(individus.get(0), individus.get(1)));
                }*/
                if (getRand(0, 100) < CHANCE_OF_MUTATION) {
                    population.add(mutation(individus.get(1)));
                }
            }
            // SELECTION
            populationSelection();
            // REGEN POPULATION IF ECART TYPE IS LOW
            double ecartType = getPopulationEcartType();
            if (ecartType < 0.6) {
                generatePopulation(1);
            }
            if (isPerfectResultFound) {
                break;
            }
        }
        Timestamp end = new Timestamp(System.currentTimeMillis());
        long diff = end.getTime() - start.getTime();
        System.out.println("Process in " + diff + " ms");
        printResult();
    }

    /**
     * Compute the ecart type of population
     *
     * @return
     */
    private static double getPopulationEcartType() {
        HashMap<Integer, Integer> repartitionEvalutionAndNumber = new HashMap<Integer, Integer>();
        for (int i = 0; i < population.size(); i++) {
            int eval = evaluation(population.get(i));
            if (!repartitionEvalutionAndNumber.containsKey(eval)) {
                repartitionEvalutionAndNumber.put(eval, 1);
            } else {
                Integer number = repartitionEvalutionAndNumber.get(eval);
                number++;
                repartitionEvalutionAndNumber.put(eval, number);

            }
        }
        float moyennePondere = 0;
        for (Map.Entry<Integer, Integer> entry : repartitionEvalutionAndNumber.entrySet()) {
            Integer valeur = entry.getKey();
            Integer effectif = entry.getValue();

            moyennePondere += valeur * effectif;
        }
        moyennePondere /= population.size();
        float ecartType = 0;
        for (Map.Entry<Integer, Integer> entry : repartitionEvalutionAndNumber.entrySet()) {
            Integer valeur = entry.getKey();
            Integer effectif = entry.getValue();

            ecartType += effectif * Math.pow((valeur - moyennePondere), 2);
        }
        ecartType /= population.size();
        return Math.sqrt(ecartType);
    }

    /**
     * Take one individus and inverse the tab to recreate a new one
     *
     * @param a a individu
     * @return a mixed between the first and the second individu
     */
    private static Byte[] mutation(Byte[] a) {
        Byte[] c = a.clone();
        for (int i = 0; i < REINE; i++) {
            c[i] = a[(REINE - 1) - i];
        }
        return c;
    }

    private static boolean individuContainsDouble(Byte[] individu) {
        HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
        for (Byte gene : individu) {
            int key = (int) gene;
            if (!map.containsKey(key)) {
                map.put((int) gene, 0);
            }
            map.put(key, map.get(key) + 1);
        }
        for (Map.Entry<Integer, Integer> element : map.entrySet()) {
            if (element.getValue() > 1)
                return true;
        }
        return false;
    }

    /**
     * Take two individus and apply a random slice to recreate a new one
     *
     * @param a a first individu
     * @param b a seconde individu
     * @return c a mixed between the first and the second individu
     */
    private static Byte[] croisement(Byte[] a, Byte[] b) {
        Byte[] c = new Byte[REINE];
        int randSlice = getRand(0, REINE - 1);
        for (int i = 0; i < REINE; i++) {
            c[i] = (i < randSlice) ? a[i] : b[i];
        }
        return c;
    }

    /**
     * Sort and Select a Sample of N new individu
     */
    private static void populationSelection() {
        // SORT
        population.sort((a, b) -> individuCompare(a, b));
        // RESIZE
        while (population.size() != POPULATION_SIZE) {
            population.remove(population.size() - 1);
        }
    }

    private static int evaluation(Byte[] individu) {
        int colision = 0;
        for (int i = 0; i < REINE; i++) {
            for (int j = i + 1; j < REINE; j++) {
                int posCols = Math.abs(i - j);
                int posRow = Math.abs(individu[i] - individu[j]);
                if (posRow == posCols)
                    colision++;
            }
        }
        if (colision == 0) {
            isPerfectResultFound = true;
        }
        return colision;
    }

    /**
     * Comparaison function (for sorting purpose)
     *
     * @param a individu A
     * @param b individu B
     * @return 0 if A == B | -1 if A < B | 1 if A > B
     */
    private static int individuCompare(Byte[] a, Byte[] b) {
        int aResult = evaluation(a);
        int bresult = evaluation(b);
        if (aResult < bresult) {
            return -1;
        } else if (aResult > bresult) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Gen or Regen Population
     */
    private static void generatePopulation(int start) {
        for (int i = start; i < POPULATION_SIZE; i++) {
            Byte[] individu = new Byte[REINE];
            do {
                for (int j = 0; j < REINE; j++) {
                    individu[j] = (byte) getRand(0, REINE - 1);
                }
            } while (individuContainsDouble(individu));

            population.add(i, individu);
        }
    }

    /**
     * Randomize an Int
     *
     * @param min min value
     * @param max max value
     * @return random value
     */
    private static int getRand(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    /**
     * Print Result
     */
    private static void printResult() {
        System.out.println("-- THE BEST RESULT IS : --");
        Byte[] bestCandidat = population.get(0);
        String bestCandidatString = "";
        for (int i = 0; i < REINE; i++) {
            System.out.print("|");
            bestCandidatString += bestCandidat[i];
            for (int j = 0; j < REINE; j++) {
                if (j == bestCandidat[i]) {
                    System.out.print(" R ");
                } else {
                    System.out.print(" - ");
                }
                System.out.print("|");
            }
            System.out.print("\n");
        }
        System.out.println("BEST CANDIDATE : " + bestCandidatString);
        System.out.println("EVAL : " + evaluation(population.get(0)));
    }
}
