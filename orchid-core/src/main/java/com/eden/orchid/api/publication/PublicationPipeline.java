package com.eden.orchid.api.publication;

import clog.Clog;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.ValidationError;
import com.eden.orchid.api.theme.components.ModularList;
import kotlin.Pair;

import javax.inject.Inject;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public final class PublicationPipeline extends ModularList<PublicationPipeline, OrchidPublisher> {

    @Inject
    public PublicationPipeline() {
        super();
    }

    @Override
    protected Class<OrchidPublisher> getItemClass() {
        return OrchidPublisher.class;
    }

    public boolean publishAll(OrchidContext context) {
        return publishAll(context, false);
    }

    public boolean publishAll(OrchidContext context, boolean dryDeploy) {
        return publishAll(context, dryDeploy, null);
    }

    public boolean publishAll(OrchidContext context, boolean dryDeploy, BiConsumer<Integer, Integer> update) {
        List<OrchidPublisher> allPublishers = get(context);
        List<Pair<OrchidPublisher, List<ValidationError>>> invalidPublishers = new ArrayList<>();

        int progress = 0;
        final int maxProgress = allPublishers.size();

        boolean success = true;

        if(update != null) {
            update.accept(progress, maxProgress);
        }

        // validate all publishers first
        for(OrchidPublisher publisher : allPublishers) {
            List<ValidationError> errors = publisher.validate(context);
            if(!errors.isEmpty()) {
                invalidPublishers.add(new Pair<>(publisher, errors));
            }
        }

        // only if all are valid, should we publish any of them
        if(!invalidPublishers.isEmpty()) {
            final StringBuilder moduleLog = new StringBuilder();
            for (Pair<OrchidPublisher, List<ValidationError>> publisher : invalidPublishers) {
                moduleLog.append("\n * " + publisher.getFirst().getType());
                for(ValidationError error : publisher.getSecond()) {
                    moduleLog.append("\n   - " + error.getDescription());
                }
            }
            moduleLog.append("\n");
            Clog.e("Some publishing stages failed validation, cannot deploy:\n{}", moduleLog.toString());
            success = false;
        }
        else {
            for (OrchidPublisher publisher : allPublishers) {
                boolean publisherIsDry = dryDeploy || publisher.isDry();
                Clog.i("{}Publishing [{}: {}]", (publisherIsDry) ? "Dry " : "", publisher.getPriority(), publisher.getType());

                boolean publisherSuccess = true;
                if (!publisherIsDry) {
                    try {
                        publisher.publish(context);
                    }
                    catch (Exception e) {
                        Clog.e("Something went wrong publishing [{}]", publisher.getType());
                        context.diagnosisMessage(() -> {
                            StringWriter sw = new StringWriter();
                            PrintWriter pw = new PrintWriter(sw);
                            e.printStackTrace(pw);

                            return pw.toString();
                        });
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
