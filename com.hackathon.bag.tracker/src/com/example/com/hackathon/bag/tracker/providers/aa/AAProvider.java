package com.example.com.hackathon.bag.tracker.providers.aa;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;

import com.example.com.hackathon.bag.tracker.helpers.SharedPreferencesHelper;
import com.example.com.hackathon.bag.tracker.providers.IAAProvider;
import com.example.com.hackathon.bag.tracker.providers.IRestProvider;

public class AAProvider implements IAAProvider {

	public final static int MESSAGE_LOGIN_SUCCESSFUL = 1;
	public final static int MESSAGE_LOGIN_FAILED = 2;
	public final static int MESSAGE_LOGIN_LOCKED = 3;
	
	private final static String BASE_URL = "https://aahackathon.api.layer7.com:9443/AA1";
	private final static String API_KEY = "l7xx73f3c637d4374b538ffaa0572d120f1a";
	
	private SharedPreferencesHelper mPreferences;
	private IRestProvider mRestProvider;
	
	public AAProvider(Context context, IRestProvider provider) {
		this.mPreferences = new SharedPreferencesHelper(context);
		this.mRestProvider = provider;
	}
	
	@Override
	public AAReservation getReservation(Handler handler, String firstName, String lastName, String reservationNumber) {
		Uri reservationUri = this.getReservationUri(firstName, lastName, reservationNumber);
		HttpResponse response = this.mRestProvider.getRequest(reservationUri);
		
		if(response != null && response.getStatusLine().getStatusCode() == 200) {
			HttpEntity entity = response.getEntity();
			
		} else {
			
		}
		return null;
	}
	
	@Override
	public void login(Handler handler, String aadvantageNumber, String password) {
		Uri loginUri = this.getLoginUri(aadvantageNumber, password);
		AALogin aaLogin = new AALogin(aadvantageNumber, password);
		
		HttpResponse response = this.mRestProvider.postRequest(loginUri, aaLogin);
		if(response != null && response.getStatusLine().getStatusCode() == 200) {
			Header[] header = response.getHeaders("Set-Cookie");
			if(header.length == 1) {
				String aaSecurityToken = header[0].getValue();
				this.mPreferences.setAASecurityToken(aaSecurityToken);
				
				Message message = Message.obtain(handler, MESSAGE_LOGIN_SUCCESSFUL);
				handler.sendMessage(message);
			}
		} else if(response != null && response.getStatusLine().getStatusCode() == 403) {
			Message message = Message.obtain(handler, MESSAGE_LOGIN_LOCKED);
			handler.sendMessage(message);
		} else {
			Message message = Message.obtain(handler, MESSAGE_LOGIN_FAILED);
			handler.sendMessage(message);
		}
	}
	
	private Uri getLoginUri(String aadvantageNumber, String password) {
		String uriString = String.format("%s/login?apikey=%s&aadvantageNumber=%s&password=%s", BASE_URL, API_KEY, aadvantageNumber, password);
		return Uri.parse(uriString);
	}
	
	private Uri getReservationUri(String firstName, String lastName, String reservationNumber) {
		String uriString = String.format("%s/reservation?apikey=%s&firstName=%s&lastName=%s&recordLocator=%s", BASE_URL, API_KEY, firstName, lastName, reservationNumber);
		return Uri.parse(uriString);
	}

}
