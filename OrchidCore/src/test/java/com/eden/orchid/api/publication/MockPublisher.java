package com.eden.orchid.api.publication;

import com.eden.orchid.api.OrchidContext;
import org.json.JSONObject;

public class MockPublisher extends OrchidPublisher {

    private final boolean isValid;
    private final boolean throwsException;

    public MockPublisher(OrchidContext context, String key, int priority, boolean isValid, boolean throwsException) {
        super(context, key, priority);
        this.isValid = isValid;
        this.throwsException = throwsException;
    }

    @Override
    public void extractOptions(OrchidContext context, JSONObject options) {
        setOrder(getPriority());
        setDry(options.optBoolean("dry", false));
    }

    @Override
    public boolean validate() {
        return isValid;
    }

    @Override
    public void publish() {
        if(throwsException) {
            throw new RuntimeException("This MockPublisher throws an exception during publishing");
        }
    }

    public static class ValidPublisher extends MockPublisher {
        public ValidPublisher(OrchidContext context) {
            super(context, "valid", 1000, true, false);
        }
    }

    public static class InvalidPublisher extends MockPublisher {
        public InvalidPublisher(OrchidContext context) {
            super(context, "invalid", 100, false, false);
        }
    }

    public static class CrashingPublisher extends MockPublisher {
        public CrashingPublisher(OrchidContext context) {
            super(context, "crashing", 10, true, true);
        }
    }
}
