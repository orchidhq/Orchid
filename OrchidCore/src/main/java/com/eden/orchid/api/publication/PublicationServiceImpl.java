package com.eden.orchid.api.publication;

import com.eden.orchid.Orchid;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.annotations.Archetype;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.options.archetypes.ConfigArchetype;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@Description(value = "How Orchid publishes your site to production.", name = "Publications")
@Archetype(value = ConfigArchetype.class, key = "services.publications")
public final class PublicationServiceImpl implements PublicationService {

    private OrchidContext context;

    @Option
    @Description("The publication pipeline stages")
    private PublicationPipeline stages;

    @Inject
    public PublicationServiceImpl() {

    }

    @Override
    public void initialize(OrchidContext context) {
        this.context = context;
    }

    // Indexing phase
//----------------------------------------------------------------------------------------------------------------------
    @Override
    public boolean publishAll(boolean dryDeploy) {
        return stages.publishAll(dryDeploy, (progress, maxProgress) -> context.broadcast(Orchid.Lifecycle.ProgressEvent.fire(this, "deploying", progress, maxProgress)));
    }

    public PublicationPipeline getStages() {
        return this.stages;
    }

    public void setStages(final PublicationPipeline stages) {
        this.stages = stages;
    }
}
