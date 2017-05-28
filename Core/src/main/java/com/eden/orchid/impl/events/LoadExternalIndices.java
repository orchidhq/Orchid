package com.eden.orchid.impl.events;

public class LoadExternalIndices {

//    private FileLoader fileLoader;
//
//    @Inject
//    public LoadExternalIndices(FileLoader fileLoader) {
//        this.fileLoader = fileLoader;
//    }
//
//    @On(Orchid.Events.DATAFILES_PARSED)
//    public void onDatafilesParsed(JSONObject options) {
//        if(options != null) {
//            JSONElement externalIndexList = new JSONElement(options).query("data.externalIndex");
//
//            if(OrchidUtils.elementIsObject(externalIndexList)) {
//                JSONObject externalIndex = (JSONObject) externalIndexList.getElement();
//
//                if(externalIndex.has("links") && externalIndex.get("links") instanceof JSONArray) {
//                    loadIndices(options, externalIndex.getJSONArray("links"));
//                }
//            }
//            else if(OrchidUtils.elementIsArray(externalIndexList)) {
//                JSONArray externalIndex = (JSONArray) externalIndexList.getElement();
//
//                loadIndices(options, externalIndex);
//            }
//        }
//    }

//    private void loadIndices(JSONObject options, JSONArray refersTo) {
//        JSONArray fullIndex = new JSONArray();
//        JSONObject keyedIndex = new JSONObject();
//
//        for (int i = 0; i < refersTo.length(); i++) {
//            JSONObject index = this.fileLoader.loadAdditionalFile(refersTo.optString(i));
//
//            if(index != null) {
//                fullIndex.put(index);
//
//                for(String key : index.keySet()) {
//                    if(!keyedIndex.has(key)) {
//                        keyedIndex.put(key, new JSONArray());
//                    }
//
//                    if(index.get(key) instanceof JSONArray) {
//                        JSONArray array = index.getJSONArray(key);
//
//                        for (int j = 0; j < array.length(); j++) {
//                            keyedIndex.getJSONArray(key).put(array.get(j));
//                        }
//                    }
//                }
//            }
//        }
//
//        options.put("externalIndex", new JSONObject());
//        options.getJSONObject("externalIndex").put("fullIndex", fullIndex);
//        options.getJSONObject("externalIndex").put("keyedIndex", keyedIndex);
//    }
}
