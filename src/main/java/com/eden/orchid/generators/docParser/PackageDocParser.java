package com.eden.orchid.generators.docParser;

import com.sun.javadoc.AnnotationTypeDoc;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.PackageDoc;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;

public class PackageDocParser {
    public static JSONObject createPackageDocJson(PackageDoc packageDoc) {
        JSONObject packageInfoJson = new JSONObject();

        packageInfoJson.put("name", packageDoc.name());
        packageInfoJson.put("url", "/classes/" + packageDoc.name().replaceAll("\\.", File.separator));

        if(packageDoc.interfaces().length > 0) {
            packageInfoJson.put("interfaces", getInterfaces(packageDoc));
        }
        if(packageDoc.ordinaryClasses().length > 0) {
            packageInfoJson.put("classes", getClasses(packageDoc));
        }
        if(packageDoc.annotationTypes().length > 0) {
            packageInfoJson.put("annotations", getAnnotations(packageDoc));
        }
        if(packageDoc.enums().length > 0) {
            packageInfoJson.put("enums", getEnums(packageDoc));
        }

        return packageInfoJson;
    }

    public static JSONArray getClasses(PackageDoc packageDoc) {
        JSONArray childClasses = new JSONArray();

        for(ClassDoc childClass : packageDoc.ordinaryClasses()) {
            JSONObject childClassJson = ParserUtils.getTypeObject(childClass);

            if(childClass.commentText().length() > 0) {
                childClassJson.put("comment", ParserUtils.getCommentDescription(childClass));
            }

            childClasses.put(childClassJson);
        }

        return childClasses;
    }

    public static JSONArray getAnnotations(PackageDoc packageDoc) {
        JSONArray childClasses = new JSONArray();

        for(AnnotationTypeDoc childClass : packageDoc.annotationTypes()) {
            JSONObject childClassJson = ParserUtils.getTypeObject(childClass);

            if(childClass.commentText().length() > 0) {
                childClassJson.put("comment", ParserUtils.getCommentDescription(childClass));
            }

            childClasses.put(childClassJson);
        }

        return childClasses;
    }

    public static JSONArray getEnums(PackageDoc packageDoc) {
        JSONArray childClasses = new JSONArray();

        for(ClassDoc childClass : packageDoc.enums()) {
            JSONObject childClassJson = ParserUtils.getTypeObject(childClass);

            if(childClass.commentText().length() > 0) {
                childClassJson.put("comment", ParserUtils.getCommentDescription(childClass));
            }

            childClasses.put(childClassJson);
        }

        return childClasses;
    }

    public static JSONArray getInterfaces(PackageDoc packageDoc) {
        JSONArray childClasses = new JSONArray();

        for(ClassDoc childClass : packageDoc.interfaces()) {
            JSONObject childClassJson = ParserUtils.getTypeObject(childClass);

            if(childClass.commentText().length() > 0) {
                childClassJson.put("comment", ParserUtils.getCommentDescription(childClass));
            }

            childClasses.put(childClassJson);
        }

        return childClasses;
    }

    public static JSONObject getPackageHeadInfo(PackageDoc packageDoc) {
        JSONObject head = new JSONObject();

        head.put("title", packageDoc.name());

        return head;
    }
}
