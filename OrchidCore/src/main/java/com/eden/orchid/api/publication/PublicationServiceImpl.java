package com.eden.orchid.api.publication;

import com.caseyjbrooks.clog.Clog;
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
public final class PublicationServiceImpl implements PublicationService {

    private final Set<OrchidPublisher> allPublishers;
    private OrchidContext context;

    @Getter @Setter
    @Option
    @Description("The publication pipeline stages")
    private PublicationPipeline stages;

    private int progress;
    private int maxProgress;

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
        boolean success = true;
        progress = 0;
        maxProgress = stages.get().size();
        context.broadcast(Orchid.Lifecycle.DeployProgress.fire(this, progress, maxProgress));

        for(OrchidPublisher publisher : stages.get()) {
            boolean publisherIsDry = dryDeploy || publisher.isDry();
            Clog.d("{}Publishing [{}: {}]", (publisherIsDry) ? "Dry " : "", publisher.getPriority(), publisher.getType());

            boolean publisherSuccess = true;

            if(publisher.validate()) {
                if(!publisherIsDry) {
                    try {
                        publisher.publish();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        Clog.e("Something went wrong publishing [{}]", e, publisher.getType());
                        publisherSuccess = false;
                    }
                }
            }
            else {
                Clog.e("Publisher validation failed for [{}]", publisher.getType());
                publisherSuccess = false;
            }

            progress++;
            context.broadcast(Orchid.Lifecycle.DeployProgress.fire(this, progress, maxProgress));

            if(!publisherSuccess) {
                success = false;
                break;
            }
        }

        if(!success) {
            context.broadcast(Orchid.Lifecycle.DeployProgress.fire(this, maxProgress, maxProgress));
        }

        return success;
    }

}
