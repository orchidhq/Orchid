package com.eden.orchid.api.publication;

import com.eden.orchid.Orchid;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.options.annotations.Option;
import lombok.Getter;
import lombok.Setter;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Set;
import java.util.TreeSet;

/**
 * @since v1.0.0
 * @orchidApi services
 */
@Singleton
@Description(value = "How Orchid publishes your site to production.", name = "Publications")
public final class PublicationServiceImpl implements PublicationService {

    private final Set<OrchidPublisher> allPublishers;
    private OrchidContext context;

    @Getter @Setter
    @Option
    @Description("The publication pipeline stages")
    private PublicationPipeline stages;

    @Inject
    public PublicationServiceImpl(Set<OrchidPublisher> publishers) {
        this.allPublishers = new TreeSet<>(publishers);
    }

    @Override
    public void initialize(OrchidContext context) {
        this.context = context;
    }

// Indexing phase
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public boolean publishAll(boolean dryDeploy) {
        return stages.publishAll(dryDeploy,
                (progress, maxProgress) ->
                        context.broadcast(Orchid.Lifecycle.ProgressEvent.fire(this, "deploying", progress, maxProgress)));
    }

}
