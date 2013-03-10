package com.example.com.hackathon.bag.tracker.providers.zda;

import android.content.Context;
import android.os.RemoteException;

import com.example.com.hackathon.bag.tracker.providers.IZDAProvider;
import com.zoscomm.zda.client.api.ZDAClassFactory;
import com.zoscomm.zda.client.api.ZDAEventListener.Stub;
import com.zoscomm.zda.client.api.ZDAEventService;

public class ZDAProvider implements IZDAProvider {

	private final static String API_KEY = "93f9f22c-7b08-11e2-916a-12313d23a402";
	private final static String API_SECRET = "00ad47f2";
	
	private ZDAEventService mZDAEventService;
	
	public ZDAProvider(Context context) {
		this.mZDAEventService = ZDAClassFactory.getEventService(context);
	}

	@Override
	public void initiateSecurityTokenRequest(Stub listener) {
		try {
			this.mZDAEventService.getVersion(listener);
		} catch (RemoteException exception) {
			this.handleRemoteException(exception);
		}
	}

	@Override
	public void authenticateUser(Stub listener) {
		try {
			this.mZDAEventService.authenticateUser(API_KEY, API_SECRET, listener);
		} catch (RemoteException exception) {
			this.handleRemoteException(exception);
		}
	}
	
	private void handleRemoteException(RemoteException exception) {
		exception.printStackTrace();
	}
}
