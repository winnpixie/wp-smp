package io.github.winnpixie.smp.utilities;

import java.util.concurrent.ThreadLocalRandom;

public class MathHelper {
    private static final ThreadLocalRandom random = ThreadLocalRandom.current();

    // min-inclusive, max-exclusive
    public static double getRandom(double min, double max) {
        return random.nextDouble(min, max);
    }
}
