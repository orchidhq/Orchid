package com.eden.orchid.api.publication;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.theme.components.ModularList;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public final class PublicationPipeline extends ModularList<PublicationPipeline, OrchidPublisher> {

    @Inject
    public PublicationPipeline(OrchidContext context) {
        super(context);
    }

    @Override
    protected Class<OrchidPublisher> getItemClass() {
        return OrchidPublisher.class;
    }

    public boolean publishAll() {
        return publishAll(false);
    }

    public boolean publishAll(boolean dryDeploy) {
        return publishAll(dryDeploy, null);
    }

    public boolean publishAll(boolean dryDeploy, BiConsumer<Integer, Integer> update) {
        List<OrchidPublisher> allPublishers = get();
        List<OrchidPublisher> invalidPublishers = new ArrayList<>();

        int progress = 0;
        final int maxProgress = allPublishers.size();

        boolean success = true;

        if(update != null) {
            update.accept(progress, maxProgress);
        }

        // validate all publishers first
        for(OrchidPublisher publisher : allPublishers) {
            if(!publisher.validate(context)) {
                invalidPublishers.add(publisher);
            }
        }

        // only if all are valid, should we publish any of them
        if(invalidPublishers.size() > 0) {
            final StringBuilder moduleLog = new StringBuilder();
            for (OrchidPublisher publisher : allPublishers) {
                moduleLog.append("\n * " + publisher.getType());
            }
            moduleLog.append("\n");
            Clog.tag("Some publishing stages failed validation, cannot deploy").log(moduleLog.toString());
            success = false;
        }
        else {
            for (OrchidPublisher publisher : allPublishers) {
                boolean publisherIsDry = dryDeploy || publisher.isDry();
                Clog.d("{}Publishing [{}: {}]", (publisherIsDry) ? "Dry " : "", publisher.getPriority(), publisher.getType());

                boolean publisherSuccess = true;
                if (!publisherIsDry) {
                    try {
                        publisher.publish(context);
                    }
                    catch (Exception e) {
                        Clog.e("Something went wrong publishing [{}]", e, publisher.getType());
                        publisherSuccess = false;
                    }
                }

                if (!publisherSuccess) {
                    success = false;
                    break;
                }
                else {
                    progress++;
                    if (update != null) {
                        update.accept(progress, maxProgress);
                    }
                }
            }

        }

        if (!success && update != null) {
            update.accept(maxProgress, maxProgress);
        }
        return success;
    }

}
