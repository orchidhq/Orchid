package com.eden.orchid.impl.compilers.pebble;

import com.eden.common.util.EdenPair;
import com.eden.orchid.Orchid;
import com.eden.orchid.api.events.On;
import com.eden.orchid.api.events.OrchidEventListener;
import com.mitchellbosecke.pebble.attributes.AttributeResolver;
import com.mitchellbosecke.pebble.attributes.ResolvedAttribute;
import com.mitchellbosecke.pebble.node.ArgumentsNode;
import com.mitchellbosecke.pebble.template.EvaluationContextImpl;
import lombok.Getter;

import javax.inject.Inject;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public final class GetMethodAttributeResolver implements AttributeResolver, OrchidEventListener {

    private final ConcurrentHashMap<Class<?>, ConcurrentHashMap<String, EdenPair<Boolean, Method>>> cache;

    @Inject
    public GetMethodAttributeResolver() {
        cache = new ConcurrentHashMap<>();
    }

    @Override
    public ResolvedAttribute resolve(Object instance, Object attributeNameValue, Object[] argumentValues, ArgumentsNode args, EvaluationContextImpl context, String filename, int lineNumber) {
        if(instance == null) return null;

        try {
            String attributeName = String.valueOf(attributeNameValue);
            EdenPair<Boolean, Method> resolved = cacheOrShouldInvoke(instance, attributeName);
            if(resolved.first) {
                Object result = resolved.second.invoke(instance, attributeName);
                return new ResolvedAttribute(result);
            }
        }
        catch (Exception e) { }

        return null;
    }

    private EdenPair<Boolean, Method> cacheOrShouldInvoke(Object instance, String attributeName) {
        // check for the item in cache and return it if found
        ConcurrentHashMap<String, EdenPair<Boolean, Method>> classCache = cache.get(instance.getClass());
        if(classCache != null) {
            EdenPair<Boolean, Method> cachedValue = classCache.get(attributeName);
            if(cachedValue != null) {
                return cachedValue;
            }
        }

        // method wasn't found, lets figure it out for ourselves...
        EdenPair<Boolean, Method> unCachedValue = shouldInvoke(instance, attributeName);

        // ...then cache what we found...
        if(classCache == null) {
            classCache = new ConcurrentHashMap<>();
            cache.put(instance.getClass(), classCache);
        }
        classCache.put(attributeName, unCachedValue);

        // ...and return the value
        return unCachedValue;
    }

    private EdenPair<Boolean, Method> shouldInvoke(Object instance, String attributeName) {
        try {
            Method dynamicGetMethod = instance.getClass().getMethod("get", String.class);
            return new EdenPair<>(true, dynamicGetMethod);
        }
        catch (Exception e) { }

        return new EdenPair<>(false, null);
    }

    @On(Orchid.Lifecycle.ClearCache.class)
    public void onClearCache(Orchid.Lifecycle.ClearCache event) {
        cache.clear();
    }
}
