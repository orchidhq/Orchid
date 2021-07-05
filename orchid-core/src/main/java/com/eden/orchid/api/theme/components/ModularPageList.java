package com.eden.orchid.api.theme.components;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.theme.pages.OrchidPage;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public abstract class ModularPageList<
        L extends ModularPageList<L, I>,
        I extends ModularPageListItem<L, I>
> extends ModularList<L, I> {

    private OrchidPage containingPage;

    @Inject
    public ModularPageList() {
        super();
    }

    public ModularPageList(Function<OrchidContext, Map<String, Class<I>>> itemTypesProvider) {
        super(itemTypesProvider);
    }

    @Override
    protected void addItem(I item, Map<String, Object> itemJson) {
        if (item.canBeUsedOnPage(containingPage, (L) this, itemsJson, loadedItems)) {
            item.initialize(containingPage.getContext(), containingPage);
            item.extractOptions(containingPage.getContext(), itemJson);
            super.addItem(item, itemJson);
        }
    }

    @Override
    protected String getLogMessage() {
        return "(on page '" + containingPage.getTitle() + "' at " + containingPage.getLink() + ")";
    }

    @Override
    public final List<I> get(OrchidContext context) {
        throw new UnsupportedOperationException("Please use get(OrchidPage page)");
    }

    public synchronized List<I> get(@Nonnull OrchidPage page) {
        if(page == null) throw new NullPointerException("Page cannot be null");

        containingPage = page;
        List<I> listItems = super.get(containingPage.getContext());
        containingPage = null;

        return listItems;
    }

}
