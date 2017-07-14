package com.eden.orchid.api.theme.components;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.utilities.OrchidUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ComponentHolderDelegate implements ComponentHolder {

    private OrchidContext context;

    private Set<Class<? extends OrchidComponent>> componentClasses;
    private Set<OrchidComponent> components;

    @Inject
    public ComponentHolderDelegate(OrchidContext context) {
        this.context = context;
        componentClasses = new HashSet<>();
        components = null;
    }

    @Override
    public void addComponents(JSONArray components) {
        for (int i = 0; i < components.length(); i++) {
            try {
                Clog.v("Creating " + components.getString(i) + " component dynamically");

                Class<? extends OrchidComponent> componentClass = (Class<? extends OrchidComponent>) Class.forName(components.getString(i));
                addComponent(componentClass);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void addComponent(Class<? extends OrchidComponent> componentClass) {
        componentClasses.add(componentClass);
    }

    @Override
    public OrchidComponent getComponent(OrchidPage page, String componentKey) {
        for(OrchidComponent component : getComponents(page)) {
            if(component.getKey().equals(componentKey)) {
                component.setRendered(true);
                return component;
            }
        }

        return null;
    }

    @Override
    public Set<OrchidComponent> getComponents(OrchidPage page) {
        prepareAll(page);
        return components;
    }

    @Override
    public Set<OrchidComponent> getRemainingComponents(OrchidPage page) {
        return getComponents(page)
                .stream()
                .filter(OrchidUtils.not(OrchidComponent::isRendered))
                .collect(Collectors.toSet());
    }

    private void prepareAll(OrchidPage page) {
        if (components == null) {
            components = new HashSet<>();
            for (Class<? extends OrchidComponent> componentClass : componentClasses) {
                OrchidComponent component = context.getInjector().getInstance(componentClass);
                component.prepare(page);

                if(page.getData().has(component.getKey())) {
                    JSONObject componentData = page.getData().getJSONObject(component.getKey());
                    component.extractOptions(context, componentData);

                    if(componentData.has("priority")) {
                        component.setPriority(componentData.getInt("priority"));
                    }
                }
                else {
                    component.extractOptions(context, new JSONObject());
                }

                components.add(component);
            }
        }
    }
}
