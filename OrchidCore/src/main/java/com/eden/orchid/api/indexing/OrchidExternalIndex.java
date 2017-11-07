package com.eden.orchid.api.indexing;

public class OrchidExternalIndex extends OrchidIndex {

    public OrchidExternalIndex(String ownKey) {
        super(ownKey);
    }

    @Override
    public Class<? extends OrchidIndex> childIndexClass() {
        return OrchidExternalIndex.class;
    }

}
