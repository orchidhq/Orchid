package com.eden.orchid.api.theme.components;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.google.inject.Provider;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

public abstract class ModularPageList<L extends ModularPageList<L, I>, I extends ModularPageListItem<L, I>> extends ModularList<L, I> {

    private OrchidPage containingPage;

    @Inject
    public ModularPageList(OrchidContext context) {
        super(context);
    }

    public ModularPageList(OrchidContext context, Provider<Map<String, Class<I>>> itemTypesProvider) {
        super(context, itemTypesProvider);
    }

    @Override
    protected void addItem(I item, Map<String, Object> itemJson) {
        if (item.canBeUsedOnPage(containingPage, (L) this, itemsJson, loadedItems)) {
            item.initialize(containingPage.getContext(), containingPage);
            item.extractOptions(context, itemJson);
            super.addItem(item, itemJson);
        }
    }

    @Override
    protected String getLogMessage() {
        return Clog.format("(on page '{}' at {})", containingPage.getTitle(), containingPage.getLink());
    }

    @Override
    public final List<I> get() {
        throw new UnsupportedOperationException("Please use get(OrchidPage page)");
    }

    public synchronized List<I> get(OrchidPage page) {
        containingPage = page;
        List<I> listItems = super.get();
        containingPage = null;

        return listItems;
    }

}
