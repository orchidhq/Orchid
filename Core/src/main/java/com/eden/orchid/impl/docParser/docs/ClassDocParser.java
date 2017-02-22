package com.eden.orchid.impl.docParser.docs;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.OrchidReference;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.ConstructorDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.MethodDoc;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;
import java.io.File;

public class ClassDocParser {

    private OrchidContext context;

    private CommentParser commentParser;
    private AnnotationParser annotationParser;
    private TypeParser typeParser;
    private ParameterParser parameterParser;

    @Inject
    public ClassDocParser(OrchidContext context, CommentParser commentParser, AnnotationParser annotationParser, TypeParser typeParser, ParameterParser parameterParser) {
        this.context = context;
        this.commentParser = commentParser;
        this.annotationParser = annotationParser;
        this.typeParser = typeParser;
        this.parameterParser = parameterParser;
    }

    public JSONObject loadClassData(ClassDoc classDoc) {
        JSONObject object = getBasicClassObject(classDoc);

        object.put("info", getClassInfo(classDoc));
        object.put("constructors", getClassConstructors(classDoc));
        object.put("methods", getClassMethods(classDoc));
        object.put("fields", getClassFields(classDoc));

        return object;
    }

    public JSONObject getBasicClassObject(ClassDoc classDoc) {
        JSONObject object = new JSONObject();

        return object;
    }

    public OrchidReference getReference(ClassDoc classDoc) {
        OrchidReference ref = new OrchidReference(context, "classes", classDoc.qualifiedTypeName().replaceAll("\\.", File.separator) + ".html");
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
    public JSONObject getClassInfo(ClassDoc classDoc) {
        JSONObject object = new JSONObject();
        object.put("name", classDoc.simpleTypeName());
        object.put("package", classDoc.containingPackage().name());
        object.put("qualifiedName", classDoc.qualifiedTypeName());
        object.put("url", getReference(classDoc).toString());
        object.put("comment", commentParser.getCommentObject(classDoc));

        return object;
    }

    public JSONArray getClassConstructors(ClassDoc classDoc) {
        JSONArray ctors = new JSONArray();

        for(ConstructorDoc cDoc : classDoc.constructors()) {
            JSONObject ctor = new JSONObject();
            ctor.put("comment", commentParser.getCommentObject(cDoc));
            ctor.put("annotations", annotationParser.getAnnotations(cDoc));
            ctor.put("parameters", parameterParser.getParameters(cDoc));

            ctors.put(ctor);
        }

        return ctors;
    }

    public JSONArray getClassMethods(ClassDoc classDoc) {
        JSONArray methods = new JSONArray();

        for(MethodDoc mDoc : classDoc.methods()) {
            JSONObject method = new JSONObject();
            method.put("comment", commentParser.getCommentObject(mDoc));
            method.put("annotations", annotationParser.getAnnotations(mDoc));
            method.put("returns", typeParser.getTypeObject(mDoc.returnType()));
            method.put("parameters", parameterParser.getParameters(mDoc));
            method.put("name", mDoc.name());

            methods.put(method);
        }
        return methods;
    }

    public JSONArray getClassFields(ClassDoc classDoc) {
        JSONArray fields = new JSONArray();

        for(FieldDoc fDoc : classDoc.fields()) {
            JSONObject field = new JSONObject();
            field.put("comment", commentParser.getCommentObject(fDoc));
            field.put("annotations", annotationParser.getAnnotations(fDoc));
            field.put("name", fDoc.name());
            field.put("type", typeParser.getTypeObject(fDoc.type()));

            fields.put(field);
        }
        return fields;
    }
}
