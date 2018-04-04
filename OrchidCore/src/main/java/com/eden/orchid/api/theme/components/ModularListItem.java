package com.eden.orchid.api.theme.components;

import com.eden.orchid.api.options.OptionsHolder;
import com.eden.orchid.api.theme.pages.OrchidPage;
import org.json.JSONArray;

import java.util.List;

public interface ModularListItem<L extends ModularList<L, I>, I extends ModularListItem<L, I>> extends OptionsHolder {

    String getType();

    void setOrder(int order);

    int getOrder();

    boolean canBeUsedOnPage(OrchidPage containingPage, L modularList, JSONArray possibleItems, List<I> currentItems);

    void setPage(OrchidPage containingPage);

}
