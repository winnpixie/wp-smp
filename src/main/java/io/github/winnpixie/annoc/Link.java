package io.github.winnpixie.annoc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker class for configuration fields
 *
 * @author Hannah
 * @since 0.0.1
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Link {
    /**
     * The (full) path the configuration key.
     * ie. {@code a.b.c}, where {@code a.b} is the path and {@code c} = the name of the key
     *
     * @return The full path
     */
    String path();
}
