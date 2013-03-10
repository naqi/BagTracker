package com.example.com.hackathon.bag.tracker.activities;

import com.example.com.hackathon.bag.tracker.R;
import com.example.com.hackathon.bag.tracker.providers.IAAProvider;
import com.example.com.hackathon.bag.tracker.providers.IRestProvider;
import com.example.com.hackathon.bag.tracker.providers.aa.AAProvider;
import com.example.com.hackathon.bag.tracker.providers.rest.RestProvider;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class RecordLocaterActivity extends Activity {

	private IAAProvider mAAProvider;
	
	private EditText mEditFirstName;
	private EditText mEditLastName;
	private EditText mEditRecord;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_record_locater);
		
		IRestProvider restProvider = new RestProvider();
		this.mAAProvider = new AAProvider(this, restProvider);
		
		Button submitButton = (Button) this.findViewById(R.id.submit_record);
		submitButton.setOnClickListener(submitClickListener);
		
		this.mEditFirstName = (EditText) this.findViewById(R.id.edit_first_name);
		this.mEditLastName = (EditText) this.findViewById(R.id.edit_last_name);
		this.mEditRecord = (EditText) this.findViewById(R.id.edit_record);
	}
	
	private OnClickListener submitClickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			final String firstName = mEditFirstName.getText().toString();
			final String lastName = mEditLastName.getText().toString();
			final String record = mEditRecord.getText().toString();
			
			new Thread(new Runnable() {

				@Override
				public void run() {
					mAAProvider.getReservation(mAAReservationHandler, firstName, lastName, record);
				}
				
			}).start();
		}
		
	};
	
	private Handler mAAReservationHandler = new Handler() {
		
		@Override
		public void handleMessage(Message message) {
			switch(message.what) {
			
			
			}
		}
		
	};
}
