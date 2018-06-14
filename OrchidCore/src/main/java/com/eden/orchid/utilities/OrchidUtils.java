package com.eden.orchid.utilities;

import com.eden.common.json.JSONElement;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.theme.assets.AssetHolder;
import com.eden.orchid.api.theme.assets.AssetPage;
import com.eden.orchid.api.theme.components.ComponentHolder;
import com.eden.orchid.api.theme.components.OrchidComponent;
import com.eden.orchid.api.theme.pages.OrchidPage;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Formatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.eden.orchid.utilities.OrchidExtensionsKt.from;
import static com.eden.orchid.utilities.OrchidExtensionsKt.to;

public final class OrchidUtils {

// Tested and documented methods
//----------------------------------------------------------------------------------------------------------------------

    static boolean isWindows = File.separator.equals("\\");

    public static String applyBaseUrl(OrchidContext context, String url) {
        return context.getBaseUrl() + "/" + OrchidUtils.normalizePath(url);
    }

    /**
     * Removes the base directory from a file path. Leading slashes are also removed from the resulting file path.
     *
     * @param sourcePath The original file path
     * @param baseDir the base directory that should be removed from the original file path
     * @return the file path relative to the base directory
     */
    public static String getRelativeFilename(String sourcePath, String baseDir) {
        if (sourcePath.contains(baseDir)) {
            int indexOf = sourcePath.indexOf(baseDir);

            if (indexOf + baseDir.length() < sourcePath.length()) {
                String relative = sourcePath.substring((indexOf + baseDir.length()));

                if (relative.startsWith("/")) {
                    relative = relative.substring(1);
                }
                else if (relative.startsWith("\\")) {
                    relative = relative.substring(1);
                }

                return relative;
            }
        }

        return sourcePath;
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
            if (isWindows) {
                normalizedPath = normalizedPath.replaceAll("\\\\", "/");
            }

            normalizedPath = StringUtils.strip(normalizedPath.trim(), "/");
        }

        return normalizedPath;
    }

    /**
     * Parse input in the forms of an array of key-arg mappings, such as `['-key1 val1', '-key2 val1 val2 val3']`, and
     * converts it to its mapped form, like {'-key1': ['-key1', 'val1'], '-key2': ['-key2', 'val1', 'val2', 'val3']}.
     * The keys are retained as the first item in the value arrays, and the key retains the dash at its start.
     *
     * @param args
     * @return parsed args
     */
    public static Map<String, String[]> parseCommandLineArgs(String[] args) {
        return Arrays
                .stream(args)
                .filter(s -> s.startsWith("-"))
                .map(s -> s.split("\\s+"))
                .collect(Collectors.toMap(s -> s[0], s -> s, (key1, key2) -> key1));
    }

    /**
     * Parse input as entered on the command palette or in the Orchid interactive shell. The command input consists of
     * two parts: the first is an ordered input, and the keys for the ordered values are given by `paramKeys`. The
     * second consists of named key-arg mappings in the same format as used for the command-line flags. The resulting
     * values for the named args do not retain the key in the value array, and the dash is stripped from the key. In
     * addition, single values are lifted out an the array into single values. The two sections are separated by `--`.
     * As as example: `val1 -- -key2 val1 val2 val3`, when parsed with paramKeys as `['key1']`, results in a mapped form
     * like `{'key1': 'val1', 'key2': ['val1', 'val2', 'val3']}`.
     *
     * @param args the raw input text
     * @param paramKeys the key names used for the ordered parameters
     * @return a mapping of values extracted from the input text
     */
    public static JSONObject parseCommandArgs(String args, String[] paramKeys) {
        JSONObject paramMap = new JSONObject();

        String[] argsPieces = args.split("\\s*--\\s*");
        String orderedInput = argsPieces[0].trim();
        String namedInput = (argsPieces.length > 1) ? argsPieces[1].trim() : "";

        if(!EdenUtils.isEmpty(orderedInput)) {
            String[] orderedInputPieces = orderedInput.split("\\s+");
            int i = 0;
            while (i < paramKeys.length && i < orderedInputPieces.length) {
                paramMap.put(paramKeys[i], orderedInputPieces[i]);
                i++;
            }
        }

        if(!EdenUtils.isEmpty(namedInput)) {
            String[] namedInputPieces = namedInput.split("\\s*-\\s*");

            for (int i = 0; i < namedInputPieces.length; i++) {
                namedInputPieces[i] = "-" + namedInputPieces[i];
            }

            parseCommandLineArgs(namedInputPieces).forEach((key, vals) -> {
                String keyName = StringUtils.stripStart(key, "-");
                if(!EdenUtils.isEmpty(keyName)) {
                    if(vals.length == 2) {
                        paramMap.put(StringUtils.stripStart(key, "-"), vals[1]);
                    }
                    else {
                        paramMap.put(StringUtils.stripStart(key, "-"), Arrays.copyOfRange(vals, 1, vals.length));
                    }
                }
            });
        }

        return paramMap;
    }

    /**
     * Converts a String to a URL-safe, or 'slug' version. This converts the input to lowercase and replaces all
     * characters with dashes (-) except for alphaneumerics, dashes (-), underscores (_), and forward slashes (/).
     *
     * @param pathPiece the input to 'slugify'
     * @return the URL-safe slug
     */
    public static String toSlug(String pathPiece){
        String s = pathPiece.replaceAll("\\s+", "-").toLowerCase();
        s = s.replaceAll("[^\\w-_/]", "");
        return s;
    }

    /**
     * Returns true if the input filename represents an external resource. Currently supports http:// and https://
     *
     * @param fileName the input to check if external
     * @return whether the input represents an external resource
     */
    public static boolean isExternal(String fileName) {
        return (fileName.startsWith("http://") || fileName.startsWith("https://"));
    }

    /**
     * Returns the first item in a list if possible, returning null otherwise.
     *
     * @param items the list
     * @param <T> the type of items in the list
     * @return the first item in the list if the list is not null and not empty, null otherwise
     */
    public static <T> T first(List<T> items) {
        if(items != null && items.size() > 0) {
            return items.get(0);
        }

        return null;
    }

    public static <T> T first(Stream<T> items) {
        return items.findFirst().orElse(null);
    }

