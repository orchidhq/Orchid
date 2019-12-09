package com.eden.orchid.api.options;

public final class OptionsDescription {
    private final String key;
    private final Class optionType;
    private final Class[] optionTypeParameters;
    private final String description;
    private final String defaultValue;

    public OptionsDescription(final String key, final Class optionType, final Class[] optionTypeParameters, final String description, final String defaultValue) {
        this.key = key;
        this.optionType = optionType;
        this.optionTypeParameters = optionTypeParameters;
        this.description = description;
        this.defaultValue = defaultValue;
    }

    public String getKey() {
        return this.key;
    }

    public Class getOptionType() {
        return this.optionType;
    }

    public Class[] getOptionTypeParameters() {
        return this.optionTypeParameters;
    }

    public String getDescription() {
        return this.description;
    }

    public String getDefaultValue() {
        return this.defaultValue;
    }

    @Override
    public boolean equals(final java.lang.Object o) {
        if (o == this) return true;
        if (!(o instanceof OptionsDescription)) return false;
        final OptionsDescription other = (OptionsDescription) o;
        final java.lang.Object this$key = this.getKey();
        final java.lang.Object other$key = other.getKey();
        if (this$key == null ? other$key != null : !this$key.equals(other$key)) return false;
        final java.lang.Object this$optionType = this.getOptionType();
        final java.lang.Object other$optionType = other.getOptionType();
        if (this$optionType == null ? other$optionType != null : !this$optionType.equals(other$optionType)) return false;
        if (!java.util.Arrays.deepEquals(this.getOptionTypeParameters(), other.getOptionTypeParameters())) return false;
        final java.lang.Object this$description = this.getDescription();
        final java.lang.Object other$description = other.getDescription();
        if (this$description == null ? other$description != null : !this$description.equals(other$description)) return false;
        final java.lang.Object this$defaultValue = this.getDefaultValue();
        final java.lang.Object other$defaultValue = other.getDefaultValue();
        if (this$defaultValue == null ? other$defaultValue != null : !this$defaultValue.equals(other$defaultValue)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final java.lang.Object $key = this.getKey();
        result = result * PRIME + ($key == null ? 43 : $key.hashCode());
        final java.lang.Object $optionType = this.getOptionType();
        result = result * PRIME + ($optionType == null ? 43 : $optionType.hashCode());
        result = result * PRIME + java.util.Arrays.deepHashCode(this.getOptionTypeParameters());
        final java.lang.Object $description = this.getDescription();
        result = result * PRIME + ($description == null ? 43 : $description.hashCode());
        final java.lang.Object $defaultValue = this.getDefaultValue();
        result = result * PRIME + ($defaultValue == null ? 43 : $defaultValue.hashCode());
        return result;
    }

    @Override
    public java.lang.String toString() {
        return "OptionsDescription(key=" + this.getKey() + ", optionType=" + this.getOptionType() + ", optionTypeParameters=" + java.util.Arrays.deepToString(this.getOptionTypeParameters()) + ", description=" + this.getDescription() + ", defaultValue=" + this.getDefaultValue() + ")";
    }
}
