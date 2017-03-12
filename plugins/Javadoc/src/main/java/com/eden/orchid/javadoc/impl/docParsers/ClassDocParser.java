package com.eden.orchid.javadoc.impl.docParsers;

import com.eden.common.json.JSONElement;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.OrchidReference;
import com.eden.orchid.utilities.OrchidUtils;
import com.sun.javadoc.AnnotationDesc;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.ConstructorDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.ProgramElementDoc;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ClassDocParser {

    private OrchidContext context;

    private CommentParser commentParser;
    private TypeParser typeParser;
    private AnnotationParser annotationParser;
    private ParameterParser parameterParser;

    @Inject
    public ClassDocParser(OrchidContext context, CommentParser commentParser, AnnotationParser annotationParser, TypeParser typeParser, ParameterParser parameterParser) {
        this.context = context;
        this.commentParser = commentParser;
        this.typeParser = typeParser;
        this.annotationParser = annotationParser;
        this.parameterParser = parameterParser;
    }

    public JSONObject loadClassData(ClassDoc classDoc) {
        JSONObject object = new JSONObject();
        object.put("info", getClassInfo(classDoc));
        return object;
    }

    public OrchidReference getReference(ClassDoc classDoc) {
        OrchidReference ref = new OrchidReference(context, "javadoc", classDoc.qualifiedTypeName().replaceAll("\\.", File.separator) + ".html");
        ref.setUsePrettyUrl(true);
        return ref;
    }

    private JSONObject getClassInfo(ClassDoc classDoc) {
        JSONObject object = typeParser.getTypeObject(classDoc);

        object.put("modifiers", getModifiersInfo(classDoc));
        object.put("superclass", getSuperclassInfo(classDoc));
        object.put("interfaces", getInterfacesInfo(classDoc));
        object.put("annotations", getAnnotationsInfo(classDoc));
        object.put("comment", commentParser.getCommentObject(classDoc));

        return object;
    }

    private JSONObject getModifiersInfo(ClassDoc classDoc) {
        JSONObject object = new JSONObject();

        String visibility;
        if (classDoc.isPublic()) { visibility = "public"; }
        else if (classDoc.isPackagePrivate()) { visibility = "package-private"; }
        else if (classDoc.isProtected()) { visibility = "protected"; }
        else if (classDoc.isPrivate()) { visibility = "private"; }
        else { visibility = ""; }
        object.put("visibility", visibility);

        object.put("isFinal", classDoc.isFinal());
        object.put("isStatic", classDoc.isStatic());
        object.put("isAbstract", classDoc.isAbstract() && !classDoc.isAnnotationType());

        String objectType;
        String objectTypeName;
        if (classDoc.isAnnotationType()) {
            objectTypeName = "Annotation Type";
            objectType = "@interface";
        }
        else if (classDoc.isInterface()) {
            objectTypeName = "Interface";
            objectType = "interface";
        }
        else if (classDoc.isEnum()) {
            objectTypeName = "Enum";
            objectType = "enum";
        }
        else if (classDoc.isException()) {
            objectTypeName = "Exception";
            objectType = "class";
        }
        else if (classDoc.isError()) {
            objectTypeName = "Error Type";
            objectType = "class";
        }
        else if (classDoc.isOrdinaryClass() && classDoc.isAbstract()) {
            objectTypeName = "Abstract Class";
            objectType = "class";
        }
        else {
            objectTypeName = "Class";
            objectType = "class";
        }
        object.put("objectType", objectType);
        object.put("objectTypeName", objectTypeName);

        return object;
    }

    private JSONObject getSuperclassInfo(ClassDoc classDoc) {
        if (classDoc.superclass() != null) {
            return typeParser.getTypeObject(classDoc.superclass());
        }
        else {
            return null;
        }
    }

    private JSONArray getInterfacesInfo(ClassDoc classDoc) {
        if (!EdenUtils.isEmpty(classDoc.interfaces())) {
            JSONArray array = new JSONArray();

            for (ClassDoc i : classDoc.interfaces()) {
                array.put(typeParser.getTypeObject(i));
            }

            return array;
        }
        else {
            return null;
        }
    }

    private JSONArray getAnnotationsInfo(ProgramElementDoc classDoc) {
        if (!EdenUtils.isEmpty(classDoc.annotations())) {
            JSONArray array = new JSONArray();

            for (AnnotationDesc i : classDoc.annotations()) {
                if (!i.isSynthesized()) {
                    JSONObject object = typeParser.getTypeObject(i.annotationType());
                    object.put("values", new JSONObject());
                    for (AnnotationDesc.ElementValuePair pair : i.elementValues()) {
                        object.getJSONObject("values").put(pair.element().name(), pair.value().toString());
                    }
                    array.put(object);
                }
            }

            return array;
        }
        else {
            return null;
        }
    }

    public JSONArray getClassConstructors(ClassDoc classDoc) {
        JSONArray ctors = new JSONArray();

        for (ConstructorDoc cDoc : classDoc.constructors()) {
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

        for (MethodDoc mDoc : classDoc.methods()) {
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

        for (FieldDoc fDoc : classDoc.fields()) {
            JSONObject field = new JSONObject();
            field.put("comment", commentParser.getCommentObject(fDoc));
            field.put("annotations", annotationParser.getAnnotations(fDoc));
            field.put("name", fDoc.name());
            field.put("type", typeParser.getTypeObject(fDoc.type()));

            fields.put(field);
        }
        return fields;
    }

    public JSONArray getDirectSubclasses(ClassDoc classDoc) {

        Map<String, JSONObject> relatedClasses = new HashMap<>();
        JSONArray fields = new JSONArray();

        JSONArray element = OrchidUtils.queryIndex(context, "javadoc");

        for (int i = 0; i < element.length(); i++) {
            JSONObject object = element.getJSONObject(i);
            JSONElement objectElement = new JSONElement(object);

            if (objectElement.query("data.info") != null && objectElement.query("data.info.superclass") != null) {
                String objectQualifiedName = objectElement.query("data.info.qualifiedName").getElement().toString();
                String superclassQualifiedName = objectElement.query("data.info.superclass.qualifiedName").getElement().toString();

                if (superclassQualifiedName.equals(classDoc.qualifiedName()) && !relatedClasses.containsKey(objectQualifiedName)) {
                    relatedClasses.put(objectQualifiedName, object);
                    fields.put(object);
                }
            }
        }

        return fields;
    }
}
