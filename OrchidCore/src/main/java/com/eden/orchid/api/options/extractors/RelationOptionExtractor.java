package com.eden.orchid.api.options.extractors;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.OptionExtractor;
import com.eden.orchid.api.options.Relation;
import com.eden.orchid.api.options.annotations.RelationConfig;
import com.google.inject.Provider;
import org.json.JSONObject;

import javax.inject.Inject;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @orchidApi optionTypes
 * @since v1.0.0
 */
public final class RelationOptionExtractor extends OptionExtractor<Relation> {

    private final Provider<OrchidContext> contextProvider;

    @Inject
    public RelationOptionExtractor(Provider<OrchidContext> contextProvider) {
        super(1000);
        this.contextProvider = contextProvider;
    }

    @Override
    public boolean acceptsClass(Class clazz) {
        return Relation.class.isAssignableFrom(clazz);
    }

    @Override
    public Relation getOption(Field field, Object sourceObject, String key) {
        return loadRelation(field, sourceObject);
    }

    @Override
    public Relation getDefaultValue(Field field) {
        return loadRelation(field, null);
    }

    private Relation loadRelation(final Field field, final Object sourceObject) {
        Relation relation = (Relation) contextProvider.get().resolve(field.getType());
        Map<String, Object> relationConfig = new HashMap<>();

        RelationConfig configAnnotation = field.getAnnotation(RelationConfig.class);
        if (configAnnotation != null) {
            if (!EdenUtils.isEmpty(configAnnotation.collectionType())) {
                relationConfig.put("collectionType", configAnnotation.collectionType());
            }
            if (!EdenUtils.isEmpty(configAnnotation.collectionId())) {
                relationConfig.put("collectionId", configAnnotation.collectionId());
            }
            if (!EdenUtils.isEmpty(configAnnotation.itemId())) {
                relationConfig.put("itemId", configAnnotation.itemId());
            }
        }

        if (sourceObject instanceof String) {
            relationConfig = EdenUtils.merge(relationConfig, relation.parseStringRef((String) sourceObject));
        }
        else if (sourceObject instanceof JSONObject) {
            relationConfig = EdenUtils.merge(relationConfig, ((JSONObject) sourceObject).toMap());
        }
        else if (sourceObject instanceof Map) {
            relationConfig = EdenUtils.merge(relationConfig, (Map<String, Object>) sourceObject);
        }

        relation.setRef(relationConfig);

        return relation;
    }

}
