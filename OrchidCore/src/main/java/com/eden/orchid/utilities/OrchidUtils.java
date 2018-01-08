package com.eden.orchid.utilities;

import com.eden.common.json.JSONElement;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.theme.assets.AssetHolder;
import com.eden.orchid.api.theme.assets.AssetPage;
import com.eden.orchid.api.theme.components.ComponentHolder;
import com.eden.orchid.api.theme.components.OrchidComponent;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class OrchidUtils {
    public static String applyBaseUrl(OrchidContext context, String url) {
        return context.getBaseUrl() + "/" + OrchidUtils.normalizePath(url);
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

    public static JSONObject merge(JSONObject... sources) {
        JSONObject dest = new JSONObject();

        for(JSONObject tmpSource : sources) {
            JSONObject source = new JSONObject(tmpSource.toMap());

            for (String key : source.keySet()) {
                if (dest.has(key)) {
                    if (dest.get(key) instanceof JSONObject && source.get(key) instanceof JSONObject) {
                        dest.put(key, merge(dest.getJSONObject(key), source.getJSONObject(key)));
                    }
                    else if (dest.get(key) instanceof JSONArray && source.get(key) instanceof JSONArray) {
                        for (Object obj : source.getJSONArray(key)) {
                            dest.getJSONArray(key).put(obj);
                        }
                    }
                    else {
                        dest.put(key, source.get(key));
                    }
                }
                else {
                    dest.put(key, source.get(key));
                }
            }
        }

        return dest;
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
            }

            normalizedPath = StringUtils.strip(normalizedPath.trim(), "/");
        }

        return normalizedPath;
    }

    public static String camelcaseToTitleCase(String camelcase) {
        return Arrays
                .stream(StringUtils.splitByCharacterTypeCamelCase(camelcase))
                .map(StringUtils::capitalize)
                .collect(Collectors.joining(" "));
    }

    public static <T, R> R firstBy(Stream<T> stream, Function<? super T, ? extends R> mapper) {
        return stream
                .map(mapper)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    public static <T, R> R firstBy(T[] items, Function<? super T, ? extends R> mapper) {
        return firstBy(Arrays.stream(items), mapper);
    }

    public static <T, R> R firstBy(Collection<T> items, Function<? super T, ? extends R> mapper) {
        return firstBy(items.stream(), mapper);
    }

    public static <T, U> boolean inArray(T item, U[] items, BiPredicate<T, U> condition) {
        for(U test : items) {
            if(condition.test(item, test)) {
                return true;
            }
        }

        return false;
    }

    public static void addExtraAssetsTo(OrchidContext context, String[] extraCss, String[] extraJs, AssetHolder holder, Object source, String sourceKey) {
        if(!EdenUtils.isEmpty(extraCss)) {
            Arrays.stream(extraCss)
                  .map(context::getResourceEntry)
                  .filter(Objects::nonNull)
                  .map(orchidResource -> new AssetPage(source, sourceKey, orchidResource, orchidResource.getReference().getTitle()))
                  .forEach(holder::addCss);
        }
        if(!EdenUtils.isEmpty(extraJs)) {
            Arrays.stream(extraJs)
                  .map(context::getResourceEntry)
                  .filter(Objects::nonNull)
                  .map(orchidResource -> new AssetPage(source, sourceKey, orchidResource, orchidResource.getReference().getTitle()))
                  .forEach(holder::addJs);
        }
    }

    public static void addComponentAssets(ComponentHolder[] componentHolders, List<AssetPage> assets, Function<? super OrchidComponent, ? extends List<AssetPage>> getter) {
        if(!EdenUtils.isEmpty(componentHolders)) {
            for (ComponentHolder componentHolder : componentHolders) {
                try {
                    List<OrchidComponent> componentsList = componentHolder.getComponents();
                    if (!EdenUtils.isEmpty(componentsList)) {
                        componentsList
                                .stream()
                                .map(getter)
                                .forEach(assets::addAll);
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static boolean isExternal(String fileName) {
        return (fileName.startsWith("http://") || fileName.startsWith("https://"));
    }
}
