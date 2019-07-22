package com.eden.orchid.api.options;

import java.util.List;

public final class OptionHolderDescription {
    private final String descriptiveName;
    private final String classDescription;
    private final List<OptionsDescription> optionsDescriptions;
    private final List<ArchetypeDescription> archetypeDescriptions;

    public OptionHolderDescription(final String descriptiveName, final String classDescription, final List<OptionsDescription> optionsDescriptions, final List<ArchetypeDescription> archetypeDescriptions) {
        this.descriptiveName = descriptiveName;
        this.classDescription = classDescription;
        this.optionsDescriptions = optionsDescriptions;
        this.archetypeDescriptions = archetypeDescriptions;
    }

    public String getDescriptiveName() {
        return this.descriptiveName;
    }

    public String getClassDescription() {
        return this.classDescription;
    }

    public List<OptionsDescription> getOptionsDescriptions() {
        return this.optionsDescriptions;
    }

    public List<ArchetypeDescription> getArchetypeDescriptions() {
        return this.archetypeDescriptions;
    }

    @Override
    public boolean equals(final java.lang.Object o) {
        if (o == this) return true;
        if (!(o instanceof OptionHolderDescription)) return false;
        final OptionHolderDescription other = (OptionHolderDescription) o;
        final java.lang.Object this$descriptiveName = this.getDescriptiveName();
        final java.lang.Object other$descriptiveName = other.getDescriptiveName();
        if (this$descriptiveName == null ? other$descriptiveName != null : !this$descriptiveName.equals(other$descriptiveName)) return false;
        final java.lang.Object this$classDescription = this.getClassDescription();
        final java.lang.Object other$classDescription = other.getClassDescription();
        if (this$classDescription == null ? other$classDescription != null : !this$classDescription.equals(other$classDescription)) return false;
        final java.lang.Object this$optionsDescriptions = this.getOptionsDescriptions();
        final java.lang.Object other$optionsDescriptions = other.getOptionsDescriptions();
        if (this$optionsDescriptions == null ? other$optionsDescriptions != null : !this$optionsDescriptions.equals(other$optionsDescriptions)) return false;
        final java.lang.Object this$archetypeDescriptions = this.getArchetypeDescriptions();
        final java.lang.Object other$archetypeDescriptions = other.getArchetypeDescriptions();
        if (this$archetypeDescriptions == null ? other$archetypeDescriptions != null : !this$archetypeDescriptions.equals(other$archetypeDescriptions)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final java.lang.Object $descriptiveName = this.getDescriptiveName();
        result = result * PRIME + ($descriptiveName == null ? 43 : $descriptiveName.hashCode());
        final java.lang.Object $classDescription = this.getClassDescription();
        result = result * PRIME + ($classDescription == null ? 43 : $classDescription.hashCode());
        final java.lang.Object $optionsDescriptions = this.getOptionsDescriptions();
        result = result * PRIME + ($optionsDescriptions == null ? 43 : $optionsDescriptions.hashCode());
        final java.lang.Object $archetypeDescriptions = this.getArchetypeDescriptions();
        result = result * PRIME + ($archetypeDescriptions == null ? 43 : $archetypeDescriptions.hashCode());
        return result;
    }

    @Override
    public java.lang.String toString() {
        return "OptionHolderDescription(descriptiveName=" + this.getDescriptiveName() + ", classDescription=" + this.getClassDescription() + ", optionsDescriptions=" + this.getOptionsDescriptions() + ", archetypeDescriptions=" + this.getArchetypeDescriptions() + ")";
    }
}
