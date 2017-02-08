package com.eden.orchid.impl.docParser.docs;

import com.eden.orchid.api.registration.Contextual;
import com.eden.orchid.api.resources.OrchidReference;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.ConstructorDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.MethodDoc;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;

public class ClassDocParser implements Contextual {

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
    public JSONObject getClassInfo(ClassDoc classDoc) {
        JSONObject object = new JSONObject();
        object.put("name", classDoc.simpleTypeName());
        object.put("package", classDoc.containingPackage().name());
        object.put("qualifiedName", classDoc.qualifiedTypeName());
        object.put("url", getReference(classDoc).toString());
        object.put("comment", getContext().getCommentParser().getCommentObject(classDoc));

        return object;
    }

    public JSONArray getClassConstructors(ClassDoc classDoc) {
        JSONArray ctors = new JSONArray();

        for(ConstructorDoc cDoc : classDoc.constructors()) {
            JSONObject ctor = new JSONObject();
            ctor.put("comment", getContext().getCommentParser().getCommentObject(cDoc));
            ctor.put("annotations", getContext().getAnnotationParser().getAnnotations(cDoc));
            ctor.put("parameters", getContext().getParameterParser().getParameters(cDoc));

            ctors.put(ctor);
        }

        return ctors;
    }

    public JSONArray getClassMethods(ClassDoc classDoc) {
        JSONArray methods = new JSONArray();

        for(MethodDoc mDoc : classDoc.methods()) {
            JSONObject method = new JSONObject();
            method.put("comment", getContext().getCommentParser().getCommentObject(mDoc));
            method.put("annotations", getContext().getAnnotationParser().getAnnotations(mDoc));
            method.put("returns", getContext().getTypeParser().getTypeObject(mDoc.returnType()));
            method.put("parameters", getContext().getParameterParser().getParameters(mDoc));
            method.put("name", mDoc.name());

            methods.put(method);
        }
        return methods;
    }

    public JSONArray getClassFields(ClassDoc classDoc) {
        JSONArray fields = new JSONArray();

        for(FieldDoc fDoc : classDoc.fields()) {
            JSONObject field = new JSONObject();
            field.put("comment", getContext().getCommentParser().getCommentObject(fDoc));
            field.put("annotations", getContext().getAnnotationParser().getAnnotations(fDoc));
            field.put("name", fDoc.name());
            field.put("type", getContext().getTypeParser().getTypeObject(fDoc.type()));

            fields.put(field);
        }
        return fields;
    }
}
