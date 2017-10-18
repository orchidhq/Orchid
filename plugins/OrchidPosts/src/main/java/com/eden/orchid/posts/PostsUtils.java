package com.eden.orchid.posts;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.utilities.OrchidUtils;

public class PostsUtils {

    public static String getPostFilename(OrchidResource entry, String baseCategoryPath) {
        String path = OrchidUtils.normalizePath(OrchidUtils.normalizePath(entry.getReference().getPath()).replace(baseCategoryPath, ""));
        String formattedFilename = (!EdenUtils.isEmpty(path))
                ? path.replaceAll("/", "-") + "-" + entry.getReference().getFileName()
                : entry.getReference().getFileName();

        return formattedFilename;
    }
}
