package com.eden.orchid.api.theme.components;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.options.OptionsHolder;
import com.eden.orchid.api.registration.Prioritized;
import com.eden.orchid.api.theme.assets.AssetHolder;
import com.eden.orchid.api.theme.assets.AssetHolderDelegate;
import com.eden.orchid.utilities.OrchidUtils;
import lombok.Getter;
import lombok.Setter;

import javax.inject.Inject;

public abstract class OrchidComponent extends Prioritized implements OptionsHolder, AssetHolder {

    protected final OrchidContext context;

    @Getter protected final String key;
    @Getter protected final AssetHolder assetHolder;

    @Option
    @Description("Specify a list of templates to use when rendering this component. The first template that exists " +
            "will be chosen for this component.")
    @Getter @Setter
    protected String[] templates;

    @Option
    @Description("By default, components are rendered in the order in which they are declared, but the ordering can " +
            "be changed by setting the order on any individual component. A higher value for order will render that " +
            "component earlier in the list.")
    @Getter @Setter
    protected int order;

    @Getter @Setter @Option protected String[] extraCss;
    @Getter @Setter @Option protected String[] extraJs;

    @Inject
    public OrchidComponent(OrchidContext context, String key, int priority) {
        super(priority);
        this.key = key;
        this.context = context;
        this.assetHolder = new AssetHolderDelegate(context);
    }

    @Override
    public void addAssets() {
        OrchidUtils.addExtraAssetsTo(context, extraCss, extraJs, this);
    }
}
