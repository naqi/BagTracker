package com.example.com.hackathon.bag.tracker.providers;

import org.apache.http.HttpResponse;

import android.net.Uri;

public interface IRestProvider {

	HttpResponse getRequest(Uri uri);
	
	HttpResponse postRequest(Uri uri);
	
	<BodyType> HttpResponse postRequest(Uri uri, BodyType body);
}
