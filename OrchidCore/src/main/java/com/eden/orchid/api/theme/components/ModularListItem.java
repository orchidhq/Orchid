package com.eden.orchid.api.theme.components;

import com.eden.orchid.api.options.OptionsHolder;

public interface ModularListItem<L extends ModularList<L, I>, I extends ModularListItem<L, I>> extends OptionsHolder {

    String getType();

    void setOrder(int order);

    int getOrder();

}
