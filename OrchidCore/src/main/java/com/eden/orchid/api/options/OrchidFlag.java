package com.eden.orchid.api.options;

import com.eden.orchid.api.options.annotations.Description;

/**
 * Denotes a Javadoc-style command-line argument. It is important to note that Options are found by scanning the
 * classpath and are **not** created by Dependency Injection, and so are not able to have dependencies injected into
 * themselves.
 *
 * @since v1.0.0
 * @orchidApi extensible
 */
public abstract class OrchidFlag {
    enum FlagType {
        STRING,
        STRING_ARRAY,
        INTEGER,
        DOUBLE,
        BOOLEAN,
    }

    private final String flag;

    private final boolean isRequired;

    private final Object defaultValue;

    /**
     * Initialize this Flag with its flag key, whether it is required, and if it is required its optional default value.
     *
     * @see OrchidFlag#getFlag()
     * @see OrchidFlag#isRequired()
     * @see OrchidFlag#getDefaultValue()
     *
     * @param flag
     * @param isRequired
     * @param defaultValue
     */
    public OrchidFlag(String flag, boolean isRequired, Object defaultValue) {
        this.flag = flag;
        this.isRequired = isRequired;
        this.defaultValue = defaultValue;
    }

    /**
     * The name of the flag used on the command-line. This value should _not_ start with a dash, but options specified
     * on the command-line _must_ begin with a dash. The value returned by this flag can be injected with an @Named-
     * annotated parameter.
     *
     * @return the flag, without any leading dash
     */
    public final String getFlag() {
        return this.flag;
    }

    /**
     * Return true if this OrchidOption must be set before continuing with the Orchid build. If the flag is required and
     * is not set, you may pass a default value to be used. If the default value is null, the Orchid build will fail.
     *
     * @return true if this option is required, false otherwise.
     */
    public final boolean isRequired() {
        return this.isRequired;
    }

    /**
     * If the user has not provided a value for this flag, and this flag is required, this value is used instead to get
     * a value for this option.
     *
     * @return the default value
     */
    public final Object getDefaultValue() {
        return this.defaultValue;
    }

    /**
     * Return a description of this OrchidOption, which is displayed when listing available Options.
     *
     * @return this option's description
     */
    public String getDescription() {
        if(getClass().isAnnotationPresent(Description.class)) {
            return getClass().getAnnotation(Description.class).value();
        }

        return "";
    }

    /**
     * Return the number of arguments this OrchidOption is expecting. This number is strictly enforced, and the option
     * will only be allowed to parse the command-line option if the number of arguments it finds exactly matches this
     * value.
     *
     * For each custom option that you want to recognize, optionLength must return the number of separate pieces or
     * tokens in the option. For example, we want to be able to use the custom option of the form -tag mytag. This
     * option has two pieces, the -tag option itself and its value, so the optionLength method in our doclet must return
     * 2 for the -tag option.
     *
     * An option can return 0 if it isn't expecting any actual command-line option, but does want to add data to the
     * 'option' object.
     *
     * @return the length of this option, including the flag itself
     */
    public int optionLength() { return 2; }

    /**
     * A callback for when an option on the command-line matches the optionLength or when optionLength is 0. Whatever
     * JSONElement is returned will be available in the root JSONObject at `options.{flag}`.
     *
     * @param options the raw values found on the command line. Its length will always be exactly equal to optionLength
     * @return the data parsed from the command-line option
     */
    public Object parseFlag(String[] options) { return options[1]; }


    /**
     * Flags are simple values that can only be of type String, Integer, Float, or Boolean, or String[]. Return the
     * type that this flag represents.
     *
     * @return the type of this flag.
     */
    public FlagType getFlagType() { return FlagType.STRING; }

}
