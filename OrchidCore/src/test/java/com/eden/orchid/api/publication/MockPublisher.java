package com.eden.orchid.api.publication;

import com.eden.orchid.api.OrchidContext;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

@Getter @Setter
public class MockPublisher extends OrchidPublisher {

    private boolean isValid;
    private RuntimeException thrownException;

    public MockPublisher(OrchidContext context, String key, int priority, boolean isValid, RuntimeException thrownException) {
        super(context, key, priority);
        this.isValid = isValid;
        this.thrownException = thrownException;
    }

    @Override
    public void extractOptions(OrchidContext context, JSONObject options) {

    }

    @Override
    public boolean validate() {
        return isValid;
    }

    @Override
    public void publish() {
        if(thrownException != null) {
            throw thrownException;
        }
    }
}
