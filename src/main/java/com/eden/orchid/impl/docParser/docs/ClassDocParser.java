package com.eden.orchid.impl.docParser.docs;

import com.eden.orchid.resources.OrchidReference;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.ConstructorDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.MethodDoc;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;

public class ClassDocParser {

    public static JSONObject loadClassData(ClassDoc classDoc) {
        JSONObject object = ClassDocParser.getBasicClassObject(classDoc);

        object.put("info", ClassDocParser.getClassInfo(classDoc));
        object.put("constructors", ClassDocParser.getClassConstructors(classDoc));
        object.put("methods", ClassDocParser.getClassMethods(classDoc));
        object.put("fields", ClassDocParser.getClassFields(classDoc));

        return object;
    }

    public static JSONObject getBasicClassObject(ClassDoc classDoc) {
        JSONObject object = new JSONObject();

        return object;
    }

    public static OrchidReference getReference(ClassDoc classDoc) {
        OrchidReference ref = new OrchidReference("classes", classDoc.qualifiedTypeName().replaceAll("\\.", File.separator) + ".html");
        ref.setUsePrettyUrl(true);

        return ref;
    }

    /**
     * Gets the basic information describing the class as a whole: its package, name, type parameters, superclass,
     * implemented interfaces, annotations, and comments.
     *
     * @param classDoc the classdoc to extract information for
     * @return the collected info
     */
    public static JSONObject getClassInfo(ClassDoc classDoc) {
        JSONObject object = new JSONObject();
        object.put("name", classDoc.simpleTypeName());
        object.put("package", classDoc.containingPackage().name());
        object.put("qualifiedName", classDoc.qualifiedTypeName());
        object.put("url", getReference(classDoc).toString());
        object.put("comment", CommentParser.getCommentObject(classDoc));

        return object;
    }

    public static JSONArray getClassConstructors(ClassDoc classDoc) {
        JSONArray ctors = new JSONArray();

        for(ConstructorDoc cDoc : classDoc.constructors()) {
            JSONObject ctor = new JSONObject();
            ctor.put("comment", CommentParser.getCommentObject(cDoc));
            ctor.put("annotations", AnnotationParser.getAnnotations(cDoc));
            ctor.put("parameters", ParameterParser.getParameters(cDoc));

            ctors.put(ctor);
        }

        return ctors;
    }

    public static JSONArray getClassMethods(ClassDoc classDoc) {
        JSONArray methods = new JSONArray();

        for(MethodDoc mDoc : classDoc.methods()) {
            JSONObject method = new JSONObject();
            method.put("comment", CommentParser.getCommentObject(mDoc));
            method.put("annotations", AnnotationParser.getAnnotations(mDoc));
            method.put("returns", TypeParser.getTypeObject(mDoc.returnType()));
            method.put("parameters", ParameterParser.getParameters(mDoc));
            method.put("name", mDoc.name());

            methods.put(method);
        }
        return methods;
    }

    public static JSONArray getClassFields(ClassDoc classDoc) {
        JSONArray fields = new JSONArray();

        for(FieldDoc fDoc : classDoc.fields()) {
            JSONObject field = new JSONObject();
            field.put("comment", CommentParser.getCommentObject(fDoc));
            field.put("annotations", AnnotationParser.getAnnotations(fDoc));
            field.put("name", fDoc.name());
            field.put("type", TypeParser.getTypeObject(fDoc.type()));


            fields.put(field);
        }
        return fields;
    }
}
