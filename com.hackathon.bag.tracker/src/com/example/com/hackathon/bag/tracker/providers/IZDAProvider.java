package com.example.com.hackathon.bag.tracker.providers;

import com.zoscomm.zda.client.api.ZDAEventListener.Stub;

public interface IZDAProvider {

	void authenticateUser(Stub listener);
	
	void initiateSecurityTokenRequest(Stub listener);
}
