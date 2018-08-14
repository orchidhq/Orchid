package com.eden.orchid.impl.relations;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.Relation;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.utilities.OrchidUtils;
import lombok.Getter;
import lombok.Setter;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

@Getter @Setter
public class AssetRelation extends Relation<String> {

    @Option
    @Description("The filename and path of an asset to look up.")
    private String itemId;

    @Option
    @Description("The asset title.")
    private String title;

    @Option
    @Description("The asset alt text.")
    private String alt;

    @Inject
    public AssetRelation(OrchidContext context) {
        super(context);
    }

    @Override
    public String load() {
        String fieldValue = itemId;

        boolean shouldApplybaseUrl = true;

        if(!EdenUtils.isEmpty(fieldValue)) {
            if(OrchidUtils.isExternal(fieldValue)) {
                shouldApplybaseUrl = false;
            }
        }

        if(shouldApplybaseUrl) {
            fieldValue = OrchidUtils.applyBaseUrl(context, fieldValue);
        }

        return fieldValue;
    }

    @Override
    public String toString() {
        return get();
    }

    public String getLink() {
        return get();
    }

    public Map<String, Object> parseStringRef(String ref) {
        Map<String, Object> objectRef = new HashMap<>();

        objectRef.put("itemId", ref);

        return objectRef;
    }
}
