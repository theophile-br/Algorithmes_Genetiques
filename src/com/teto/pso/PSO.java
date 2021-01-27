package com.teto.pso;

import com.teto.pso.models.Particule;

import java.util.Random;

public class PSO {
    // ---------- PARAMETRE ---------- //
    public static final int NOMBRE_DE_PARTICULE = 40;
    public static final int NOMBRE_DE_VOISIN = 4;
    public static final int MAX_ITERRATION = 10_000;

    public static final double[] DELTA_VITESSE = {-4.0, 4.0};

    public static final int FIT_VOULUE = 0;
    public static final int DIMENSION = 4;

    public static final double[] BORNE = {-5.12, 5.12};

    public static final int C1 = 2;
    public static final int C2 = 2;
    // ---------- VARIABLES ---------- //
    public static Particule[] particules;

    // ---------- MAIN ---------- //
    public static void main(String[] args) {
        initializeParticule();
        algoPSO();
        Particule bestParticule = getTheBestParticule();
        System.out.println("The Best Fit is : " + bestParticule.fBest);
    }

    // ---------- STEPS ---------- //
    public static void initializeParticule() {
        particules = new Particule[NOMBRE_DE_PARTICULE];
        for(int i = 0; i < NOMBRE_DE_PARTICULE; i++) {
            particules[i] = new Particule(DIMENSION);
            for(int d = 0; d < DIMENSION; d++){
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
        for(int i = 0; i < DIMENSION; i++) {
            double x = particule.p[i];
            somme += x * x  - 10 * Math.cos(2 * Math.PI * x);
        }
        return 10 * DIMENSION + somme;
    }

    public static void algoPSO(){
        int current_iteration = 1;
        boolean not_stop = false;
        while( current_iteration <= MAX_ITERRATION && !not_stop) {
            for(Particule particule: particules) {
                Particule meilleurVoisin = obtenirMeilleurVoisin(particule);
                for(int i = 0; i < DIMENSION; i++) {
                    particule.v[i] = updateSpeed(particule,meilleurVoisin,i);
                    particule.p[i] = updatePosition(particule,i);
                }
            }
            for(Particule particule: particules) {
                double fitness = calculFitness(particule);
                if(fitness < particule.fBest) {
                    particule.fBest = fitness;
                    for(int i = 0; i < DIMENSION; i++) {
                        particule.pBest[i] = particule.p[i];
                    }
                }
                if(fitness == FIT_VOULUE){
                    not_stop = true;
                }
            }
            current_iteration++;
        }
    }

    public static Particule getTheBestParticule() {
        Particule bestVoisin = null;
        for(Particule voisin: particules) {
            if (bestVoisin == null || bestVoisin.fBest >= voisin.fBest) {
                bestVoisin = voisin;
            }
        }
        return bestVoisin;
    }

    public static Particule obtenirMeilleurVoisin(Particule particule) {
        Particule bestVoisin = null;
        for(Particule voisin: particules) {
            if(particule.equals(voisin)) {
                continue;
            }
            if (bestVoisin == null || bestVoisin.f >= voisin.f) {
                bestVoisin = voisin;
            }
        }
        return bestVoisin;
    }

    public static double updateSpeed(Particule particule, Particule meilleurVoisin, int d) {
        int r1 = C1 * getRandomZeroOrOne();
        int r2 = C2 * getRandomZeroOrOne();
        double newSpeed = particule.v[d] +
                r1 * (particule.pBest[d] - particule.p[d]) +
                r2 * (meilleurVoisin.pBest[d] - particule.p[d]);
        if(newSpeed > DELTA_VITESSE[1] ){
            return  DELTA_VITESSE[1];
        } else if (newSpeed < DELTA_VITESSE[0]) {
            return DELTA_VITESSE[0];
        }
        return newSpeed;
    }

    public static double updatePosition(Particule particule, int d) {
        double newPosition = particule.p[d] + particule.v[d];
        if(newPosition > BORNE[1] ){
            return  BORNE[1];
        } else if (newPosition < BORNE[0]) {
            return BORNE[0];
        }
        return newPosition;
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

    private static double getRandomPosition(){
        return getRandom(BORNE[0],BORNE[1]);
    }

    private static double getRandomVitesse(){
        return getRandom(DELTA_VITESSE[0],DELTA_VITESSE[1]);
    }
}

// https://github.com/vfrz/CombinatorialOptimization/blob/master/src/ParticleSwarmOptimization/ParticleSwarmOptimizationAlgorithm.cs
// http://www.mage.fst.uha.fr/idoumghar/master2/pso.c