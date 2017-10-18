package com.eden.orchid.api.indexing;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.api.theme.pages.OrchidReference;
import com.eden.orchid.impl.indexing.OrchidCompositeIndex;
import com.eden.orchid.impl.indexing.OrchidInternalIndex;
import com.eden.orchid.impl.indexing.OrchidRootExternalIndex;
import com.eden.orchid.impl.indexing.OrchidRootInternalIndex;

import javax.inject.Singleton;
import java.util.Collection;
import java.util.List;

@Singleton
public final class IndexServiceImpl implements IndexService {

    private OrchidContext context;

    private OrchidRootInternalIndex internalIndex;
    private OrchidRootExternalIndex externalIndex;
    private OrchidCompositeIndex compositeIndex;

    @Override
    public void initialize(OrchidContext context) {
        this.context = context;
    }

    @Override
    public void clearIndex() {
        this.internalIndex = new OrchidRootInternalIndex();
        this.externalIndex = new OrchidRootExternalIndex();
    }

    @Override
    public OrchidIndex getIndex() {
        return internalIndex;
    }

    @Override
    public OrchidRootInternalIndex getInternalIndex() {
        return internalIndex;
    }

    @Override
    public OrchidRootExternalIndex getExternalIndex() {
        return externalIndex;
    }

    @Override
    public OrchidCompositeIndex getCompositeIndex() {
        return compositeIndex;
    }

    @Override
    public void mergeIndices(OrchidIndex... indices) {
        this.compositeIndex = new OrchidCompositeIndex("composite");
        for (OrchidIndex index : indices) {
            if (index != null) {
                this.compositeIndex.mergeIndex(index);
            }
        }
    }

    @Override
    public List<OrchidPage> getGeneratorPages(String generator) {
        return internalIndex.getGeneratorPages(generator);
    }

    public void addChildIndex(String key, OrchidInternalIndex index) {
        this.internalIndex.addChildIndex(key, index);
    }

    public void addExternalChildIndex(OrchidIndex index) {
        this.externalIndex.addChildIndex(index);
    }

    public OrchidIndex createIndex(String rootKey, Collection<? extends OrchidPage> pages) {
        OrchidIndex index = new OrchidInternalIndex(rootKey);

        for (OrchidPage page : pages) {
            OrchidReference ref = new OrchidReference(page.getReference());
            index.addToIndex(ref.getPath(), page);
        }

        return index;
    }

    @Override
    public OrchidPage findPageByName(String name) {
        throw new UnsupportedOperationException("This method is not yet implemented");
    }

    @Override
    public OrchidPage findPageByPath(String path) {
        throw new UnsupportedOperationException("This method is not yet implemented");
    }
}
