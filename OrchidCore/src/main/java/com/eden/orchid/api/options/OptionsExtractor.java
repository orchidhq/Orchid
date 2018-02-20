package com.eden.orchid.api.options;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.json.JSONElement;
import com.eden.common.util.EdenPair;
import com.eden.common.util.EdenUtils;
import com.eden.krow.KrowTable;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.annotations.Archetype;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.options.annotations.OptionsData;
import com.eden.orchid.api.registration.Prioritized;
import com.eden.orchid.utilities.OrchidUtils;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Singleton
public class OptionsExtractor {

    private final OrchidContext context;
    private final List<OptionExtractor> extractors;

    @Inject
    public OptionsExtractor(OrchidContext context, Set<OptionExtractor> extractors) {
        this.context = context;
        this.extractors = new ArrayList<>(extractors);
        this.extractors.sort(Comparator.comparing(Prioritized::getPriority).reversed());
    }

    public void extractOptions(OptionsHolder optionsHolder, JSONObject options) {
        // setup initial options
        JSONObject initialOptions = new JSONObject(options.toMap());
        JSONObject archetypalOptions = loadArchetypalData(optionsHolder, initialOptions);

        JSONObject actualOptions = OrchidUtils.merge(archetypalOptions, initialOptions);

        // extract options fields
        EdenPair<Field, Set<Field>> fields = findOptionFields(optionsHolder.getClass());

        if(fields.first != null) {
            setOptionValue(optionsHolder, fields.first, fields.first.getName(), JSONElement.class, new JSONElement(actualOptions));
        }

        for (Field field : fields.second) {
            String key = (!EdenUtils.isEmpty(field.getAnnotation(Option.class).value()))
                    ? field.getAnnotation(Option.class).value()
                    : field.getName();

            if (field.getType().isArray()) {
                setOptionArray(optionsHolder, field, actualOptions, key);
            }
            else if (List.class.isAssignableFrom(field.getType())) {
                setOptionArray(optionsHolder, field, actualOptions, key);
            }
            else {
                setOption(optionsHolder, field, actualOptions, key);
            }
        }
    }

    public boolean hasOptions(Object possibleObjectHolder) {
        return hasOptions(possibleObjectHolder, true, true);
    }

    public boolean hasOptions(Object possibleObjectHolder, boolean includeOwnOptions, boolean includeInheritedOptions) {
        if(possibleObjectHolder instanceof OptionsHolder) {
            EdenPair<Field, Set<Field>> fields = findOptionFields(possibleObjectHolder.getClass(), includeOwnOptions, includeInheritedOptions);
            return fields.second.size() > 0;
        }
        else if(possibleObjectHolder instanceof Class) {
            if(OptionsHolder.class.isAssignableFrom((Class) possibleObjectHolder)) {
                EdenPair<Field, Set<Field>> fields = findOptionFields((Class) possibleObjectHolder, includeOwnOptions, includeInheritedOptions);
                return fields.second.size() > 0;
            }
        }

        return false;
    }

// Find Options
//----------------------------------------------------------------------------------------------------------------------

    private EdenPair<Field, Set<Field>> findOptionFields(Class<?> optionsHolderClass) {
        return findOptionFields(optionsHolderClass, true, true);
    }

