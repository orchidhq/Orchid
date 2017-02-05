package com.eden.orchid.docParser.parsers;

import com.eden.common.json.JSONElement;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.Orchid;
import com.eden.orchid.docParser.TagHandler;
import com.eden.orchid.docParser.TagHandlers;
import com.sun.javadoc.Doc;
import com.sun.javadoc.Tag;
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

        if(!EdenUtils.isEmpty(firstSentence)) {
            comment.put("shortDescription", firstSentence);
        }
        if(!EdenUtils.isEmpty(doc.commentText())) {
            String content = doc.commentText();

            if(Orchid.query("options.commentExt") != null) {
                content = Orchid.getTheme().compile(Orchid.query("options.commentExt").toString(), content);
            }

            comment.put("description", content);
        }

        applyTags(doc, comment);

        return (comment.length() > 0) ? comment : null;
    }

    private static void applyTags(Doc doc, JSONObject object) {
        for (Map.Entry<Integer, TagHandler> tagHandlerEntry : TagHandlers.tagHandlers.entrySet()) {
            Tag[] tags = doc.tags(tagHandlerEntry.getValue().getName());

            if(tags != null && tags.length > 0) {
                JSONElement el = tagHandlerEntry.getValue().processTag(tags);

                if (el != null) {
                    object.put(tagHandlerEntry.getValue().getName(), el.getElement());
                }
            }
        }
    }
}
