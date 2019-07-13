package com.eden.orchid.api.compilers;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.OptionsHolder;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.server.annotations.Extensible;
import com.eden.orchid.api.theme.pages.OrchidPage;

/**
 * Template Functions add methods that can be called by themselves or as a "filter" in an expression within the primary
 * template language.
 *
 * @since v1.0.0
 * @orchidApi extensible
 */
@Extensible
@Description(value = "A function that can be called from your templates.", name = "Template Functions")
public abstract class TemplateFunction implements OptionsHolder {

    protected final String name;

    protected final boolean isSafeString;

    /**
     * Initialize the Function with the name which it should be called with in the template., and whether Whether this
     * Function returns "safe" HTML that should not be escaped by default. For filters that return anything other than a
     * String, `isSafe` should always be false. The actual implementation of a Function should have a single constructor
     * annotated with {@link javax.inject.Inject }.
     *
     * @param name the name which to call this Function
     */
    public TemplateFunction(String name, boolean isSafeString) {
        this.name = name;
        this.isSafeString = isSafeString;
    }

    /**
     * The sequential parameters of this function
     *
     * @return the sequential parameters
     */
    public abstract String[] parameters();

    /**
     * Apply the filter or function here, returning the created or modified data.
     *
     * @return the result
     * @param context
     */
    public abstract Object apply(OrchidContext context, OrchidPage page);

    public String getName() {
        return this.name;
    }

    public boolean isSafeString() {
        return this.isSafeString;
    }

}
