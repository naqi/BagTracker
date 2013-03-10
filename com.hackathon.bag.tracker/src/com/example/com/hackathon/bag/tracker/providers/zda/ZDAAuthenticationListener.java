package com.example.com.hackathon.bag.tracker.providers.zda;

import android.content.Context;
import android.os.RemoteException;

import com.example.com.hackathon.bag.tracker.helpers.SharedPreferencesHelper;
import com.example.com.hackathon.bag.tracker.providers.IZDAProvider;
import com.zoscomm.zda.client.api.GeoLocation;
import com.zoscomm.zda.client.api.ZDAEventListener;

public class ZDAAuthenticationListener extends ZDAEventListener.Stub {

	private IZDAProvider mProvider;
	private SharedPreferencesHelper mPreferences;
	
	public ZDAAuthenticationListener(Context context, IZDAProvider provider) {
		this.mProvider = provider;
		this.mPreferences = new SharedPreferencesHelper(context);
	}
	
	@Override
	public void onAuthenticationComplete(String token) throws RemoteException {
		if(token != null) {
			this.mPreferences.setZDAToken(token);
		}
	}

	@Override
	public void onComplete(int action, String param) throws RemoteException {}

	@Override
	public void onError(int code, String reason) throws RemoteException {
		reason.getClass();
	}

	@Override
	public void onLocationShotResponse(GeoLocation location) throws RemoteException {}

	@Override
	public void onLocationUpdate(GeoLocation location) throws RemoteException {}

	@Override
	public void onMessageNotify(int count) throws RemoteException {}

	@Override
	public void onVersionResponse(String version) throws RemoteException {
		this.mPreferences.setZDAVersion(version);
		this.mProvider.authenticateUser(this);
	}

	@Override
	public void onXmlResponse(int id, String xml) throws RemoteException {}
	
}
