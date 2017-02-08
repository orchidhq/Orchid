package com.eden.orchid.impl.docParser.docs;

import com.eden.common.json.JSONElement;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.Orchid;
import com.eden.orchid.api.docParser.OrchidBlockTagHandler;
import com.eden.orchid.api.docParser.OrchidInlineTagHandler;
import com.eden.orchid.api.registration.Contextual;
import com.sun.javadoc.Doc;
import com.sun.javadoc.Tag;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

public class CommentParser implements Contextual {

    public JSONObject getCommentObject(Doc doc) {
        JSONObject comment = new JSONObject();

        String firstSentence = "";

        for (Tag tag : doc.firstSentenceTags()) {
            firstSentence += tag.text();
        }

        if (!EdenUtils.isEmpty(firstSentence)) {
            comment.put("shortDescription", firstSentence);
        }
        if (!EdenUtils.isEmpty(doc.commentText())) {
            String content = doc.commentText();

            if (Orchid.getContext().query("options.commentExt") != null) {
                content = Orchid.getContext().getTheme().compile(Orchid.getContext().query("options.commentExt").toString(), content);
            }

            comment.put("description", content);
        }

        comment.put("inlineTags", getInlineTags(doc));
        comment.put("blockTags", getBlockTags(doc));

        return (comment.length() > 0) ? comment : null;
    }

    private JSONArray getInlineTags(Doc doc) {
        JSONArray array = new JSONArray();

        Tag[] tags = doc.inlineTags();

        if (!EdenUtils.isEmpty(tags)) {

            for (Tag tag : tags) {
                OrchidInlineTagHandler handler = null;

                for (Map.Entry<Integer, OrchidInlineTagHandler> tagHandlerEntry : getRegistrar().resolveMap(OrchidInlineTagHandler.class).entrySet()) {
                    OrchidInlineTagHandler tagHandler = tagHandlerEntry.getValue();
                    if (("@" + tagHandler.getName()).equalsIgnoreCase(tag.kind())) {
                        handler = tagHandler;
                        break;
                    }
                }

                JSONObject result = new JSONObject();

                if (handler != null) {
                    result.put("kind", handler.getName());
                    result.put("value", handler.processTag(tag).getElement());
                }
                else {
                    result.put("kind", tag.kind().replaceAll("@", "").toLowerCase());
                    result.put("value", tag.text());
                }

                array.put(result);
            }
        }

        return (array.length() > 0) ? array : null;
    }

    private JSONObject getBlockTags(Doc doc) {
        JSONObject object = new JSONObject();

        for (Map.Entry<Integer, OrchidBlockTagHandler> tagHandlerEntry : getRegistrar().resolveMap(OrchidBlockTagHandler.class).entrySet()) {
            Tag[] tags = doc.tags(tagHandlerEntry.getValue().getName());

            if (!EdenUtils.isEmpty(tags)) {
                JSONElement el = tagHandlerEntry.getValue().processTags(tags);

                if (el != null) {
                    object.put(tagHandlerEntry.getValue().getName(), el.getElement());
                }
            }
        }

        return (object.length() > 0) ? object : null;
    }
}
