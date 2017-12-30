package com.refresh.chotusalesv1.responseclasses;

/**
 * Created by Lenovo on 11/25/2017.
 */

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;


public class responseParser {

    @SerializedName("resStatus")
    public String resStatus;

    @SerializedName("resText")
    public String resText;
    public responseParser getResponse(String s) {
        //Response r = new Response();
        try {
            JSONObject reader = new JSONObject(s);

            resStatus = reader.getString("resStatus");
            resText = reader.getString("resText");
        } catch (Exception e) {

        }
        return this;
    }
//        JsonDeserializer<Response> jsonDeserializer = new JsonDeserializer<Response>() {
//            @Override
//            public Response deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
//                return null;
//            }
//        };

}


