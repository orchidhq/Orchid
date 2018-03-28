package com.eden.orchid.impl.themes.tags;

import com.caseyjbrooks.clog.Clog;
import com.caseyjbrooks.clog.IClog;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.compilers.TemplateTag;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.options.annotations.StringDefault;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class LogTag extends TemplateTag {

    @Option @StringDefault("verbose")
    @Description("The priority with which to log the message.")
    public String level;

    @Option
    @Description("An options tag to display with the message.")
    public String tag;

    @Inject
    public LogTag() {
        super("log", true, false);
    }

    @Override
    public String[] parameters() {
        return new String[] {"level", "tag"};
    }

    @Override
    public void onPostExtraction() {
        IClog clog = (!EdenUtils.isEmpty(tag)) ? Clog.tag(tag) : Clog.getInstance();

        switch (level.toLowerCase()) {
            case "verbose": clog.v(getContent()); break;
            case "debug": clog.d(getContent()); break;
            case "info": clog.i(getContent()); break;
            case "warning": clog.w(getContent()); break;
            case "error": clog.e(getContent()); break;
            case "fatal": clog.wtf(getContent()); break;
            default: clog.v(getContent()); break;
        }
    }
}