package com.eden.orchid.posts;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.posts.pages.PostPage;
import com.eden.orchid.utilities.OrchidUtils;

import javax.inject.Inject;
import java.time.Month;

public class PostsPermalinkStrategy {

    private OrchidContext context;

    @Inject
    public PostsPermalinkStrategy(OrchidContext context) {
        this.context = context;
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
                return ":category/:year/:month/:title";
            }
            else {
                return ":year/:month/:title";
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
            switch (pieceKey) {
                case "title":
                    resultingPiece = post.getTitle();
                    break;
                case "year":
                    resultingPiece = "" + post.getYear();
                    break;
                case "month":
                    resultingPiece = "" + post.getMonth();
                    break;
                case "monthName":
                    resultingPiece = Month.of(post.getMonth()).toString();
                    break;
                case "day":
                    resultingPiece = "" + post.getDay();
                    break;
                default:
                    if(post.getData().has(pieceKey)) {
                        resultingPiece = post.getData().get(pieceKey).toString();
                    }
                    break;
            }

            if(resultingPiece == null) {
                throw new IllegalArgumentException("'" + piece + "' is not a valid permalink key");
            }
        }
        else {
            resultingPiece = piece;
        }

        return resultingPiece.replaceAll(" ", "-").toLowerCase() + "/";
    }
}
