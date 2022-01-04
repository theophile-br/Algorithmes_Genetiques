package particule;

public class Particule {
    float positionCourante[];
    float meilleurPosition[];
    float vitesse[];
    float costPositionCourante;
    float costMeilleurPosition;

    Particule(int dimension) {
        positionCourante = new float[dimension];
        meilleurPosition = new float[dimension];
        vitesse = new float[dimension];
    }
}
