package com.eden.orchid.posts.permalink;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.posts.PostsModel;
import com.eden.orchid.posts.pages.PermalinkPage;
import com.eden.orchid.utilities.OrchidUtils;

import javax.inject.Inject;
import java.util.Set;
import java.util.TreeSet;

public class PostsPermalinkStrategy {

    private OrchidContext context;
    private PostsModel postsModel;
    private Set<PermalinkPathType> pathTypes;

    @Inject
    public PostsPermalinkStrategy(OrchidContext context, PostsModel postsModel, Set<PermalinkPathType> pathTypes) {
        this.context = context;
        this.postsModel = postsModel;
        this.pathTypes = new TreeSet<>(pathTypes);
    }

    public void applyPermalink(PermalinkPage page) {
        applyPermalink(page, getPermalinkTemplate(page));
    }

    public void applyPermalink(OrchidPage page, String permalink) {
        String[] pieces = permalink.split("/");

        String resultingUrl = applyPermalinkTemplate(page, pieces);
        String title = applyPermalinkTemplatePiece(page, pieces[pieces.length - 1]);

        page.getReference().setPath(OrchidUtils.normalizePath(resultingUrl));
        page.getReference().setFileName(title);

        page.getReference().setUsePrettyUrl(true);
    }

    private String getPermalinkTemplate(PermalinkPage post) {
        if (!EdenUtils.isEmpty(post.getPermalink())) {
            return post.getPermalink();
        }
        else {
            return postsModel.getPermalink();
        }
    }

    private String applyPermalinkTemplate(OrchidPage page, String[] pieces) {
        String resultingUrl = "";

        for (int i = 0; i < pieces.length - 1; i++) {
            resultingUrl += applyPermalinkTemplatePiece(page, pieces[i]);
        }

        return resultingUrl;
    }

    private String applyPermalinkTemplatePiece(OrchidPage page, String piece) {
        String resultingPiece = null;

        String pieceKey = null;

        if(!EdenUtils.isEmpty(piece) && piece.startsWith(":")) {
            pieceKey = piece.substring(1);
        }
        else if(!EdenUtils.isEmpty(piece) && piece.startsWith("{") && piece.endsWith("}")) {
            pieceKey = piece.substring(1, piece.length() - 1);
        }

        if(pieceKey != null) {
            for(PermalinkPathType pathType : pathTypes) {
                if(pathType.acceptsKey(page, pieceKey)) {
                    resultingPiece = pathType.format(page, pieceKey);
                }
            }

            if(resultingPiece == null) {
                throw new IllegalArgumentException("'" + piece + "' is not a valid permalink key");
            }
        }
        else {
            resultingPiece = piece;
        }

        return sanitizePathPiece(resultingPiece) + "/";
    }

    private String sanitizePathPiece(String pathPiece) {
        String s = pathPiece.replaceAll("\\s+", "-").toLowerCase();
        s = s.replaceAll("[^\\w-_]", "");
        return s;
    }

}
