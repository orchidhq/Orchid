package com.eden.orchid.api.compilers;

import com.eden.orchid.api.registration.Prioritized;
import org.json.JSONObject;

/**
 * A generic Parser used to convert structured text input into a usable JSONObject. Commonly used for extracting data
 * from configuration files or from blocks embedded within content files.
 *
 * @since v1.0.0
 * @orchidApi extensible
 */
public abstract class OrchidParser extends Prioritized {

    /**
     * If the Parser evaluates the input to be an Array structure rather than Object structure, the resulting array data
     * should be wrapped in a JSONObject with this value as its only key, pointing to the Array contents.
     *
     * @since v1.0.0
     */
    public static final String arrayAsObjectKey = "listData";

    /**
     * Initialize the OrchidParser with a set priority. Parsers with a higher priority are chosen first to process a
     * given input content when multiple Parsers can process the same input extension.
     *
     * @param priority priority
     *
     * @since v1.0.0
     */
    public OrchidParser(int priority) {
        super(priority);
    }

    /**
     * Returns the file extensions which are able to be processed by this Parser. When content needs to be processed by
     * a parser, it looks for the highest-priority parser which accepts the given extension.
     *
     * @return the file extensions which are able to be processed by this Parser
     *
     * @since v1.0.0
     */
    public abstract String[] getSourceExtensions();

    /**
     * In some cases, the routine choosing a Parser may lookup a parser by its delimiter instead of just its extension.
     * If a returned character is not null, it will be accessible by its delimiter as well as its extensions, otherwise
     * the parser may only be found by its extensions. A Parser should decide for itself which of its extensions is
     * interpreted by the delimiting character. A delimiter is also expected to prepare itself for insertion into a
     * Regex pattern (such as by Pattern.quote()), in the case that it is a string that contains normal Regex special
     * characters.
     *
     * @return a delimiting string
     *
     * @since v1.0.0
     */
    public String getDelimiter() {
        return null;
    }

    /**
     * Return the un-quoted delimiter string so it can be displayed to the user.
     *
     * @return an un-quoted delimiting string
     *
     * @since v1.0.0
     */
    public String getDelimiterString() {
        return getDelimiter();
    }

    /**
     * Processes content according to a particular extension. The Parser expects to return a JSON object, regardless
     * of whether it parsed JSONObject- or JSONArray-type content. If the result of processing is a JSONArray, it is
     * expected that the array data will exist in a JSONObject at the OrchidParser.arrayAsObjectKey.
     *
     * @param extension the extension to parse the content against
     * @param input the content to parse
     * @return a JSONObject representing the data found by parsing the content
     *
     * @since v1.0.0
     */
    public abstract JSONObject parse(String extension, String input);

}
