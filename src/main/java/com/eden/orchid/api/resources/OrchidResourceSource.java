package com.eden.orchid.api.resources;

import com.eden.orchid.api.registration.Contextual;

/**
 * Resources in Orchid are hierarchical, and trying to find a Resource from the OrchidResouces will return the first
 * one it finds in the following order:
 *
 * 1) A file in the directory specified by -resourcesDir
 * 2) An entry from a OrchidResourceSource, with higher priority given preference.
 *
 * This is accomplished by finding the Jarfile in which a particular OrchidResourceSource is contained within, and then
 * looking for an entry in that jar matching a given path. Having a higher priority means the jar associated with this
 * OrchidResourceSource is searched before other Jars.
 */
public interface OrchidResourceSource extends Contextual {

    /**
     * Return the priority of this OrchidResourceSource. Options, Generators, etc. can also be ResourceSources, and the
     * priority of each is kept independent.
     *
     * @return this OrchidResourceSource's priority
     */
    int getResourcePriority();

    /**
     * Set the priority of this resource source. A value less than 0 removes this source from consideration when
     * attempting to resolve resources.
     *
     * @param priority the new priority value
     */
    void setResourcePriority(int priority);
}
