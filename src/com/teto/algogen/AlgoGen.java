package com.teto.algogen;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Random;

public class AlgoGen {

    // CONSTANTE
    static final byte DIMENSION = 10;
    static final byte POPULATION_SIZE = 30;
    static final int MAX_GENERATION = 20_000;
    static final byte CHANCE_OF_CROSSOVER = 75;
    static final byte CHANCE_OF_MUTATION = 10;
    // RULES
    static final int SOMME_RESULT = 36;
    static final int PRODUIT_RESULT = 360;
    // VARIABLE
    static ArrayList<Byte[]> population = new ArrayList<Byte[]>();
    static boolean isPerfectResultFound = false;

    public static void main(String[]args) {
        Timestamp start = new Timestamp(System.currentTimeMillis());

        // GEN FIRST POPULATION
        for(int i = 0; i < POPULATION_SIZE; i++) {
            population.add(i, new Byte[DIMENSION]);
            for(int j = 0; j < DIMENSION; j++ ) {
                population.get(i)[j] = (getRand(0,100) > 50)? (byte)1 : (byte)0;
            }
        }

        // GENERATION
        for(int i = 0 ; i < MAX_GENERATION; i++ ) {
            for( int j = 1; j < POPULATION_SIZE / 2; j++) {
                Byte[] individuA = population.get(getRand(0,POPULATION_SIZE - 1));
                Byte[] individuB = population.get(getRand(0,POPULATION_SIZE - 1));
                if(getRand(0,100) < CHANCE_OF_CROSSOVER) {
                    population.add(croisement(individuA,individuB));
                }
                if(getRand(0,100) < CHANCE_OF_MUTATION) {
                    population.add(mutation(individuA));
                }
                if(getRand(0,100) < CHANCE_OF_MUTATION) {
                    population.add(mutation(individuB));
                }
                populationSelection();
                if(isPerfectResultFound){
                    break;
                }
            }
            if(isPerfectResultFound){
                break;
            }
        }
        Timestamp end = new Timestamp(System.currentTimeMillis());
        long diff = end.getTime() - start.getTime();
        System.out.println("Process in " + diff + " ms");
        printResult();
    }

    /**
     * Print Result
     */
    private static void printResult() {
        System.out.println("-- THE BEST RESULT IS : --");
        String sommeDev = "", produitDev = "", genDev = "";
        int sommeResult = 0, produitResult = 1;
        for(int i = 0; i < DIMENSION; i ++) {
            byte gen = population.get(0)[i];
            genDev += gen + "_";
            int value = (i + 1);
            if( gen == 0 ) {
                sommeDev += value + " + ";
                sommeResult += value;
            } else {
                produitDev += value + " * ";
                produitResult *= value;

            };
        }
        sommeDev = sommeDev.substring(0, sommeDev.length() - 2);
        produitDev = produitDev.substring(0, produitDev.length() - 2);
        genDev = genDev.substring(0, genDev.length() - 1);
        System.out.println("    GENETIQUE : " + genDev);
        System.out.println("    SOMME : " + sommeDev + " = " + sommeResult);
        System.out.println("    PRODUIT : " + produitDev + " = " + produitResult);
        System.out.println("    EVAL : " + evaluation(population.get(0)));
    }

    /**
     * Sort and Select a Sample of 30 new individu
     */
    private static void populationSelection() {
        population.sort((a, b) -> individuCompare(a,b));
        while(population.size() != POPULATION_SIZE){
            population.remove(population.size() - 1);
        }
    };

    /**
     * Comparaison function (for sorting purpose)
     * @param a individu A
     * @param b individu B
     * @return 0 if A == B | -1 if A < B | 1 if A > B
     */
    private static int individuCompare(Byte[] a, Byte[] b) {
        int aResult = evaluation(a);
        int bresult = evaluation(b);
        if(aResult < bresult) {
            return -1;
        } else if (aResult > bresult) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Eval the loss function of an individu
     * @param individu
     * @return the loss result
     */
    private static int evaluation(Byte[] individu){
        int somme = 0;
        int produit = 1;
        for(int i = 0; i < DIMENSION; i++) {
            int value = (i+1);
            if (individu[i] == 0) {
                somme += value;
            } else {
                produit *= value;
            }
        }
        int result = Math.abs(SOMME_RESULT - somme ) + Math.abs(PRODUIT_RESULT - produit);
        if(result == 0){
            isPerfectResultFound = true;
        }
        return result;
    };

    /**
     * Take two individus and apply a random slice to recreate a new one
     * @param a a first individu
     * @param b a seconde individu
     * @return c a mixed between the first and the second individu
     */
    private static Byte[] croisement(Byte[] a, Byte[] b) {
        Byte[] c = new Byte[DIMENSION];
        int randSlice = getRand(0,DIMENSION - 1);
        for(int i = 0; i < DIMENSION; i++) {
            c[i] = (i < randSlice)?a[i]:b[i];
        }
        return c;
    }

    /**
     * Take one individus and permute an index of is tab to recreate a new one
     * @param a a individu
     * @return a mixed between the first and the second individu
     */
    private static Byte[] mutation(Byte[] a) {
        Byte[] c = a.clone();
        int randomMutation = getRand(0, DIMENSION - 1);
        c[randomMutation] = (c[randomMutation] == (byte)1)? (byte)0 : (byte)1 ;
        return c;
    }

    /**
     * Randomize an Int
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
}
