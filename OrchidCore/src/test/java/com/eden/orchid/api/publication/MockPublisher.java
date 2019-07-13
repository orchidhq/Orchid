package com.eden.orchid.api.publication;

import com.eden.orchid.api.OrchidContext;

import java.util.Map;

public class MockPublisher extends OrchidPublisher {

    private final boolean isValid;
    private final boolean throwsException;

    public MockPublisher(String key, int priority, boolean isValid, boolean throwsException) {
        super(key, priority);
        this.isValid = isValid;
        this.throwsException = throwsException;
    }

    @Override
    public void extractOptions(OrchidContext context, Map<String, Object> options) {
        setOrder(getPriority());
        setDry(options.get("dry") != null && Boolean.parseBoolean(options.get("dry").toString()));

    }

    @Override
    public boolean validate(OrchidContext context) {
        return isValid;
    }

    @Override
    public void publish(OrchidContext context) {
        if(throwsException) {
            throw new RuntimeException("This MockPublisher throws an exception during publishing");
        }
    }

    public static class ValidPublisher extends MockPublisher {
        public ValidPublisher() {
            super("valid", 1000, true, false);
        }
    }

    public static class InvalidPublisher extends MockPublisher {
        public InvalidPublisher() {
            super("invalid", 100, false, false);
        }
    }

    public static class CrashingPublisher extends MockPublisher {
        public CrashingPublisher() {
            super("crashing", 10, true, true);
        }
    }
}
