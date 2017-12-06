package com.eden.orchid.materialize;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.OptionsHolder;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.options.annotations.StringDefault;
import com.eden.orchid.api.theme.Theme;
import com.eden.orchid.api.theme.models.About;

import javax.inject.Inject;

public class MaterializeTheme extends Theme {

    @Option public About about;

    @Option public MaterializeColors colors;

    @Option public MaterializeShades shades;

    @Inject
    public MaterializeTheme(OrchidContext context) {
        super(context, "Materialize", 100);
    }

    @Override
    public void addAssets() {
        addCss("assets/css/appStyles.scss");
        addJs("assets/js/jquery-2.1.1.min.js");
        addJs("assets/js/materialize.min.js");

        super.addAssets();
    }

    public static class MaterializeColors implements OptionsHolder {

        @Option
        @StringDefault("cyan")
        public String primary;

        @Option
        @StringDefault("orange")
        public String secondary;

        @Option
        @StringDefault("green")
        public String success;

        @Option
        @StringDefault("red")
        public String error;

        @Option
        @StringDefault("light-blue")
        public String link;

    }

    public static class MaterializeShades implements OptionsHolder {

        @Option
        @StringDefault("lighten-2")
        public String primary;

        @Option
        @StringDefault("lighten-1")
        public String secondary;

        @Option
        @StringDefault("base")
        public String success;

        @Option
        @StringDefault("base")
        public String error;

        @Option
        @StringDefault("darken-1")
        public String link;

    }

}
