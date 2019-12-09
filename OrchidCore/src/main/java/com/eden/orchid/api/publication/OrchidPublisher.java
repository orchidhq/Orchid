package com.eden.orchid.api.publication;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.OptionsHolder;
import com.eden.orchid.api.options.annotations.AllOptions;
import com.eden.orchid.api.options.annotations.Archetype;
import com.eden.orchid.api.options.annotations.BooleanDefault;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.options.annotations.IntDefault;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.options.archetypes.ConfigArchetype;
import com.eden.orchid.api.registration.Prioritized;
import com.eden.orchid.api.server.annotations.Extensible;
import com.eden.orchid.api.tasks.OrchidCommand;
import com.eden.orchid.api.tasks.OrchidTask;
import com.eden.orchid.api.theme.components.ModularListItem;

import java.util.Map;

/**
 * An OrchidPublisher is a task that gets run when the Orchid site is done building and is ready to be deployed, such as
 * uploading the site's files to a remote server, or optimizing the resulting files. Publication can be run with the
 * `deploy` {@link OrchidTask} or {@link OrchidCommand}. Publication can optionally be run as a _dry deploy_, where each
 * OrchidPublisher is loaded and its options extracted and validated to ensure it is set up correctly, but the actual
 * `publish()` method is never called.
 *
 * When `deploy` is run as a command, the default behavior is to do a dry deploy. This can be configured with the `dry`
 * command parameter. In contrast, when `deploy` is run as a task, it is a full deploy. This can be configured with the
 * `dryDeploy` command-line flag. In addition, individual stages of the publication can be individually run as a dry run
 * or disabled completely.
 *
 * OrchidPublishers are prioritized, and they are executed in order from highest priority to low. This means that
 * Publishers implement a complete _post-processing pipeline_ rather than just deploying code, and can be reliably used
 * to do any kind of post-processing or deployment on the generated site. Any failed validation or exceptions thrown by
 * a publisher terminate the entire publication phase.
 *
 * @since v1.0.0
 * @orchidApi extensible
 */
@Extensible
@Description(value = "Deploy your site after it has finished being built.", name = "Publishers")
@Archetype(value = ConfigArchetype.class, key = "allPublishers")
public abstract class OrchidPublisher extends Prioritized implements OptionsHolder, ModularListItem<PublicationPipeline, OrchidPublisher> {

    protected final String type;

    @Option
    @BooleanDefault(false)
    private boolean dry;

    @Option
    @IntDefault(0)
    @Description("Manually set the order in which this publisher executes")
    protected int order;

    @AllOptions
    private Map<String, Object> allData;

    public OrchidPublisher(String type, int priority) {
        super(priority);
        this.type = type;
    }

    public OrchidPublisher(String type) {
        this(type, 100);
    }

    /**
     * A callback to run the publication step.
     * @param context
     */
    public abstract void publish(OrchidContext context) throws Exception;

    protected boolean exists(String value, String message) {
        if (EdenUtils.isEmpty(value)) {
            Clog.e(message);
            return false;
        }
        return true;
    }

    protected boolean exists(Object value, String message) {
        if (value == null) {
            Clog.e(message);
            return false;
        }
        return true;
    }

    public String getType() {
        return this.type;
    }

    public boolean isDry() {
        return this.dry;
    }

    public void setDry(final boolean dry) {
        this.dry = dry;
    }

    public int getOrder() {
        return this.order;
    }

    public void setOrder(final int order) {
        this.order = order;
    }

    public Map<String, Object> getAllData() {
        return this.allData;
    }

    public void setAllData(final Map<String, Object> allData) {
        this.allData = allData;
    }
}
