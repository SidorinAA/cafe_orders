package util;

import java.util.Random;

public class RandomUtil {

    private static final Random RANDOM = new Random();

    private RandomUtil() {
    }

    public static int getCountProducts(int max) {
        return RANDOM.nextInt(max);
    }
}
