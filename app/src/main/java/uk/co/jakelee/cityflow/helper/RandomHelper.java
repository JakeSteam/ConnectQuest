package uk.co.jakelee.cityflow.helper;

public class RandomHelper {
    public static int getNumber(int min, int max) {
        return min + (int) (Math.random() * ((max - min) + 1));
    }
}
