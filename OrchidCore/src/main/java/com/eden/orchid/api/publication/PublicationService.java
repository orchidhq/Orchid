package com.eden.orchid.api.publication;

import com.eden.orchid.api.OrchidService;
import com.google.inject.ImplementedBy;

/**
 * @since v1.0.0
 * @orchidApi services
 */
@ImplementedBy(PublicationServiceImpl.class)
public interface PublicationService extends OrchidService {

    default boolean publishAll(boolean dryDeploy) {
        return getService(PublicationService.class).publishAll(dryDeploy);
    }

}
