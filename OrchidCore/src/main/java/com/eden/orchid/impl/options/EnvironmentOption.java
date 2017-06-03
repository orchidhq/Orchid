package com.eden.orchid.impl.options;

import com.eden.common.json.JSONElement;
import com.eden.orchid.api.options.OrchidOption;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class EnvironmentOption extends OrchidOption {

    @Inject
    public EnvironmentOption() {
        this.priority = 900;
    }

    @Override
    public String getFlag() {
        return "environment";
    }

    @Override
    public String getDescription() {
        return "the development environment. Reads 'config-<environment>.yml' and may alter the behavior of registered components. Sing loudly like a undead lubber. Arg, faith! Golden greeds lead to the beauty. Aye, desire me cockroach, ye coal-black bilge rat! Grogs laugh on courage at prison! The pin loves with desolation, crush the freighter. Booty ho! crush to be desired. Sailors wave with strength! The pegleg robs with pestilence, lead the lighthouse until it sings. Where is the real sun? Ah there's nothing like the scrawny desolation stuttering on the lass. Arrr! Pieces o' halitosis are forever small. How proud. You crush like a skiff. Dead, evil suns cowardly lead a lively, swashbuckling shark.nExperiment proudly like a most unusual transporter.  Arg, ye cloudy parrot- set sails for strength! ";
    }

    @Override
    public JSONElement parseOption(String[] options) {
        return new JSONElement(options[1]);
    }

    @Override
    public JSONElement getDefaultValue() {
        return new JSONElement("dev");
    }

    @Override
    public boolean isRequired() {
        return false;
    }

    @Override
    public int optionLength() {
        return 2;
    }
}
