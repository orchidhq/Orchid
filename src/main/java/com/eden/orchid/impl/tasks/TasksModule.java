package com.eden.orchid.impl.tasks;

import com.eden.orchid.OrchidModule;
import com.eden.orchid.api.tasks.OrchidTask;

public class TasksModule extends OrchidModule {

    @Override
    protected void configure() {
        addToSet(OrchidTask.class, BuildTask.class);
        addToSet(OrchidTask.class, ListCompilersTask.class);
        addToSet(OrchidTask.class, ListGeneratorsTask.class);
        addToSet(OrchidTask.class, ListOptionsTask.class);
        addToSet(OrchidTask.class, ListResourceSourcesTask.class);
        addToSet(OrchidTask.class, ListTasksTask.class);
        addToSet(OrchidTask.class, ListThemesTask.class);
        addToSet(OrchidTask.class, ServeTask.class);
    }
}
