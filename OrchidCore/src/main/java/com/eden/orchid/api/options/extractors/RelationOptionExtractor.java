package com.eden.orchid.api.options.extractors;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.OptionExtractor;
import com.eden.orchid.api.options.Relation;
import com.eden.orchid.api.options.annotations.RelationConfig;
import com.eden.orchid.utilities.OrchidUtils;
import com.google.inject.Provider;
import org.json.JSONObject;

import javax.inject.Inject;
import java.lang.reflect.Field;

/**
 * @since v1.0.0
 * @orchidApi optionTypes
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
        Relation relation = null;
        JSONObject relationConfig = new JSONObject();

        RelationConfig configAnnotation = field.getAnnotation(RelationConfig.class);
        if(configAnnotation != null) {
            if(!EdenUtils.isEmpty(configAnnotation.collectionType())) {
                relationConfig.put("collectionType", configAnnotation.collectionType());
            }
            if(!EdenUtils.isEmpty(configAnnotation.collectionId())) {
                relationConfig.put("collectionId", configAnnotation.collectionId());
            }
            if(!EdenUtils.isEmpty(configAnnotation.itemId())) {
                relationConfig.put("itemId", configAnnotation.itemId());
            }
        }

        if(sourceObject instanceof String) {
            relation = (Relation) contextProvider.get().getInjector().getInstance(field.getType());
            relationConfig = OrchidUtils.merge(relationConfig, relation.parseStringRef((String) sourceObject));
        }
        else if(sourceObject instanceof JSONObject) {
            relation = (Relation) contextProvider.get().getInjector().getInstance(field.getType());
            relationConfig = OrchidUtils.merge(relationConfig, (JSONObject) sourceObject);
        }

        if(relation == null) {
            relation = getDefaultValue(field);
        }

        relation.setRef(relationConfig);

        return relation;
    }

    @Override
    public Relation getDefaultValue(Field field) {
        return (Relation) contextProvider.get().getInjector().getInstance(field.getType());
    }

}
