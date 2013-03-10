package com.example.com.hackathon.bag.tracker.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import com.hackathon.bag.tracker.common.io.LoginUserRequest;
import com.hackathon.bag.tracker.common.io.LoginUserResponse;
import com.hackathon.bag.tracker.common.serializer.SerializerCommon;

public class BagTrackerServer {
	
	private final static String BASE_SERVER_PATH = "https://aahackathon.api.layer7.com:9443/AA1/";
	
	private static HttpResponse sendRequest(String servletName, Object obj) 
			throws Exception {
		HttpResponse response = null;
		String uriPath = BASE_SERVER_PATH + "/" + servletName.toString() + "?apikey=l7xx5cd5fb04544f49f6a3c40511bcd2cf9c";

		try {
			URI uri = new URI(uriPath);
			HttpPost postMethod = new HttpPost(uri);
						
			if (obj != null) {
				String jsonized = SerializerCommon.writeObjectToString(obj);
				StringEntity reqEntity = new StringEntity(jsonized);
				reqEntity.setContentEncoding("UTF-8");
				reqEntity.setContentType("application/json");
				postMethod.setEntity(reqEntity);
			}
			
			//Log.i(LOG_TAG, new StringBuffer("Executing request: ").append(uriPath).toString());
			response = ServerRequest.get().doPost(postMethod);

		} catch (URISyntaxException e) {
			e.printStackTrace();
			throw new Exception(e);
		} catch (IOException e){
			e.printStackTrace();
			throw new Exception(e);
		}

		return response;
	}
	
	public void loginUser(){
		LoginUserRequest request = new LoginUserRequest("597FXT0", "testing"); 
		try {
		sendRequest("login", request, LoginUserResponse.class );
		} catch(Exception ex){
			ex.printStackTrace();
		}
		
	}
	
	
	private static <T> T sendRequest(String servletName, Object requestData, Class<T> responseType) 
			throws Exception {
		try {
			HttpResponse response = sendRequest(servletName, requestData);
			if (response != null && !response.containsHeader("Error")) {
				return getObjectFromResponse(response, responseType);
			}
		} catch (Exception ex) {
			return null;
		}
		return null;
	}
	
	/**
	 * assumes the response is not <code>null</code>
	 */
	private static <T> T getObjectFromResponse(HttpResponse response, Class<T> classType) {
		InputStream contentStream;
		if (response == null || response.containsHeader("Error")){
			//Log.e(LOG_TAG, "response is null or contains error");
			return null;
		}
		else {
			try {
				HttpEntity entity = response.getEntity();
				if (entity == null) {
					return null;
				}
				contentStream = entity.getContent();
				if (contentStream == null) {
					//Log.e(LOG_TAG, "contentStream in null");
				}
				else {
					return SerializerCommon.readObjectFromStream(contentStream, classType);
				}
			}
			catch (IllegalStateException e) {
				//Log.e(LOG_TAG, e.getMessage(), e);
				e.printStackTrace();
			}
			catch (IOException e) {
				//Log.e(LOG_TAG, e.getMessage(), e);
				e.printStackTrace();
			}
			catch (ClassNotFoundException e) {
				//Log.e(LOG_TAG, e.getMessage(), e);
				e.printStackTrace();
			}
		}
		
		return null;
	}



}
