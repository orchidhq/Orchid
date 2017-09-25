package com.eden.orchid.api.theme.components;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.Option;
import com.eden.orchid.api.options.OptionsHolder;
import com.eden.orchid.api.registration.Prioritized;
import com.eden.orchid.api.theme.assets.AssetHolder;
import com.eden.orchid.api.theme.assets.AssetHolderDelegate;
import lombok.Getter;
import lombok.Setter;

import javax.inject.Inject;

@Getter @Setter
public abstract class OrchidComponent extends Prioritized implements OptionsHolder, AssetHolder {

    protected String key;
    protected OrchidContext context;

    private boolean rendered;

    protected AssetHolder assets;

    @Option
    protected String[] templates;

    @Inject
    public OrchidComponent(int priority, String key, OrchidContext context) {
        super(priority);
        this.key = key;
        this.context = context;
        this.assets = new AssetHolderDelegate(context);
    }

    @Override
    public AssetHolder getAssetHolder() {
        return assets;
    }
}
