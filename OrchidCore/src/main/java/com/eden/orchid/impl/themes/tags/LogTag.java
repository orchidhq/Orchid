package com.eden.orchid.impl.themes.tags;

import com.caseyjbrooks.clog.Clog;
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
        if(!EdenUtils.isEmpty(tag)) {
            Clog.pushTag(tag);
        }

        switch (level.toLowerCase()) {
            case "verbose": Clog.v(getContent()); break;
            case "debug": Clog.d(getContent()); break;
            case "info": Clog.i(getContent()); break;
            case "warning": Clog.w(getContent()); break;
            case "error": Clog.e(getContent()); break;
            case "fatal": Clog.wtf(getContent()); break;
            default: Clog.v(getContent()); break;
        }

        if(!EdenUtils.isEmpty(tag)) {
            Clog.popTag();
        }
    }
}