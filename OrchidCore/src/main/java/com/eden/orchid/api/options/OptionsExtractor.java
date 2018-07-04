package com.eden.orchid.api.options;

import com.eden.common.util.EdenPair;
import com.eden.common.util.EdenUtils;
import com.eden.krow.KrowTable;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.options.annotations.Option;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Singleton
public class OptionsExtractor extends Extractor {

    private final OrchidContext context;

    @Inject
    public OptionsExtractor(OrchidContext context, Set<OptionExtractor> extractors, OptionsValidator validator) {
        super(extractors, validator);
        this.context = context;
    }

    public void extractOptions(OptionsHolder optionsHolder, Map<String, Object> options) {
        super.extractOptions(optionsHolder, options);
    }

    @Override
    protected <T> T getInstance(Class<T> clazz) {
        return context.resolve(clazz);
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

// Describe Options
//----------------------------------------------------------------------------------------------------------------------

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
            optionDescriptions.add(new OptionsDescription(fields.first.getName(), Map.class, "All options passed to this object.", "{}"));
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
