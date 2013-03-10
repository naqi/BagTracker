package com.example.com.hackathon.bag.tracker.server;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class SerializerCommon {

	public static <T> T readObjectFromStream(InputStream stream, Class<T> classType) 
			throws IOException, ClassNotFoundException, IllegalStateException {
    	InputStreamReader reader = new InputStreamReader(stream, Charset.forName("UTF-8"));
    	Gson gson = new Gson();
    	try {
    		return gson.fromJson(reader, classType);
    	} catch (JsonSyntaxException e) {
    		e.printStackTrace();
    	}
		return null;
	}

	public static String writeObjectToString(Object obj) {
    	Gson gson = new Gson();
    	return gson.toJson(obj);
	}
	
	public static <T> T readObjectFromString(String string, Class<T> classType) throws IOException, ClassNotFoundException {

    	Gson gson = new Gson();
    	return gson.fromJson(string, classType);

	}

}
