package com.eden.orchid.api.theme.components;

import com.eden.orchid.api.theme.pages.OrchidPage;
import org.json.JSONArray;

import java.util.List;

public interface ModularPageListItem<L extends ModularPageList<L, I>, I extends ModularPageListItem<L, I>> extends ModularListItem<L, I> {

    boolean canBeUsedOnPage(OrchidPage containingPage, L modularList, JSONArray possibleItems, List<I> currentItems);

    void setPage(OrchidPage containingPage);

}