    private EdenPair<Field, Set<Field>> findOptionFields(Class<?> optionsHolderClass, boolean includeOwnOptions, boolean includeInheritedOptions) {
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

    public List<String> getOptionNames(Class<?> optionsHolderClass) {
        EdenPair<Field, Set<Field>> fields = findOptionFields(optionsHolderClass);

        List<String> optionNames = new ArrayList<>();

        for (Field field : fields.second) {
            String key = (!EdenUtils.isEmpty(field.getAnnotation(Option.class).value()))
                    ? field.getAnnotation(Option.class).value()
                    : field.getName();

            optionNames.add(key);
        }

        return optionNames;
    }

    public List<String> getOptionNames(Class<?> optionsHolderClass, boolean includeOwnOptions, boolean includeInheritedOptions) {
        EdenPair<Field, Set<Field>> fields = findOptionFields(optionsHolderClass, includeOwnOptions, includeInheritedOptions);

        List<String> optionNames = new ArrayList<>();

        for (Field field : fields.second) {
            String key = (!EdenUtils.isEmpty(field.getAnnotation(Option.class).value()))
                    ? field.getAnnotation(Option.class).value()
                    : field.getName();

            optionNames.add(key);
        }

        return optionNames;
    }

// Options Archetypes
//----------------------------------------------------------------------------------------------------------------------

    private JSONObject loadArchetypalData(Object target, JSONObject actualOptions) {
        Class<?> optionsHolderClass = target.getClass();
        JSONObject allAdditionalData = new JSONObject();

        List<Archetype> archetypeAnnotations = new ArrayList<>();

        while (optionsHolderClass != null) {
            Collections.addAll(archetypeAnnotations, optionsHolderClass.getAnnotationsByType(Archetype.class));
            optionsHolderClass = optionsHolderClass.getSuperclass();
        }

        Collections.reverse(archetypeAnnotations);

        for(Archetype archetype : archetypeAnnotations) {
            OptionArchetype archetypeDataProvider = context.getInjector().getInstance(archetype.value());

            JSONObject archetypeConfiguration;
            if(actualOptions.has(archetype.key()) && actualOptions.get(archetype.key()) instanceof JSONObject) {
                archetypeConfiguration = actualOptions.getJSONObject(archetype.key());
            }
            else {
                archetypeConfiguration = new JSONObject();
            }

            archetypeDataProvider.extractOptions(context, archetypeConfiguration);
            JSONObject archetypalData = archetypeDataProvider.getOptions(target, archetype.key());

            if(archetypalData != null) {
                allAdditionalData = OrchidUtils.merge(allAdditionalData, archetypalData);
            }
        }

        return allAdditionalData;
    }

// Set option values
//----------------------------------------------------------------------------------------------------------------------

    private void setOptionArray(OptionsHolder optionsHolder, Field field, JSONObject options, String key) {
        boolean foundExtractor = false;
        for (OptionExtractor extractor : extractors) {
            if (extractor.acceptsClass(field.getType())) {
                setOptionValue(optionsHolder, field, key, field.getType(), extractor.getArray(field, options, key));
                foundExtractor = true;
                break;
            }
        }

        if (!foundExtractor) {
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

        if (!foundExtractor) {
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

// Describe Options
//----------------------------------------------------------------------------------------------------------------------

    public OptionHolderDescription describeOptions(Class<?> optionsHolderClass, boolean includeOwnOptions, boolean includeInheritedOptions) {
        return describeOptions(optionsHolderClass, findOptionFields(optionsHolderClass, includeOwnOptions, includeInheritedOptions));
    }

    public OptionHolderDescription describeAllOptions(Class<?> optionsHolderClass) {
        return describeOptions(optionsHolderClass, findOptionFields(optionsHolderClass, true, true));
    }

    public OptionHolderDescription describeOwnOptions(Class<?> optionsHolderClass) {
        return describeOptions(optionsHolderClass, findOptionFields(optionsHolderClass, true, false));
    }

    public OptionHolderDescription describeInheritedOptions(Class<?> optionsHolderClass) {
        return describeOptions(optionsHolderClass, findOptionFields(optionsHolderClass, false, true));
    }

    private OptionHolderDescription describeOptions(Class<?> optionsHolderClass, EdenPair<Field, Set<Field>> fields) {
        List<OptionsDescription> optionDescriptions = new ArrayList<>();

        if(fields.first != null) {
            optionDescriptions.add(new OptionsDescription(fields.first.getName(), JSONElement.class, "All options passed to this object.", "{}"));
        }

        for (Field field : fields.second) {
            String key = (!EdenUtils.isEmpty(field.getAnnotation(Option.class).value()))
                    ? field.getAnnotation(Option.class).value()
                    : field.getName();
            String description = (field.getAnnotation(Description.class) != null && !EdenUtils.isEmpty(field.getAnnotation(Description.class).value()))
                    ? field.getAnnotation(Description.class).value()
                    : "";
            String defaultValue = "N/A";

            for (OptionExtractor extractor : extractors) {
                if (extractor.acceptsClass(field.getType())) {
                    defaultValue = extractor.describeDefaultValue(field);
                    break;
                }
            }

            optionDescriptions.add(new OptionsDescription(key, field.getType(), description, defaultValue));
        }

        optionDescriptions.sort(Comparator.comparing(OptionsDescription::getKey));

        String classDescription = (optionsHolderClass.isAnnotationPresent(Description.class))
                ? optionsHolderClass.getAnnotation(Description.class).value()
                : "";


        Clog.v("Class {} description: {}", optionsHolderClass.getSimpleName(), classDescription);

        return new OptionHolderDescription(classDescription, optionDescriptions);
    }

    public KrowTable getDescriptionTable(OptionHolderDescription optionsHolderDescription) {
        KrowTable table = new KrowTable();

        List<OptionsDescription> options = optionsHolderDescription.getOptionsDescriptions();

        options.forEach(option -> {
            table.cell("Type", option.getKey(), cell -> {cell.setContent(option.getOptionType().getSimpleName()); return null;});
            table.cell("Default Value", option.getKey(), cell -> {cell.setContent(option.getDefaultValue()); return null;});
            table.cell("Description", option.getKey(), cell -> {cell.setContent(option.getDescription()); return null;});
        });

        table.column("Description", cell -> {cell.setWrapTextAt(45); return null;});
        table.column("Type", cell -> {cell.setWrapTextAt(15); return null;});
        table.column("Default Value", cell -> {cell.setWrapTextAt(15); return null;});

        return table;
    }

}
