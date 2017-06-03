package com.eden.orchid.utilities;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.json.JSONElement;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.impl.compilers.jtwig.WalkMapFilter;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.google.inject.util.Types;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class OrchidUtils {

    public static String applyBaseUrl(OrchidContext context, String url) {
        String baseUrl = "";

        if (context.query("options.baseUrl") != null) {
            baseUrl = context.query("options.baseUrl").toString();
        }

        return baseUrl + "/" + url;
    }

    public static String linkTo(OrchidContext context, String linkName) {
        return linkTo(context, linkName, linkName);
    }

    public static String linkTo(OrchidContext context, String linkName, String displayName) {
        Set<OrchidGenerator> generators = new ObservableTreeSet<>(OrchidUtils.resolveSet(context, OrchidGenerator.class));

        for (OrchidGenerator generator : generators) {
            String linkText = linkToIndex(context, linkName, generator.getName(), displayName);

            if (!linkText.equals(linkName)) {
                return linkText;
            }
        }

        return linkName;
    }

    public static String linkToIndex(OrchidContext context, String linkName, String indexKey) {
        return linkToIndex(context, linkName, indexKey, linkName);
    }

    public static String linkToIndex(OrchidContext context, String linkName, String indexKey, String displayName) {

        JSONArray index = queryIndex(context, indexKey);

        if (index != null) {
            JSONObject indexObject = new JSONObject();
            indexObject.put("index", index);

            String s = findInMap(linkName, indexObject, displayName);
            if (!EdenUtils.isEmpty(s)) {
                return s;
            }
        }

        return linkName;
    }

    private static String findInMap(String link, JSONObject mapObject, String displayName) {
        List urls = WalkMapFilter.walkObject(mapObject, "url");
        String template = "<a href=\"#{$1}\">#{$2}</a>";

        for (Object object : urls) {
            if (object instanceof Map) {
                Map map = (Map) object;

                if (map.containsKey("url")) {
                    JSONElement element = new JSONElement(new JSONObject(map));
                    if (OrchidUtils.elementIsString(element.query("data.info.qualifiedName")) && element.query("data.info.qualifiedName").toString().equals(link)) {
                        if (!EdenUtils.isEmpty(displayName)) {
                            return Clog.format(template, map.get("url"), displayName);
                        }
                        else {
                            return Clog.format(template, map.get("url"), map.get("name"));
                        }
                    }
                    else if (map.containsKey("name") && map.get("name").toString().equals(link)) {
                        if (!EdenUtils.isEmpty(displayName)) {
                            return Clog.format(template, map.get("url"), displayName);
                        }
                        else {
                            return Clog.format(template, map.get("url"), map.get("name"));
                        }
                    }
                }
            }
        }

        return null;
    }

    public static List<String> wrapString(String content, int width) {
        List<String> matchList = new ArrayList<>();

        if (!EdenUtils.isEmpty(content)) {
            Pattern regex = Pattern.compile("(.{1," + width + "}(?:\\s|$))|(.{0," + width + "})", Pattern.DOTALL);
            Matcher regexMatcher = regex.matcher(content);
            while (regexMatcher.find()) {
                String line = regexMatcher.group().trim();
                if (!EdenUtils.isEmpty(line)) {
                    matchList.add(line);
                }
            }
        }

        return matchList;
    }

    public static class DefaultComparator<T> implements Comparator<T> {
        @Override
        public int compare(T o1, T o2) {
            return o1.getClass().getName().compareTo(o2.getClass().getName());
        }
    }

    public static <T> Set<T> resolveSet(OrchidContext context, Class<T> clazz) {
        return resolveSet(context.getInjector(), clazz);
    }

    public static <T> Set<T> resolveSet(Injector injector, Class<T> clazz) {
        try {
            TypeLiteral<Set<T>> lit = (TypeLiteral<Set<T>>) TypeLiteral.get(Types.setOf(clazz));
            Key<Set<T>> key = Key.get(lit);
            Set<T> bindings = injector.getInstance(key);

            if (bindings != null) {
                return bindings;
            }
        }
        catch (Exception e) {

        }

        return new TreeSet<>();
    }

    public static String getRelativeFilename(String sourcePath, String baseDir) {
        if (sourcePath.contains(baseDir)) {
            int indexOf = sourcePath.indexOf(baseDir);

            if (indexOf + baseDir.length() < sourcePath.length()) {
                String relative = sourcePath.substring((indexOf + baseDir.length()));

                if (relative.startsWith("/")) {
                    relative = relative.substring(1);
                }

                return relative;
            }
        }

        return sourcePath;
    }

    public static <T> Predicate<T> not(Predicate<T> t) {
        return t.negate();
    }

    public static boolean elementIsObject(JSONElement el) {
        return (el != null) && (el.getElement() != null) && (el.getElement() instanceof JSONObject);
    }

    public static boolean elementIsArray(JSONElement el) {
        return (el != null) && (el.getElement() != null) && (el.getElement() instanceof JSONArray);
    }

    public static boolean elementIsString(JSONElement el) {
        return (el != null) && (el.getElement() != null) && (el.getElement() instanceof String);
    }

    public static List walkObject(JSONObject object, String stop) {
        return WalkMapFilter.walkObject(object, stop);
    }

    public static JSONArray queryIndex(OrchidContext context, String indexName) {
        JSONArray array = new JSONArray();

        JSONElement internalEl = context.query("index." + indexName);
        if (OrchidUtils.elementIsObject(internalEl)) {
            List items = OrchidUtils.walkObject((JSONObject) internalEl.getElement(), "url");

            for (Object item : items) {
                JSONObject object = null;
                if (item instanceof Map) {
                    object = new JSONObject((Map) item);
                }
                else if (item instanceof JSONObject) {
                    object = (JSONObject) item;
                }

                if (object != null) {
                    array.put(object);
                }
            }
        }

        JSONElement externalEl = context.query("options.externalIndex.keyedIndex." + indexName);
        if (OrchidUtils.elementIsArray(externalEl)) {
            JSONArray externalIndexArray = (JSONArray) externalEl.getElement();

            for (int i = 0; i < externalIndexArray.length(); i++) {
                JSONObject object = null;
                if (externalIndexArray.get(i) instanceof Map) {
                    object = new JSONObject((Map) externalIndexArray.get(i));
                }
                else if (externalIndexArray.get(i) instanceof JSONObject) {
                    object = (JSONObject) externalIndexArray.get(i);
                }

                if (object != null) {
                    array.put(object);
                }
            }
        }

        return array;
    }

    /**
     * Replaces a string's OS-dependant file path-separator characters (File.separator) with '/', and also strips
     * any slashes from the beginning and end of the string. This allows us to do path operations using the standard
     * forward slash, bypassing any potential regex-related issues, and also makes it easy to split a path into its
     * exact parts.
     *
     * @param path The path to normalize
     * @return the normalized path
     */
    public static String normalizePath(String path) {
        String normalizedPath = path;
        if (normalizedPath != null) {
            if (File.separator.equals("\\")) {
                normalizedPath = normalizedPath.replaceAll("\\\\", "/");
                normalizedPath = StringUtils.strip(normalizedPath.trim(), "\\\\");
            }
            else {
                normalizedPath = StringUtils.strip(normalizedPath.trim(), "/");
            }
        }

        return normalizedPath;
    }
}
