package com.eden.orchid.utilities;

import com.caseyjbrooks.clog.Clog;
import com.copperleaf.krow.TableFormatter;
import com.copperleaf.krow.formatters.ascii.AsciiTableFormatter;
import com.copperleaf.krow.formatters.ascii.CrossingBorder;
import com.copperleaf.krow.formatters.ascii.NoBorder;
import com.copperleaf.krow.formatters.ascii.SingleBorder;
import com.eden.common.json.JSONElement;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.OrchidFlags;
import com.eden.orchid.api.theme.assets.AssetHolder;
import com.eden.orchid.api.theme.assets.CssPage;
import com.eden.orchid.api.theme.assets.JsPage;
import com.eden.orchid.api.theme.components.ComponentHolder;
import com.eden.orchid.api.theme.components.OrchidComponent;
import com.eden.orchid.api.theme.pages.OrchidPage;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static com.eden.orchid.utilities.OrchidExtensionsKt.from;
import static com.eden.orchid.utilities.OrchidExtensionsKt.to;

public final class OrchidUtils {
    
// constants
//----------------------------------------------------------------------------------------------------------------------

    public static boolean isWindows = File.separator.equals("\\");

    public static final TableFormatter<String> defaultTableFormatter = (OrchidUtils.isWindows)
            ? new AsciiTableFormatter(new CrossingBorder())
            : new AsciiTableFormatter(new SingleBorder());

    public static final TableFormatter<String> compactTableFormatter = new AsciiTableFormatter(new NoBorder());

// Tested and documented methods
//----------------------------------------------------------------------------------------------------------------------

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

    public static Map<String, Object> parseCommandLineArgs(String[] args) {
        Map<String, String> flagNames = OrchidFlags.getInstance().getFlagNames();
        Map<String, String> flagAliases = OrchidFlags.getInstance().getFlagAliases();
        List<String> positionalFlags = OrchidFlags.getInstance().getPositionalFlags();

        return parseArgsArray(args, flagNames, flagAliases, positionalFlags);
    }

    static Map<String, Object> parseArgsArray(String[] args, Map<String, String> validNames, Map<String, String> validAliases, List<String> positionalNames) {
        List<String> positionalArgs = new ArrayList<>();
        Map<String, Object> namedArgs = new HashMap<>();

        // loop over flags, adding them to positional args until we get an arg starting with '--' or '-', at which point
        // we start adding to that key's values
        String currentFlag = null;
        int valuesParsed = 0;
        for (int i = 0; i < args.length; i++) {
            String arg = (args[i] != null) ? args[i] : "";

            if(arg.startsWith("--")) {
                // full flag name
                String flag = arg.replace("--", "");
                if(validNames == null || validNames.containsKey(flag)) {
                    if(valuesParsed == 0 && currentFlag != null) {
                        // the previous flag had no values, mark it as true
                        addArgValue(namedArgs, currentFlag, "true");
                    }
                    currentFlag = flag;
                    valuesParsed = 0;

                }
                else {
                    throw new IllegalArgumentException(Clog.format("Unrecognized flag: --{}", flag));
                }
            }
            else if(arg.startsWith("-")) {
                // aliased flag name
                String flag = arg.replace("-", "");
                if(validNames != null && validAliases != null && validAliases.containsKey(flag)) {
                    if(valuesParsed == 0 && currentFlag != null) {
                        // the previous flag had no values, mark it as true
                        addArgValue(namedArgs, currentFlag, "true");
                    }
                    currentFlag = validAliases.get(flag);
                    valuesParsed = 0;
                }
                else {
                    throw new IllegalArgumentException(Clog.format("Unrecognized flag: -{}", flag));
                }
            }
            else {
                if(currentFlag != null) {
                    // named flag values
                    addArgValue(namedArgs, currentFlag, arg);
                    valuesParsed++;
                }
                else {
                    // positional flag values
                    positionalArgs.add(arg);
                }
            }
        }

        if(valuesParsed == 0 && currentFlag != null) {
            // the final flag had no values, mark it as true
            addArgValue(namedArgs, currentFlag, "true");
        }

        // add positional args to named args map. Positional args override named args
        if(positionalArgs.size() > 0 && positionalNames.size() >= positionalArgs.size()) {
            for (int i = 0; i < positionalArgs.size(); i++) {
                addArgValue(namedArgs, positionalNames.get(i), positionalArgs.get(i));
            }
        }

        Iterator<String> it = namedArgs.keySet().iterator();

        while(it.hasNext()) {
            String key = it.next();
            Object value = namedArgs.get(key);

            boolean removeObject = false;
            if(value == null) {
                removeObject = true;
            }
            else if(value instanceof String && EdenUtils.isEmpty((String)value)) {
                removeObject = true;
            }
            else if(value instanceof Collection && EdenUtils.isEmpty((Collection)value)) {
                removeObject = true;
            }

            if(removeObject) {
                it.remove();
            }
        }

        return namedArgs;
    }

