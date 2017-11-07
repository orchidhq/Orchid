package com.eden.orchid.api.indexing;

public class OrchidInternalIndex extends OrchidIndex {

    public OrchidInternalIndex(String ownKey) {
        super(ownKey);
    }

    @Override
    public Class<? extends OrchidIndex> childIndexClass() {
        return OrchidInternalIndex.class;
    }

}
