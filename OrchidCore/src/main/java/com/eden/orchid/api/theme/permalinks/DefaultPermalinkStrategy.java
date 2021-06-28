package com.eden.orchid.api.theme.permalinks;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.utilities.OrchidUtils;
import org.apache.commons.io.FilenameUtils;

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
        applyPermalink(page, permalink, true);
    }

    @Override
    public void applyPermalink(OrchidPage page, String permalink, boolean prettyUrl) {
        String extension = FilenameUtils.getExtension(permalink);
        String nameWithoutExtension = FilenameUtils.removeExtension(permalink);

        String[] pieces = OrchidUtils.normalizePath(nameWithoutExtension).split("/");

        StringBuffer formattedPath = new StringBuffer();
        StringBuffer formattedFileName = new StringBuffer();

        applyPermalinkTemplate(formattedPath, page, Arrays.copyOfRange(pieces, 0, pieces.length - 1));
        applyPermalinkTemplatePiece(formattedFileName, page, pieces[pieces.length - 1]);

        page.getReference().setPath(OrchidUtils.normalizePath(formattedPath.toString()).replaceAll("//", "/"));
        page.getReference().setFileName(OrchidUtils.normalizePath(formattedFileName.toString()).replaceAll("//", "/"));
        page.getReference().setUsePrettyUrl(prettyUrl);

        if(!EdenUtils.isEmpty(extension)) {
            page.getReference().setOutputExtension(extension);
        }
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
            throw new IllegalArgumentException("Permalink key cannot be empty (on page ${page.getTitle()})");
        }

        String resultingPiece = null;
        for (PermalinkPathType pathType : injectedPathTypes) {
            if (pathType.acceptsKey(page, pieceKey)) {
                resultingPiece = pathType.format(page, pieceKey);
                break;
            }
        }
        if (resultingPiece == null) {
            throw new IllegalArgumentException("'" + pieceKey + "' is not a valid permalink key");
        }

        resultingUrl.append(OrchidUtils.toSlug(resultingPiece));
    }

}
