package com.eden.orchid.api.theme.components;

import com.eden.orchid.api.theme.pages.OrchidPage;
import org.json.JSONArray;

import java.util.Set;

public interface ComponentHolder {

    void addComponents(JSONArray components);

    void addComponent(Class<? extends OrchidComponent> componentClass);

    OrchidComponent getComponent(OrchidPage page, String componentKey);

    Set<OrchidComponent> getComponents(OrchidPage page);

    Set<OrchidComponent> getRemainingComponents(OrchidPage page);
}
