package com.eden.orchid.api.options;

import com.eden.common.util.EdenUtils;
import org.json.JSONObject;

import javax.inject.Inject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OptionsExtractor {

    private final Set<OptionExtractor> extractors;

    @Inject
    public OptionsExtractor(Set<OptionExtractor> extractors) {
        this.extractors = extractors;
    }

    public void extractOptions(OptionsHolder optionsHolder, JSONObject options) {
        Set<Field> fields = findOptionFields(optionsHolder);

        for (Field field : fields) {
            String key = (!EdenUtils.isEmpty(field.getAnnotation(Option.class).value()))
                    ? field.getAnnotation(Option.class).value()
                    : field.getName();

            if (field.getType().isArray()) {
                setOptionArray(optionsHolder, field, options, key);
            }
            else if (List.class.isAssignableFrom(field.getType())) {
                setOptionArray(optionsHolder, field, options, key);
            }
            else {
                setOption(optionsHolder, field, options, key);
            }
        }
    }

    public List<OptionsDescription> describeOptions(OptionsHolder optionsHolder) {
        Set<Field> fields = findOptionFields(optionsHolder);

        List<OptionsDescription> optionDescriptions = new ArrayList<>();

        for (Field field : fields) {
            String key = (!EdenUtils.isEmpty(field.getAnnotation(Option.class).value()))
                    ? field.getAnnotation(Option.class).value()
                    : field.getName();
            String description = (field.getAnnotation(Description.class) != null && !EdenUtils.isEmpty(field.getAnnotation(Description.class).value()))
                    ? field.getAnnotation(Description.class).value()
                    : "";

            optionDescriptions.add(new OptionsDescription(key, field.getType(), description));
        }

        return optionDescriptions;
    }

    private Set<Field> findOptionFields(OptionsHolder optionsHolder) {
        Class<?> optionsHolderClass = optionsHolder.getClass();

        Set<Field> fields = new HashSet<>();

        while (optionsHolderClass != null) {
            Field[] declaredFields = optionsHolderClass.getDeclaredFields();
            if (!EdenUtils.isEmpty(declaredFields)) {
                for (Field field : declaredFields) {
                    if (field.isAnnotationPresent(Option.class)) {
                        fields.add(field);
                    }
                }
            }

            optionsHolderClass = optionsHolderClass.getSuperclass();
        }

        return fields;
    }

    private void setOptionArray(OptionsHolder optionsHolder, Field field, JSONObject options, String key) {
        boolean foundExtractor = false;
        for (OptionExtractor extractor : extractors) {
            if (extractor.acceptsClass(field.getType())) {
                setOptionValue(optionsHolder, field, key, field.getType(), extractor.getArray(field, options, key));
                foundExtractor = true;
                break;
            }
        }

        if(!foundExtractor) {
            setOptionValue(optionsHolder, field, key, field.getType(), null);
        }
    }

    private void setOption(OptionsHolder optionsHolder, Field field, JSONObject options, String key) {
        boolean foundExtractor = false;
        for (OptionExtractor extractor : extractors) {
            if (extractor.acceptsClass(field.getType())) {
                Object object = extractor.getOption(field, options, key);
                setOptionValue(optionsHolder, field, key, field.getType(), object);
                foundExtractor = true;
                break;
            }
        }

        if(!foundExtractor) {
            setOptionValue(optionsHolder, field, key, field.getType(), null);
        }
    }

    private void setOptionValue(OptionsHolder optionsHolder, Field field, String key, Class<?> objectClass, Object value) {
        try {
            String setterMethodName = "set" + key.substring(0, 1).toUpperCase() + key.substring(1);
            Method method = optionsHolder.getClass().getMethod(setterMethodName, objectClass);
            method.invoke(optionsHolder, value);
            return;

        }
        catch (NoSuchMethodException e) {
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        try {
            field.set(optionsHolder, value);
        }
        catch (IllegalAccessException e) {
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
