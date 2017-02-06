package com.eden.orchid.resources;

/**
 * Resources in Orchid are hierarchical, and trying to find a Resource from the OrchidResouces will return the first
 * one it finds in the following order:
 *
 * 1) A file in the directory specified by -resourcesDir
 * 2) An entry from a ResourceSource, with higher priority given preference.
 *
 * This is accomplished by finding the Jarfile in which a particular ResourceSource is contained within, and then
 * looking for an entry in that jar matching a given path. Having a higher priority means the jar associated with this
 * ResourceSource is searched before other Jars.
 */
public interface ResourceSource {

    /**
     * Return the priority of this ResourceSource. Options, Generators, etc. can also be ResourceSources, and the
     * priority of each is kept independent.
     *
     * @return this ResourceSource's priority
     */
    int resourcePriority();
}
