package com.eden.orchid.api.theme.pages;

import com.eden.orchid.api.resources.resource.ExternalResource;
import com.eden.orchid.api.theme.components.OrchidComponent;

import java.util.Map;

/**
 * An OrchidExternalPage is little more than an OrchidReference which points to a page on an external site. Much of the
 * functionality for Pages is disabled for this page type, and is intended just to be used so external pages can be
 * linked to in the same way as internal pages.
 */
public class OrchidExternalPage extends OrchidPage {

    public OrchidExternalPage(OrchidReference reference) {
        super(new ExternalResource(reference));
    }

    @Override
    public Map<String, Class<? extends OrchidComponent>> getComponentClasses() {
        throw new UnsupportedOperationException("This method is not allowed on OrchidExternalPage");
    }

    @Override
    public Map<String, OrchidComponent> getComponents() {
        throw new UnsupportedOperationException("This method is not allowed on OrchidExternalPage");
    }

    @Override
    public void setComponentClasses(Map<String, Class<? extends OrchidComponent>> componentClasses) {
        throw new UnsupportedOperationException("This method is not allowed on OrchidExternalPage");
    }

    @Override
    public void setComponents(Map<String, OrchidComponent> components) {
        throw new UnsupportedOperationException("This method is not allowed on OrchidExternalPage");
    }

    @Override
    public void renderTemplate(String... templateName) {
        throw new UnsupportedOperationException("This method is not allowed on OrchidExternalPage");
    }

    @Override
    public void renderString(String extension, String templateString) {
        throw new UnsupportedOperationException("This method is not allowed on OrchidExternalPage");
    }

    @Override
    public void renderRaw() {
        throw new UnsupportedOperationException("This method is not allowed on OrchidExternalPage");
    }

    @Override
    public String getContent() {
        throw new UnsupportedOperationException("This method is not allowed on OrchidExternalPage");
    }

    @Override
    public void addComponent(Class<? extends OrchidComponent> component) {
        throw new UnsupportedOperationException("This method is not allowed on OrchidExternalPage");
    }

    @Override
    public void addComponent(String alias, Class<? extends OrchidComponent> component) {
        throw new UnsupportedOperationException("This method is not allowed on OrchidExternalPage");
    }

    @Override
    public void prepareComponents() {
        throw new UnsupportedOperationException("This method is not allowed on OrchidExternalPage");
    }

    @Override
    public OrchidComponent getComponent(String alias) {
        throw new UnsupportedOperationException("This method is not allowed on OrchidExternalPage");
    }
}
