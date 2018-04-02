package com.eden.orchid.api.options.converters;

import com.eden.common.util.EdenPair;
import com.eden.orchid.api.converters.TypeConverter;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlexibleIterableConverter implements TypeConverter<Iterable> {

    private final Provider<FlexibleMapConverter> mapConverter;

    @Inject
    public FlexibleIterableConverter(Provider<FlexibleMapConverter> mapConverter) {
        this.mapConverter = mapConverter;
    }

    public Class<Iterable> resultClass() {
        return Iterable.class;
    }

    @Override
    public EdenPair<Boolean, Iterable> convert(Object object) {
        return convert(object, null);
    }

    public EdenPair<Boolean, Iterable> convert(Object object, String keyName) {
        if(object != null) {
            if (object instanceof Iterable) {
                return new EdenPair<>(true, (Iterable) object);
            }
            else if (object.getClass().isArray()) {
                List<Object> list = new ArrayList<>();
                Collections.addAll(list, (Object[]) object);
                return new EdenPair<>(true, list);
            }
            else {
                EdenPair<Boolean, Map> potentialMap = mapConverter.get().convert(object);
                if(potentialMap.first) {
                    Map<String, Object> actualMap = (Map<String, Object>) potentialMap.second;
                    List<Object> list = mapToList(actualMap, keyName);
                    return new EdenPair<>(false, list);
                }
                else {
                    return new EdenPair<>(false, Collections.singletonList(object));
                }
            }
        }

        return new EdenPair<>(false, new ArrayList());
    }

    private List<Object> mapToList(Map<String, Object> map, String keyName) {
        List<Object> list = new ArrayList<>();

        for(String key : map.keySet()) {
            Object item = map.get(key);

            EdenPair<Boolean, Map> potentialMapItem = mapConverter.get().convert(item);
            if(potentialMapItem.first) {
                Map<String, Object> mapItem = new HashMap<>((Map<String, Object>) potentialMapItem.second);
                mapItem.put(keyName, key);
                list.add(mapItem);
            }
            else {
                list.add(item);
            }
        }

        return list;
    }

}
