package com.eden.orchid.api.options;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.json.JSONElement;
import com.eden.common.util.EdenPair;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.options.annotations.Archetype;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.options.annotations.OptionsData;
import com.eden.orchid.api.registration.Prioritized;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class Extractor {

    protected final List<OptionExtractor> extractors;
    private final OptionsValidator validator;

    public Extractor(Collection<OptionExtractor> extractors, OptionsValidator validator) {
        this.extractors = new ArrayList<>(extractors);
        this.extractors.sort(Comparator.comparing(Prioritized::getPriority).reversed());
        this.validator = validator;
    }

    public final void extractOptions(Object optionsHolder, JSONObject options) {
        // setup initial options
        JSONObject initialOptions = (options != null) ? new JSONObject(options.toMap()) : new JSONObject();
        JSONObject archetypalOptions = loadArchetypalData(optionsHolder, initialOptions);

        JSONObject actualOptions = EdenUtils.merge(archetypalOptions, initialOptions);

        // extract options fields
        EdenPair<Field, Set<Field>> fields = findOptionFields(optionsHolder.getClass());

        if(fields.first != null) {
            setOptionValue(optionsHolder, fields.first, fields.first.getName(), JSONElement.class, new JSONElement(actualOptions));
        }

        for (Field field : fields.second) {
            String fieldOptionKey = (!EdenUtils.isEmpty(field.getAnnotation(Option.class).value()))
                    ? field.getAnnotation(Option.class).value()
                    : field.getName();

            setOption(optionsHolder, field, actualOptions, fieldOptionKey);
        }

        if(validator != null) {
            try {
                validator.validate(optionsHolder);
            }
            catch (Exception e) {
                Clog.e("{} did not pass validation", optionsHolder, e);
                throw new IllegalStateException(Clog.format("{} did not pass validation", optionsHolder, e));
            }
        }
    }

// Find Options
//----------------------------------------------------------------------------------------------------------------------

    protected final EdenPair<Field, Set<Field>> findOptionFields(Class<?> optionsHolderClass) {
        return findOptionFields(optionsHolderClass, true, true);
    }

    protected final EdenPair<Field, Set<Field>> findOptionFields(Class<?> optionsHolderClass, boolean includeOwnOptions, boolean includeInheritedOptions) {
        Field optionsDataField = null;
        Set<Field> fields = new HashSet<>();

        int i = 0;
        while (optionsHolderClass != null) {
            boolean shouldGetOptions = true;
            if(i == 0) {
                if(!includeOwnOptions) {
                    shouldGetOptions = false;
                }
            }
            else {
                if(!includeInheritedOptions) {
                    shouldGetOptions = false;
                }
            }

            if(shouldGetOptions) {
                Field[] declaredFields = optionsHolderClass.getDeclaredFields();
                if (!EdenUtils.isEmpty(declaredFields)) {
                    for (Field field : declaredFields) {
                        if (field.isAnnotationPresent(Option.class)) {
                            fields.add(field);
                        } else if (field.isAnnotationPresent(OptionsData.class) && field.getType().equals(JSONElement.class)) {
                            optionsDataField = field;
                        }
                    }
                }
            }

            optionsHolderClass = optionsHolderClass.getSuperclass();
            i++;
        }

        return new EdenPair<>(optionsDataField, fields);
    }

// Options Archetypes
//----------------------------------------------------------------------------------------------------------------------

    protected final JSONObject loadArchetypalData(Object target, JSONObject actualOptions) {
        Class<?> optionsHolderClass = target.getClass();
        JSONObject allAdditionalData = new JSONObject();

        List<Archetype> archetypeAnnotations = new ArrayList<>();

        while (optionsHolderClass != null) {
            Collections.addAll(archetypeAnnotations, optionsHolderClass.getAnnotationsByType(Archetype.class));
            optionsHolderClass = optionsHolderClass.getSuperclass();
        }

        Collections.reverse(archetypeAnnotations);

        for(Archetype archetype : archetypeAnnotations) {
            OptionArchetype archetypeDataProvider = getInstance(archetype.value());

            JSONObject archetypeConfiguration;
            if(actualOptions.has(archetype.key()) && actualOptions.get(archetype.key()) instanceof JSONObject) {
                archetypeConfiguration = actualOptions.getJSONObject(archetype.key());
            }
            else {
                archetypeConfiguration = new JSONObject();
            }

            Extractor extractor = getInstance(Extractor.class);
            extractor.extractOptions(archetypeDataProvider, archetypeConfiguration);
            JSONObject archetypalData = archetypeDataProvider.getOptions(target, archetype.key());

            if(archetypalData != null) {
                allAdditionalData = EdenUtils.merge(allAdditionalData, archetypalData);
            }
        }

        return allAdditionalData;
    }

// Set option values
//----------------------------------------------------------------------------------------------------------------------

    protected final void setOption(Object optionsHolder, Field field, JSONObject options, String key) {
        boolean foundExtractor = false;
        for (OptionExtractor extractor : extractors) {
            if (extractor.acceptsClass(field.getType())) {

                Object sourceObject = null;
                Object resultObject = null;

                if(options.has(key)) {
                    sourceObject = options.get(key);
                    resultObject = extractor.getOption(field, sourceObject, key);
                    if(extractor.isEmptyValue(resultObject)) {
                        resultObject = extractor.getDefaultValue(field);
                    }
                }
                else {
                    resultObject = extractor.getDefaultValue(field);
                }

                setOptionValue(optionsHolder, field, key, field.getType(), resultObject);
                foundExtractor = true;
                break;
            }
        }

        if (!foundExtractor) {
            setOptionValue(optionsHolder, field, key, field.getType(), null);
        }
    }

    protected final void setOptionValue(Object optionsHolder, Field field, String key, Class<?> objectClass, Object value) {
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
            return;
        }
        catch (IllegalAccessException e) {
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        Clog.e("Options field {} in class {} is inaccessible. Make sure the field is public or has a bean-style setter method", key, optionsHolder.getClass().getSimpleName());
    }

// Description
//----------------------------------------------------------------------------------------------------------------------

    public String describeOption(Class<?> optionsHolderClass, String optionKey) {
        EdenPair<Field, Set<Field>> fields = findOptionFields(optionsHolderClass, true, true);

        Field optionField = null;
        for (Field field : fields.second) {
            String fieldOptionKey = (!EdenUtils.isEmpty(field.getAnnotation(Option.class).value()))
                    ? field.getAnnotation(Option.class).value()
                    : field.getName();

            if(fieldOptionKey.equals(optionKey)) {
                optionField = field;
                break;
            }
        }

        if(optionField != null) {
            for (OptionExtractor extractor : extractors) {
                if (extractor.acceptsClass(optionField.getType())) {
                    return extractor.describeDefaultValue(optionField);
                }
            }
        }

        return "";
    }

// Utils
//----------------------------------------------------------------------------------------------------------------------

    protected abstract <T> T getInstance(Class<T> clazz);

}
