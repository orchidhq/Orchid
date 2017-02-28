package com.eden.orchid.posts;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.utilities.OrchidUtils;
import org.json.JSONObject;

import javax.inject.Inject;

public class PostsPermalinkStrategy {

    private OrchidContext context;

    @Inject
    public PostsPermalinkStrategy(OrchidContext context) {
        this.context = context;
    }

    public String getPermalinkTemplate(JSONObject pageData) {

        String permalink = null;

        if (pageData.has("permalink")) {
            permalink = pageData.getString("permalink");
        }
        else if (!EdenUtils.isEmpty(context.query("options.posts.permalink"))) {
            permalink = context.query("options.posts.permalink").toString();
        }
        else if (!EdenUtils.isEmpty(context.query("options.permalink"))) {
            permalink = context.query("options.permalink").toString();
        }

        permalink = OrchidUtils.stripSeparators(permalink);

        if(!EdenUtils.isEmpty(permalink)) {
            return permalink;
        }
        else {
            return ":year/:month/:day/:title";
        }
    }

    public String applyPermalinkTemplate(String permalink, JSONObject pageData) {
        String[] pieces = permalink.split("/");

        String resultingUrl = "";

        for (int i = 0; i < pieces.length - 1; i++) {
            resultingUrl += applyPermalinkTemplatePiece(pieces[i], pageData);
        }

        return resultingUrl;
    }

    public String applyPermalinkTemplatePiece(String piece, JSONObject pageData) {
        String resultingPiece = "";

        if(!EdenUtils.isEmpty(piece) && piece.startsWith(":")) {
            if(pageData.has(piece.substring(1))) {
                resultingPiece = pageData.get(piece.substring(1)).toString();
            }
            else {
                throw new IllegalArgumentException("'" + piece + "' is not a valid permalink key");
            }
        }
        else {
            resultingPiece = piece;
        }

        return resultingPiece.replaceAll(" ", "-").toLowerCase() + "/";
    }
}
