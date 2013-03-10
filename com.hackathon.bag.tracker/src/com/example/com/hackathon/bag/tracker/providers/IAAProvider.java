package com.example.com.hackathon.bag.tracker.providers;

import android.os.Handler;

import com.example.com.hackathon.bag.tracker.providers.aa.AAReservation;

public interface IAAProvider {

	AAReservation getReservation(Handler handler, String firstName, String lastName, String reservationNumber);
	
	void login(Handler handler, String aadvantageNumber, String password);
}
