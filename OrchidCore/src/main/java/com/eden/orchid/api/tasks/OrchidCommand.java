package com.eden.orchid.api.tasks;

import com.eden.orchid.api.options.OptionsHolder;
import com.eden.orchid.api.registration.Prioritized;

/**
 * A Command represents something that can be executed via user-input in an interactive session (such as the
 * "interactive" task or from the admin command bar). Commands are very similar to tasks, but differ in the following
 * ways:
 * - Orchid runs a single Task in a session, but multiple Commands may be issued in a session
 * - A Task shuts Orchid down after it completes, but a command does not
 * - A Task does not have additional parameters passed to it; rather the command-line flags act as the Task options. In
 *      contrast, additional parameters may be passed to a command directly, being extracted as an OptionsHolder
 * - A Task expects a regular String name, while Commands may be matched against a Regex pattern
 */
public abstract class OrchidCommand extends Prioritized implements OptionsHolder {

    public OrchidCommand(int priority) {
        super(priority);
    }

    /**
     * Return a pattern to match this command on
     *
     * @return the pattern for this command
     */
    public abstract boolean matches(String commandName);

    public abstract String[] parameters();

    public abstract void run(String commandName) throws Exception;

}
