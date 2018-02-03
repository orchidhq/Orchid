package com.eden.orchid.api.server;

import com.eden.orchid.api.registration.Prioritized;
import com.eden.orchid.api.server.annotations.Get;
import com.eden.orchid.api.server.annotations.Post;
import com.eden.orchid.api.server.annotations.Put;
import com.eden.orchid.api.server.annotations.Delete;

/**
 * Custom routes can be registered for the local server as methods annotated in a Controller class. Any method in the
 * Controller can be annotated with {@link Get}, {@link Post}, {@link Put}, {@link Delete} will be registered as
 * routes. You may set a "path namespace" to consider all paths in this controller as sub-paths prefixed by the path
 * namespace.
 *
 * Controller methods should always accept a first parameter of OrchidRequest, and also String parameters for all path
 * placeholders. In addition, you may create an OptionsHolder class which loads the parameters from the request and
 * specify that in the annotation and also have it passed as the last argument in the method parameters.
 *
 * @since v1.0.0
 * @extensible classes
 */
public abstract class OrchidController extends Prioritized {

    public OrchidController(int priority) {
        super(priority);
    }

    public String getPathNamespace()  {
        return "/" + this.getClass().getSimpleName().toLowerCase().replace("controller", "");
    }
}
