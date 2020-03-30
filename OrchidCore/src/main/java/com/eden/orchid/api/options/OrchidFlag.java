package com.eden.orchid.api.options;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.options.annotations.Archetype;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.options.annotations.FlagAliases;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.options.annotations.Protected;
import com.eden.orchid.api.options.archetypes.EnvironmentVariableArchetype;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

@Archetype(value = EnvironmentVariableArchetype.class, key = "")
public abstract class OrchidFlag {

    public Map<String, FlagDescription> describeFlags() {
        Map<String, FlagDescription> flagDescriptions = new HashMap<>();

        for(Field field : this.getClass().getFields()) {
            if(field.isAnnotationPresent(Option.class)) {
                String flagKey = (!EdenUtils.isEmpty(field.getAnnotation(Option.class).value()))
                        ? field.getAnnotation(Option.class).value()
                        : field.getName();
                String[] aliases = (field.isAnnotationPresent(FlagAliases.class))
                        ? field.getAnnotation(FlagAliases.class).value()
                        : null;
                String description = (field.isAnnotationPresent(Description.class))
                        ? field.getAnnotation(Description.class).value()
                        : null;

                try {
                    FlagDescription des = new FlagDescription(
                            field.getType().getSimpleName(),
                            flagKey,
                            aliases,
                            description
                    );

                    flagDescriptions.put(des.key, des);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return flagDescriptions;
    }

    public Map<String, Value> getParsedFlags() {
        Map<String, Value> flagValues = new HashMap<>();

        for(Field field : this.getClass().getFields()) {
            if(field.isAnnotationPresent(Option.class)) {
                String flagKey = (!EdenUtils.isEmpty(field.getAnnotation(Option.class).value()))
                        ? field.getAnnotation(Option.class).value()
                        : field.getName();
                try {
                    Value value = new Value(
                            field.getType(),
                            flagKey,
                            field.get(this),
                            field.isAnnotationPresent(Protected.class)
                    );

                    flagValues.put(value.key, value);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return flagValues;
    }

    final void loadFlagNames(BiConsumer<String, String[]> consumer) {
        for(Field field : this.getClass().getFields()) {
            if(field.isAnnotationPresent(Option.class)) {
                String flagKey = (!EdenUtils.isEmpty(field.getAnnotation(Option.class).value()))
                        ? field.getAnnotation(Option.class).value()
                        : field.getName();

                String[] aliases = (field.isAnnotationPresent(FlagAliases.class))
                        ? field.getAnnotation(FlagAliases.class).value()
                        : null;

                consumer.accept(flagKey, aliases);
            }
        }
    }

    public static final class FlagDescription {

        public final String type;
        public final String key;
        public final String[] aliases;
        public final String description;

        public FlagDescription(String type, String key, String[] aliases, String description) {
            this.type = type;
            this.key = key;
            this.aliases = aliases;
            this.description = description;
        }

        public String getType() {
            return this.type;
        }

        public String getKey() {
            return this.key;
        }

        public String[] getAliases() {
            return this.aliases;
        }

        public String getDescription() {
            return this.description;
        }
    }

    public static final class Value {

        private final Class<?> type;
        private final String key;
        private final Object value;

        private final boolean isProtected;

        public Value(Class<?> type, String key, Object value, boolean isProtected) {
            this.type = type;
            this.key = key;
            this.value = value;
            this.isProtected = isProtected;
        }

        public Class<?> getType() {
            if(type.equals(byte.class))   { return Byte.class; }
            if(type.equals(short.class))  { return Short.class; }
            if(type.equals(int.class))    { return Integer.class; }
            if(type.equals(long.class))   { return Long.class; }
            if(type.equals(float.class))  { return Float.class; }
            if(type.equals(double.class)) { return Double.class; }

            return type;
        }

        public String getKey() {
            return this.key;
        }

        public Object getValue() {
            return this.value;
        }

        public boolean isProtected() {
            return this.isProtected;
        }
    }

}
