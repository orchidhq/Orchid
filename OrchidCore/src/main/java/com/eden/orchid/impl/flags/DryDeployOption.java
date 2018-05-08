package com.eden.orchid.impl.flags;

import com.eden.orchid.api.options.OrchidFlag;
import com.eden.orchid.api.options.annotations.Description;

@Description("Whether to deploy dry")
public final class DryDeployOption extends OrchidFlag {

    public DryDeployOption() {
        super("dryDeploy", false, true, false);
    }

    @Override
    public Boolean parseFlag(String[] options) {
        return Boolean.parseBoolean(options[1]);
    }

    @Override
    public OrchidFlag.FlagType getFlagType() {
        return FlagType.BOOLEAN;
    }
}
