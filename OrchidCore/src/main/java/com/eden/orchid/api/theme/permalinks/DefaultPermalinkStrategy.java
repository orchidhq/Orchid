package com.eden.orchid.api.theme.permalinks;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.utilities.OrchidUtils;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class DefaultPermalinkStrategy implements PermalinkStrategy {

    private final Set<PermalinkPathType> injectedPathTypes;

    @Inject
    public DefaultPermalinkStrategy(Set<PermalinkPathType> injectedPathTypes) {
        this.injectedPathTypes = new TreeSet<>(injectedPathTypes);
    }

    @Override
    public void applyPermalink(OrchidPage page, String permalink) {
        String[] pieces = permalink.split("/");

        StringBuffer resultingUrl = new StringBuffer();
        StringBuffer title = new StringBuffer();

        applyPermalinkTemplate(resultingUrl, page, Arrays.copyOfRange(pieces, 0, pieces.length - 1));
        applyPermalinkTemplatePiece(title, page, pieces[pieces.length - 1]);

        page.getReference().setPath(OrchidUtils.normalizePath(resultingUrl.toString()));
        page.getReference().setFileName(title.toString());
        page.getReference().setUsePrettyUrl(true);
    }

    private void applyPermalinkTemplate(StringBuffer resultingUrl, OrchidPage page, String[] pieces) {
        for (String piece : pieces) {
            applyPermalinkTemplatePiece(resultingUrl, page, piece);
        }
    }

    private void applyPermalinkTemplatePiece(StringBuffer resultingUrl, OrchidPage page, String piece) {
        if (!EdenUtils.isEmpty(piece) && piece.startsWith(":")) {
            getReplacement(resultingUrl, page, piece.substring(1));
        }
        else if (!EdenUtils.isEmpty(piece)) {
            Matcher matcher = Pattern.compile("\\{(.*?)}").matcher(piece);

            while (matcher.find()) {
                matcher.appendReplacement(resultingUrl, "");
                getReplacement(resultingUrl, page, matcher.group(1));
            }
            matcher.appendTail(resultingUrl);
        }

        resultingUrl.append("/");
    }

    private void getReplacement(StringBuffer resultingUrl, OrchidPage page, String pieceKey) {
        if (EdenUtils.isEmpty(pieceKey)) {
            throw new IllegalArgumentException(Clog.format("Permalink key cannot be empty (on page {})", page.getTitle()));
        }

        String resultingPiece = null;
        for (PermalinkPathType pathType : injectedPathTypes) {
            if (pathType.acceptsKey(page, pieceKey)) {
                resultingPiece = pathType.format(page, pieceKey);
                break;
            }
        }
        if (resultingPiece == null) {
            throw new IllegalArgumentException(Clog.format("'{}' is not a valid permalink key", pieceKey));
        }

        resultingUrl.append(OrchidUtils.toSlug(resultingPiece));
    }

}