    private static void addArgValue(Map<String, Object> namedArgs, String key, String value) {
        if(!namedArgs.containsKey(key)) {
            namedArgs.put(key, value);
        }
        else {
            if(!(namedArgs.get(key) instanceof List)) {
                List<Object> listValues = new ArrayList<>();
                listValues.add(namedArgs.get(key));
                namedArgs.put(key, listValues);
            }

            ((List<Object>) namedArgs.get(key)).add(value);
        }
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
     * @param argString the raw input text
     * @param paramKeys the key names used for the ordered parameters
     * @return a mapping of values extracted from the input text
     */
    public static Map<String, Object> parseCommandArgs(String argString, String[] paramKeys) {
        return parseArgsArray(argString.split("\\s+"), null, null, Arrays.asList(paramKeys));
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
                    .map(orchidResource -> new CssPage(
                            source,
                            sourceKey,
                            orchidResource,
                            orchidResource.getReference().getTitle(),
                            orchidResource.getReference().getTitle()
                    ))
                    .forEach(holder::addCss);
        }
        if(!EdenUtils.isEmpty(extraJs)) {
            Arrays.stream(extraJs)
                    .map(context::getResourceEntry)
                    .filter(Objects::nonNull)
                    .map(orchidResource -> new JsPage(
                            source,
                            sourceKey,
                            orchidResource,
                            orchidResource.getReference().getTitle(),
                            orchidResource.getReference().getTitle()
                    ))
                    .forEach(holder::addJs);
        }
    }

    public static <T extends AssetHolder> void addComponentAssets(OrchidPage containingPage, ComponentHolder[] componentHolders, List<T> assets, Function<? super OrchidComponent, ? extends List<T>> getter) {
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

    public static Stream<String> expandTemplateList(OrchidContext context, final List<String> templates, final String templateBase) {
        String themePreferredExtension = context.getTheme().getPreferredTemplateExtension();
        String defaultExtension = context.getDefaultTemplateExtension();

        return templates
                .stream()
                .filter(OrchidUtils.not(EdenUtils::isEmpty))
                .distinct()
                .flatMap(template -> Stream.of(
                        "templates/" + template,
                        "templates/" + template + "." + themePreferredExtension,
                        "templates/" + template + "." + defaultExtension,
                        "templates/" + templateBase + "/" + template,
                        "templates/" + templateBase + "/" + template + "." + themePreferredExtension,
                        "templates/" + templateBase + "/" + template + "." + defaultExtension
                        ).distinct()
                );
    }

// Temporary Directories, which get deleted after Orchid finishes
//----------------------------------------------------------------------------------------------------------------------

    public static Path getTempDir(String dirName) throws IOException {
        return getTempDir(OrchidFlags.getInstance().getFlagValue("dest"), dirName);
    }

    public static Path getTempDir(String baseDir, String dirName) throws IOException {
        return getTempDir(baseDir, dirName, false);
    }

    public static Path getTempDir(String dirName, boolean asSiblingToBase) throws IOException {
        return getTempDir(OrchidFlags.getInstance().getFlagValue("dest"), dirName, asSiblingToBase);
    }

    public static Path getTempDir(String baseDir, String dirName, boolean asSiblingToBase) throws IOException {
        Path sourceDir = Paths.get(baseDir);

        if(asSiblingToBase) {
            sourceDir = sourceDir.getParent();
        }

        Files.createDirectories(sourceDir);

        Path targetDir = Files.createTempDirectory(sourceDir, dirName);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                FileUtils.deleteDirectory(targetDir.toFile());
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }));

        return targetDir;
    }

// Cache directories, which stay on the user's system
//----------------------------------------------------------------------------------------------------------------------

    public static Path getCacheDir(String dirName) throws IOException {
        Path sourceDir = Paths.get(System.getProperty("user.home") + "/.orchid/" + dirName);

        return Files.createDirectories(sourceDir);
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
