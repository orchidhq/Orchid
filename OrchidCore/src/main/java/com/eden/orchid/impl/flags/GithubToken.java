package com.eden.orchid.impl.flags;

import com.eden.orchid.api.options.OrchidFlag;
import com.eden.orchid.api.options.annotations.Description;

@Description("Your GitHub Personal Access Token, required for GitHub Pages deploys.")
public final class GithubToken extends OrchidFlag {

    public GithubToken() {
        super("githubToken", true, false, "");
    }

}