// Untested or undocumented methods
//----------------------------------------------------------------------------------------------------------------------

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

    public static void addComponentAssets(OrchidPage containingPage, ComponentHolder[] componentHolders, List<AssetPage> assets, Function<? super OrchidComponent, ? extends List<AssetPage>> getter) {
        if(!EdenUtils.isEmpty(componentHolders)) {
            for (ComponentHolder componentHolder : componentHolders) {
                try {
                    List<OrchidComponent> componentsList = componentHolder.get(containingPage);
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

    public static <T> Predicate<T> not(Predicate<T> t) {
        return t.negate();
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

    public static <T> T transform(T input, List<Function<T, T>> transformations) {
        return transformations
                .stream()
                .reduce(
                        UnaryOperator.identity(),
                        (a, b) -> ((T o) -> b.apply(a.apply(o)))
                ).apply(input);
    }

    public static String sha1(final File file) throws NoSuchAlgorithmException, IOException {
        return sha1(new BufferedInputStream(new FileInputStream(file)));
    }

    public static String sha1(final String text) throws NoSuchAlgorithmException, IOException {
        return sha1(new ByteArrayInputStream(text.getBytes(Charset.forName("UTF-8"))));
    }

    public static String sha1(final InputStream stream) throws NoSuchAlgorithmException, IOException {
        final MessageDigest messageDigest = MessageDigest.getInstance("SHA1");

        try (InputStream is = stream) {
            final byte[] buffer = new byte[1024];
            for (int read = 0; (read = is.read(buffer)) != -1;) {
                messageDigest.update(buffer, 0, read);
            }
        }

        // Convert the byte to hex format
        try (Formatter formatter = new Formatter()) {
            for (final byte b : messageDigest.digest()) {
                formatter.format("%02x", b);
            }
            return formatter.toString();
        }
    }

// Deprecated Methods
//----------------------------------------------------------------------------------------------------------------------

    /**
     * @deprecated This method has been moved to EdenUtils.
     */
    public static boolean elementIsObject(JSONElement el) {
        return EdenUtils.elementIsObject(el);
    }

    /**
     * @deprecated This method has been moved to EdenUtils.
     */
    public static boolean elementIsArray(JSONElement el) {
        return EdenUtils.elementIsArray(el);
    }

    /**
     * @deprecated This method has been moved to EdenUtils.
     */
    public static boolean elementIsString(JSONElement el) {
        return EdenUtils.elementIsString(el);
    }

    /**
     * @deprecated This method has been moved to EdenUtils.
     */
    public static JSONObject merge(JSONObject... sources) {
        return EdenUtils.merge(sources);
    }

    /**
     * @deprecated This method has been replaced by more flexible Kotlin APIs. These new APIs are available in Java
     * as static methods in OrchidExtensionsKt, although they don't look nearly as nice in Java as they do in Kotlin.
     */
    public static String camelcaseToTitleCase(String camelcase) {
        String[] from = from(camelcase, OrchidExtensionsKt::camelCase);
        return to(from, OrchidExtensionsKt::titleCase);
    }

}
