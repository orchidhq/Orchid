package com.eden.orchid.posts.permalink;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.posts.pages.PostPage;
import com.eden.orchid.utilities.ObservableTreeSet;
import com.eden.orchid.utilities.OrchidUtils;

import javax.inject.Inject;
import java.util.Set;

public class PostsPermalinkStrategy {

    private OrchidContext context;
    private Set<PermalinkPathType> pathTypes;

    @Inject
    public PostsPermalinkStrategy(OrchidContext context, Set<PermalinkPathType> pathTypes) {
        this.context = context;
        this.pathTypes = new ObservableTreeSet<>(pathTypes);
    }

    public void applyPermalink(PostPage post) {
        String permalink = getPermalinkTemplate(post);

        String[] pieces = permalink.split("/");

        String resultingUrl = applyPermalinkTemplate(permalink, post);
        String title = applyPermalinkTemplatePiece(pieces[pieces.length - 1], post);

        post.getReference().setPath(OrchidUtils.normalizePath(resultingUrl));
        post.getReference().setFileName(title);

        post.getReference().setUsePrettyUrl(true);
    }

    private String getPermalinkTemplate(PostPage post) {

        String permalink = null;

        if (post.getData().has("permalink")) {
            permalink = post.getData().getString("permalink");
        }
        else if (!EdenUtils.isEmpty(context.query("options.posts.permalink"))) {
            permalink = context.query("options.posts.permalink").toString();
        }

        permalink = OrchidUtils.normalizePath(permalink);

        if(!EdenUtils.isEmpty(permalink)) {
            return permalink;
        }
        else {
            if(post.getData().has("category")) {
                return ":category/:year/:month/:slug";
            }
            else {
                return ":year/:month/:slug";
            }
        }
    }

    private String applyPermalinkTemplate(String permalink, PostPage post) {
        String[] pieces = permalink.split("/");

        String resultingUrl = "";

        for (int i = 0; i < pieces.length - 1; i++) {
            resultingUrl += applyPermalinkTemplatePiece(pieces[i], post);
        }

        return resultingUrl;
    }

    private String applyPermalinkTemplatePiece(String piece, PostPage post) {
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
                if(pathType.acceptsKey(post, pieceKey)) {
                    resultingPiece = pathType.format(post, pieceKey);
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
