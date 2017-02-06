package com.eden.orchid.programs;

/**
 * A Runnable tailored for executing some task in Orchid. The 'name' of this Program is used on the command-line for
 * non-Javadoc builds to execute an alternative Program. The default Program builds the site once.
 */
public interface Program extends Runnable {

    /**
     * Return the name of this Program
     *
     * @return the name of this program
     */
    String getName();

    /**
     * Return a description of what this Program does, which is displayed when listing available Programs.
     *
     * @return the description of this Program
     */
    String getDescription();

}
