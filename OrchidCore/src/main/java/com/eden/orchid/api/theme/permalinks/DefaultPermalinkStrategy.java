package com.eden.orchid.api.theme.permalinks;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.utilities.OrchidUtils;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

public class DefaultPermalinkStrategy implements PermalinkStrategy {

    private final Set<PermalinkPathType> injectedPathTypes;

    @Inject
    public DefaultPermalinkStrategy(Set<PermalinkPathType> injectedPathTypes) {
        this.injectedPathTypes = new TreeSet<>(injectedPathTypes);
    }

    @Override
    public void applyPermalink(OrchidPage page, String permalink) {
        String[] pieces = permalink.split("/");

        String resultingUrl = applyPermalinkTemplate(page, Arrays.copyOfRange(pieces, 0, pieces.length - 1));
        String title = applyPermalinkTemplatePiece(page, pieces[pieces.length - 1]);

        page.getReference().setPath(OrchidUtils.normalizePath(resultingUrl));
        page.getReference().setFileName(title);
        page.getReference().setUsePrettyUrl(true);
    }

    private String applyPermalinkTemplate(OrchidPage page, String[] pieces) {
        StringBuilder resultingUrl = new StringBuilder("");

        for (String piece : pieces) {
            resultingUrl.append(applyPermalinkTemplatePiece(page, piece));
        }

        return resultingUrl.toString();
    }

    private String applyPermalinkTemplatePiece(OrchidPage page, String piece) {
        String resultingPiece = null;
        String pieceKey = null;

        if (!EdenUtils.isEmpty(piece) && piece.startsWith(":")) {
            pieceKey = piece.substring(1);
        }
        else if (!EdenUtils.isEmpty(piece) && piece.startsWith("{") && piece.endsWith("}")) {
            pieceKey = piece.substring(1, piece.length() - 1);
        }

        if (pieceKey != null) {
            for (PermalinkPathType pathType : injectedPathTypes) {
                if (pathType.acceptsKey(page, pieceKey)) {
                    resultingPiece = pathType.format(page, pieceKey);
                }
            }

            if (resultingPiece == null) {
                throw new IllegalArgumentException(Clog.format("'{}' is not a valid permalink key", piece));
            }
        }
        else {
            resultingPiece = piece;
        }

        return OrchidUtils.toSlug(resultingPiece) + "/";
    }

}
