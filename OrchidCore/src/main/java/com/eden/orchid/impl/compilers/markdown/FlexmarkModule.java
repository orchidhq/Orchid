package com.eden.orchid.impl.compilers.markdown;

import com.eden.orchid.OrchidModule;
import com.vladsch.flexmark.Extension;
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.gfm.tasklist.TaskListExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;

public class FlexmarkModule extends OrchidModule {

    @Override
    protected void configure() {
        addToSet(Extension.class,
                TablesExtension.create(),
                StrikethroughExtension.create(),
                TaskListExtension.create()
        );
    }
}
