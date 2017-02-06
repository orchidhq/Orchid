package com.eden.orchid.impl.docParser.docs;

import com.eden.common.json.JSONElement;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.Orchid;
import com.eden.orchid.docParser.BlockTagHandler;
import com.eden.orchid.docParser.InlineTagHandler;
import com.eden.orchid.docParser.TagHandlers;
import com.sun.javadoc.Doc;
import com.sun.javadoc.Tag;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class CommentParser {
    public static Set<String> tags;

    static {
        tags = new TreeSet<>();

        tags.add("author");
        tags.add("version");
        tags.add("since");
//        tags.add("see");
//        tags.add("param");
        tags.add("return");
        tags.add("exception");
        tags.add("throws");
        tags.add("deprecated");
    }

    public static JSONObject getCommentObject(Doc doc) {
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

            if (Orchid.query("options.commentExt") != null) {
                content = Orchid.getTheme().compile(Orchid.query("options.commentExt").toString(), content);
            }

            comment.put("description", content);
        }

        comment.put("inlineTags", getInlineTags(doc));
        comment.put("blockTags", getBlockTags(doc));

        return (comment.length() > 0) ? comment : null;
    }

    private static JSONArray getInlineTags(Doc doc) {
        JSONArray array = new JSONArray();

        Tag[] tags = doc.inlineTags();

        if (!EdenUtils.isEmpty(tags)) {

            for (Tag tag : tags) {
                InlineTagHandler handler = null;

                for (Map.Entry<Integer, InlineTagHandler> tagHandlerEntry : TagHandlers.inlineTagHandlers.entrySet()) {
                    InlineTagHandler tagHandler = tagHandlerEntry.getValue();
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

    private static JSONObject getBlockTags(Doc doc) {
        JSONObject object = new JSONObject();

        for (Map.Entry<Integer, BlockTagHandler> tagHandlerEntry : TagHandlers.blockTagHandlers.entrySet()) {
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
