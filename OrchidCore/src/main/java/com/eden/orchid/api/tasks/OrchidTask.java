package com.eden.orchid.api.tasks;

import com.eden.orchid.api.options.Descriptive;
import com.eden.orchid.api.registration.Prioritized;
import lombok.Getter;

/**
 * A Runnable tailored for executing some task in Orchid. The 'name' of this OrchidTask is used on the command-line for
 * non-Javadoc builds to execute an alternative OrchidTask. The default OrchidTask builds the site once.
 *
 * @since v1.0.0
 * @orchidApi extensible
 */
public abstract class OrchidTask extends Prioritized implements Runnable, Descriptive {

    @Getter private final String name;
    @Getter private final TaskService.TaskType taskType;

    public OrchidTask(int priority, String name, TaskService.TaskType taskType) {
        super(priority);
        this.name = name;
        this.taskType = taskType;
    }

}
