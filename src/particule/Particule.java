package particule;

public class Particule {
    float positionCourante[];
    float meilleurPosition[];
    float velocite[];
    float costPositionCourante;
    float costMeilleurPosition;

    Particule(int dimension) {
        positionCourante = new float[dimension];
        meilleurPosition = new float[dimension];
        velocite = new float[dimension];
    }
}
