package com.eden.orchid.impl.flags;

import com.eden.orchid.api.options.OrchidFlag;
import com.eden.orchid.api.options.annotations.Description;

@Description("Your Netlify Personal Access Token, required for Netlify deploys.")
public final class NetlifyToken extends OrchidFlag {

    public NetlifyToken() {
        super("netlifyToken", true, false, "");
    }

}
