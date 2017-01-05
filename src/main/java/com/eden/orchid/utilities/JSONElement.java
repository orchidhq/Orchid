package com.eden.orchid.utilities;

import org.json.JSONArray;
import org.json.JSONObject;

public class JSONElement {

    private Object element;

    public JSONElement(JSONObject object) { this.element = object; }
    public JSONElement(JSONArray object)  { this.element = object; }
    public JSONElement(String object)     { this.element = object; }
    public JSONElement(byte object)       { this.element = object; }
    public JSONElement(short object)      { this.element = object; }
    public JSONElement(int object)        { this.element = object; }
    public JSONElement(long object)       { this.element = object; }
    public JSONElement(float object)      { this.element = object; }
    public JSONElement(double object)     { this.element = object; }
    public JSONElement(boolean object)    { this.element = object; }

    public JSONElement(JSONElement object) {
        this(object.getElement());
    }

    public JSONElement(Object object) throws IllegalArgumentException {
        if(object == null) {
            throw new IllegalArgumentException("A JSONElement cannot be null");
        }
        else if(!OrchidUtils.isJsonAware(object)) {
            throw new IllegalArgumentException("A JSONElement must be an object that is json-aware (JSONObject, JSONArray, JSONElement, String, or primitive)");
        }
        else {
            this.element = object;
        }
    }

    public Object getElement() {
        return element;
    }

    @Override
    public String toString() {
        return element.toString();
    }

    /**
     * Query the gathered site data using a javascript-like syntax, or the native JSONObject query syntax. For example,
     * given a JSONObject initialized with this document:
     * <pre>
     * {
     *   "a": {
     *     "b": "c"
     *   }
     * }
     * </pre>
     * and this JSONPointer string:
     * <pre>
     * "/a/b"
     * </pre>
     * or this Javascript pointer string:
     * <pre>
     * "a.b"
     * </pre>
     * Then this method will return the String "c".
     * In the end, the Javascript syntax is converted to the corresponding JSONPointer syntax and queried.
     *
     * @param pointer  string that can be used to create a JSONPointer
     * @return  the item matched by the JSONPointer, otherwise null
     */
    public JSONElement query(String pointer) {
        if(!OrchidUtils.isEmpty(pointer)) {
            pointer = pointer.replaceAll("\\.", "/");

            if (!pointer.startsWith("/")) {
                pointer = "/" + pointer;
            }

            Object result = null;

            if(element instanceof JSONObject) {
                result = ((JSONObject) element).query(pointer);
            }
            else if(element instanceof JSONArray) {
                result = ((JSONArray) element).query(pointer);
            }

            if (result != null) {
                return new JSONElement(result);
            }
        }

        return null;
    }
}
