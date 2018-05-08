package com.eden.orchid.api.publication;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.json.JSONElement;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.Orchid;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.utilities.OrchidUtils;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    @Description("Whitelist the publishers in this array, only publishing with these publishers.")
    private String[] enabled;

    @Getter @Setter
    @Option
    @Description("Blacklist the publishers in this array, not publishing these publishers.")
    private String[] disabled;

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
        maxProgress = getFilteredPublishers().toArray().length;
        context.broadcast(Orchid.Lifecycle.DeployProgress.fire(this, progress, maxProgress));

        for(OrchidPublisher publisher : getFilteredPublishers()) {
            boolean publisherIsDry = dryDeploy || publisher.isDry();
            Clog.d("{}Publishing [{}: {}]", (publisherIsDry) ? "Dry " : "", publisher.getPriority(), publisher.getKey());

            JSONElement el = context.query(getKey() + "." + publisher.getKey());
            if (OrchidUtils.elementIsObject(el)) {
                publisher.extractOptions(context, (JSONObject) el.getElement());
            }
            else {
                publisher.extractOptions(context, new JSONObject());
            }

            boolean publisherSuccess = true;

            if(publisher.validate()) {
                if(!publisherIsDry) {
                    try {
                        publisher.publish();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        Clog.e("Something went wrong publishing [{}]", e, publisher.getKey());
                        publisherSuccess = false;
                    }
                }
            }
            else {
                Clog.e("Publisher validation failed for [{}]", publisher.getKey());
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

// Utilities
//----------------------------------------------------------------------------------------------------------------------

    List<OrchidPublisher> getFilteredPublishers() {
        return getFilteredPublishers(enabled, disabled).collect(Collectors.toList());
    }

    Stream<OrchidPublisher> getFilteredPublishers(String[] include, String[] exclude) {
        return getFilteredPublishers(allPublishers.stream(), include, exclude);
    }

    Stream<OrchidPublisher> getFilteredPublishers(Stream<OrchidPublisher> publishers, String[] include, String[] exclude) {
        Stream<OrchidPublisher> publisherStream = publishers;

        if(!EdenUtils.isEmpty(exclude)) {
            publisherStream = publisherStream
                    .filter(publisher -> !OrchidUtils.inArray(publisher, exclude, (publisher1, s) -> publisher1.getKey().equals(s)));
        }

        if(!EdenUtils.isEmpty(include)) {
            publisherStream = publisherStream
                    .filter(publisher -> OrchidUtils.inArray(publisher, include, (publisher1, s) -> publisher1.getKey().equals(s)));
        }

        return publisherStream;
    }

}
