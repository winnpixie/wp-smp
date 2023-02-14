package io.github.winnpixie.smp.utilities;

import java.util.concurrent.ThreadLocalRandom;

public class MathHelper {
    private static final ThreadLocalRandom LOCAL_RANDOM = ThreadLocalRandom.current();

    // min-inclusive, max-exclusive
    public static double getRandom(double min, double max) {
        return LOCAL_RANDOM.nextDouble(min, max);
    }
}
