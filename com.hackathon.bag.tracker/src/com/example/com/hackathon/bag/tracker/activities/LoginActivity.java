package com.example.com.hackathon.bag.tracker.activities;

import com.example.com.hackathon.bag.tracker.R;
import com.example.com.hackathon.bag.tracker.helpers.SharedPreferencesHelper;
import com.example.com.hackathon.bag.tracker.providers.IAAProvider;
import com.example.com.hackathon.bag.tracker.providers.IRestProvider;
import com.example.com.hackathon.bag.tracker.providers.aa.AAProvider;
import com.example.com.hackathon.bag.tracker.providers.rest.RestProvider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private SharedPreferencesHelper mPreferences;
	private IAAProvider mProvider;
	private Context mContext;
	
	private EditText mAAdvantageNumber;
	private EditText mPassword;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.mContext = this;
		this.mPreferences = new SharedPreferencesHelper(this);
		
		if(!this.mPreferences.isLoginRequired()) {
			this.launchRecordLocaterActivity();
			
			finish();
		} else {
			this.setContentView(R.layout.activity_login);
			
			IRestProvider restProvider = new RestProvider();
			this.mProvider = new AAProvider(this, restProvider);
			
			this.mAAdvantageNumber = (EditText) this.findViewById(R.id.edit_aadvantage_number);
			this.mPassword = (EditText) this.findViewById(R.id.edit_password);
			
			Button buttonSubmit = (Button) this.findViewById(R.id.submit_login);
			buttonSubmit.setOnClickListener(buttonSubmitClickListener);
		}
	}
	
	private OnClickListener buttonSubmitClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			final String aadvantageNumber = mAAdvantageNumber.getText().toString();
			final String password = mAAdvantageNumber.getText().toString();
			
			new Thread(new Runnable() {

				@Override
				public void run() {
					mProvider.login(aaLoginHandler, aadvantageNumber, password);
				}
				
			}).start();
		}
		
	};
	
	private Handler aaLoginHandler = new Handler() {
		
		@Override
		public void handleMessage(Message message) {
			switch(message.what) {
			case AAProvider.MESSAGE_LOGIN_SUCCESSFUL:
				if(shouldLaunchRecordLocaterActivity()) {
					launchRecordLocaterActivity();
				}
				
				finish();
				break;
				
			case AAProvider.MESSAGE_LOGIN_FAILED:
				String failedMessage = getString(R.string.toast_login_failed);
				Toast.makeText(mContext, failedMessage, Toast.LENGTH_SHORT).show();
				
				break;
			
			case AAProvider.MESSAGE_LOGIN_LOCKED:
				String lockedMessage = getString(R.string.toast_login_locked);
				Toast.makeText(mContext, lockedMessage, Toast.LENGTH_SHORT).show();
				
				break;
			}
		}
		
	};
	
	private void launchRecordLocaterActivity() {
		Intent recordLocaterIntent = new Intent(mContext, RecordLocaterActivity.class);
		startActivity(recordLocaterIntent);
	}
	
	private boolean shouldLaunchRecordLocaterActivity() {
		return true;
	}
}
