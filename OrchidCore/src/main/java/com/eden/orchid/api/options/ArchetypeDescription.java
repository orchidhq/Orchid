package com.eden.orchid.api.options;

public final class ArchetypeDescription {
    private final String key;
    private final Class<? extends OptionArchetype> archetypeType;
    private final String displayName;
    private final String description;

    public ArchetypeDescription(final String key, final Class<? extends OptionArchetype> archetypeType, final String displayName, final String description) {
        this.key = key;
        this.archetypeType = archetypeType;
        this.displayName = displayName;
        this.description = description;
    }

    public String getKey() {
        return this.key;
    }

    public Class<? extends OptionArchetype> getArchetypeType() {
        return this.archetypeType;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public String getDescription() {
        return this.description;
    }

    @Override
    public boolean equals(final java.lang.Object o) {
        if (o == this) return true;
        if (!(o instanceof ArchetypeDescription)) return false;
        final ArchetypeDescription other = (ArchetypeDescription) o;
        final java.lang.Object this$key = this.getKey();
        final java.lang.Object other$key = other.getKey();
        if (this$key == null ? other$key != null : !this$key.equals(other$key)) return false;
        final java.lang.Object this$archetypeType = this.getArchetypeType();
        final java.lang.Object other$archetypeType = other.getArchetypeType();
        if (this$archetypeType == null ? other$archetypeType != null : !this$archetypeType.equals(other$archetypeType)) return false;
        final java.lang.Object this$displayName = this.getDisplayName();
        final java.lang.Object other$displayName = other.getDisplayName();
        if (this$displayName == null ? other$displayName != null : !this$displayName.equals(other$displayName)) return false;
        final java.lang.Object this$description = this.getDescription();
        final java.lang.Object other$description = other.getDescription();
        if (this$description == null ? other$description != null : !this$description.equals(other$description)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final java.lang.Object $key = this.getKey();
        result = result * PRIME + ($key == null ? 43 : $key.hashCode());
        final java.lang.Object $archetypeType = this.getArchetypeType();
        result = result * PRIME + ($archetypeType == null ? 43 : $archetypeType.hashCode());
        final java.lang.Object $displayName = this.getDisplayName();
        result = result * PRIME + ($displayName == null ? 43 : $displayName.hashCode());
        final java.lang.Object $description = this.getDescription();
        result = result * PRIME + ($description == null ? 43 : $description.hashCode());
        return result;
    }

    @Override
    public java.lang.String toString() {
        return "ArchetypeDescription(key=" + this.getKey() + ", archetypeType=" + this.getArchetypeType() + ", displayName=" + this.getDisplayName() + ", description=" + this.getDescription() + ")";
    }
}
