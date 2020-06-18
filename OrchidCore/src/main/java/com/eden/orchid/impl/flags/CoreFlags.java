package com.eden.orchid.impl.flags;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.options.OrchidFlag;
import com.eden.orchid.api.options.annotations.BooleanDefault;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.options.annotations.FlagAliases;
import com.eden.orchid.api.options.annotations.IntDefault;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.options.annotations.StringDefault;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.Map;

public final class CoreFlags extends OrchidFlag {

// Base flags
//----------------------------------------------------------------------------------------------------------------------

    @Option
    @Description("the base URL to append to generated URLs.")
    public String baseUrl;

    @Option @StringDefault("./src")
    @Description("the directory containing your content files")
    public String src;

    @Option @StringDefault("./site")
    @FlagAliases({"d"})
    @Description("the output directory for all generated files")
    public String dest;

    @Option @StringDefault("build")
    @Description("the task to run")
    public String task;

    @Option @StringDefault("Default")
    @FlagAliases({"t"})
    @Description("the key of your selected Theme object")
    public String theme;

    @Option @StringDefault("Default")
    @Description("the fully-qualified classname of your selected Admin Theme")
    public String adminTheme;

    @Option @StringDefault("")
    @FlagAliases({"v"})
    @Description("the version of your library")
    public String version;

// Other flags
//----------------------------------------------------------------------------------------------------------------------

    @Option @StringDefault("debug")
    @FlagAliases({"e"})
    @Description("the development environment. Reads 'config-<environment>.yml' and may alter the behavior of " +
            "registered components."
    )
    public String environment;

    @Option @StringDefault("peb")
    @Description("Set the default extension to look for when loading templates. Themes set the extension they " +
            "prefer, but this value is used as a fallback. The default value is `peb` for Pebble."
    )
    public String defaultTemplateExtension;

    @Option @BooleanDefault(false)
    @Description("Whether to deploy dry")
    public boolean dryDeploy;

    @Option @StringDefault("VERBOSE")
    @Description("The level of logging statements to show. One of: [VERBOSE, DEBUG, INFO, DEFAULT, WARNING, ERROR, FATAL]")
    public Clog.Priority logLevel;

    @Option @IntDefault(8080)
    @Description("The port to run the development server on. The Websocket typically listens at a port 2 above the normal port.")
    public int port;

    @Option @BooleanDefault(false)
    @Description("Enable debug diagnostic logging.")
    public boolean diagnose;

    @Option @BooleanDefault(false)
    @Description("Return to legacy webserver functionality, returning files from disk instead of indexed pages.")
    public boolean legacyFileServer;

    @Override
    public Map<String, Value> getParsedFlags() {
        if(!environment.equals("test")) {
            Clog.getInstance().setMinPriority(logLevel);
        }

        // resolve absolute dir for source dir
        src = FilenameUtils.normalize(new File(src).getAbsolutePath());

        // resolve absolute dir for dest dir
        dest = FilenameUtils.normalize(new File(dest).getAbsolutePath());


        return super.getParsedFlags();
    }
}
