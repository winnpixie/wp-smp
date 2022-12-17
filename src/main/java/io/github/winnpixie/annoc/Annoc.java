package io.github.winnpixie.annoc;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * An API for the Bukkit API that allows {@link ConfigurationSection} values to be quickly and
 * easily accessed through normal Java fields linked by the {@link Link} annotation.
 *
 * @author Hannah
 * @since 0.0.1
 */
public class Annoc {
    private ConfigurationSection configuration;
    private final List<WrappedField> wrappedFields = new ArrayList<>(); // or CopyOnWriteArrayList, concurrency go brrrr

    public Annoc() {
    }

    /**
     * Constructs an AnnotatedConfig with a pre-defined {@link ConfigurationSection}
     *
     * @param configuration
     */
    public Annoc(@NotNull ConfigurationSection configuration) {
        this.configuration = configuration;
    }

    /**
     * @return The current {@link ConfigurationSection} this instance loaded from
     */
    public ConfigurationSection getConfiguration() {
        return configuration;
    }

    public Annoc setConfiguration(@NotNull ConfigurationSection configuration) {
        this.configuration = configuration;
        return this;
    }

    public List<WrappedField> getWrappedFields() {
        return wrappedFields;
    }

    /**
     * Creates {@link WrappedField}s from the specified owning object's instance fields.
     *
     * @param owner
     * @return
     */
    public Annoc wrapInstance(@NotNull Object owner) {
        for (Field field : owner.getClass().getDeclaredFields()) {
            if (!field.isAnnotationPresent(Link.class)) continue;
            if (Modifier.isStatic(field.getModifiers())) continue;

            this.addWrappedField(new WrappedField(owner, field));
        }

        return this;
    }

    /**
     * Creates {@link WrappedField}s from the specified class's static fields.
     *
     * @param cls
     * @return
     */
    public Annoc wrapClass(@NotNull Class<?> cls) {
        for (Field field : cls.getDeclaredFields()) {
            if (!field.isAnnotationPresent(Link.class)) continue;
            if (!Modifier.isStatic(field.getModifiers())) continue;

            this.addWrappedField(new WrappedField(null, field));
        }

        return this;
    }

    /**
     * Creates {@link WrappedField}s from the specified owning object's fields, static and instance.
     */
    public Annoc wrap(@NotNull Object owner) {
        for (Field field : owner.getClass().getDeclaredFields()) {
            if (!field.isAnnotationPresent(Link.class)) continue;

            this.addWrappedField(new WrappedField(owner, field));
        }

        return this;
    }

    private boolean addWrappedField(@NotNull WrappedField wcf) {
        return wrappedFields.add(wcf);
    }

    private boolean removeWrappedField(@NotNull WrappedField wcf) {
        return wrappedFields.remove(wcf);
    }

    public void load() {
        for (WrappedField wf : wrappedFields) {
            if (!wf.tryGet(def -> wf.trySet(configuration.get(wf.getPath(), def)))) {
                wf.trySet(configuration.get(wf.getPath()));
            }
        }
    }
}
