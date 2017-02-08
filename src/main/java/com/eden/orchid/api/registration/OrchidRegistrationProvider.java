package com.eden.orchid.api.registration;

/**
 * The various components within Orchid are registered dynamically by searching the classpath for classes annotated with
 * `@AutoRegister`. Those components can be any type of Object, including the other interfaces defined within Orchid, or
 * anything created with another extension. A OrchidRegistrationProvider provides a way for those components to be grouped
 * together.
 *
 * Before `@AutoRegister`ed classes are found on the classpath, OrchidRegistrationProvider classes are found. Every object found
 * with `@AutoRegister` is then passed to every OrchidRegistrationProvider, so one Object can be registered in multiple places.
 * It is the job of a OrchidRegistrationProvider to manage all Objects being registered with it and what to do with them
 * afterward.
 */
public interface OrchidRegistrationProvider extends Prioritized, Contextual {

    /**
     * Register an Object with this Provider. Registraion should be based on what the object is an instance of.
     *
     * @param object the `@AutoRegister`ed object to attempt to register here
     */
    void register(Object object);
}
