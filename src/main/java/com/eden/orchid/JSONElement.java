package com.eden.orchid;

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
        if(OrchidUtils.isJsonAware(object)) {
            this.element = object;
        }
        else {
            throw new IllegalArgumentException("A JSONElement must be an object that is json-aware (JSONObject, JSONArray, JSONElement, String, or primitive)");
        }
    }

    public Object getElement() {
        return element;
    }

    @Override
    public String toString() {
        return element.toString();
    }
}
