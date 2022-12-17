package io.github.winnpixie.annoc;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.function.Consumer;

/**
 * Wraps a {@link Field} annotated with @{@link Link} into an easy-to-use package
 *
 * @author Hannah
 * @since 0.0.1
 */
public class WrappedField {
    private final String path;
    private final Object owner;
    private final Field field;

    /**
     * @param owner field's owning instance, can be null if field has a static modifier.
     * @param field field's instance for this {@code WrappedField} object to manage.
     * @throws IllegalArgumentException if field's instance has a final modifier.
     * @throws SecurityException        if field has a non-public modifier and can not be made accessible.
     */
    public WrappedField(Object owner, Field field) {
        // Ensure field is not final.
        if (Modifier.isFinal(field.getModifiers())) {
            throw new IllegalArgumentException("A field annotated with @Link can NOT be final!");
        }

        this.path = field.getAnnotation(Link.class).path();
        this.owner = owner;
        this.field = field;

        if (!field.canAccess(owner)) {
            field.trySetAccessible();
        }
    }

    public String getPath() {
        return path;
    }

    public Object getOwner() {
        return owner;
    }

    public Field getField() {
        return field;
    }

    public boolean tryGet(Consumer<Object> callback) {
        try {
            callback.accept(field.get(owner));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean trySet(Object value) {
        try {
            field.set(owner, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
