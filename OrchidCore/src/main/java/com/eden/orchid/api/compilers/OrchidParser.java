package com.eden.orchid.api.compilers;

import com.eden.orchid.api.registration.Prioritized;
import org.json.JSONObject;

public abstract class OrchidParser extends Prioritized {

    public static final String arrayAsObjectKey = "listData";

    public OrchidParser(int priority) {
        super(priority);
    }

    /**
     * Returns the file extensions which are able to be processed by this Parser. When content needs to be processed by
     * a parser, it looks for the highest-priority parser which accepts the given extension.
     *
     * @return the file extensions which are able to be processed by this Parser
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
     */
    public String getDelimiter() {
        return null;
    }

    /**
     * Return the un-quoted delimiter string so it can be displayed to the user.
     *
     * @return an un-quoted delimiting string
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
     */
    public abstract JSONObject parse(String extension, String input);

}
