package com.example.com.hackathon.bag.tracker.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferencesHelper {

	private static final String KEY_PREFERENCES_FILE = "bag_tracker_prefs";
	
	private static final String KEY_AA_TOKEN = "AA_TOKEN";
	private static final String KEY_ZDA_TOKEN = "ZDA_TOKEN";
	private static final String KEY_ZDA_VERSION = "ZDA_VERSION";
	
	private SharedPreferences mPreferences;
	
	public SharedPreferencesHelper(Context context) {
		this.mPreferences = context.getSharedPreferences(KEY_PREFERENCES_FILE, Context.MODE_PRIVATE);
	}
	
	public String getAAToken() {
		return this.mPreferences.getString(KEY_AA_TOKEN, null);
	}
	
	public String getZDAToken() {
		return this.mPreferences.getString(KEY_ZDA_TOKEN, null);
	}
	
	public String getZDAVersion() {
		return this.mPreferences.getString(KEY_ZDA_VERSION, null);
	}
	
	public void setAASecurityToken(String token) {
		Editor editor = this.getEditor();
		editor.putString(KEY_AA_TOKEN, token);
		
		this.saveEditor(editor);
	}
	
	public void setZDAToken(String token) {
		Editor editor = this.getEditor();
		editor.putString(KEY_ZDA_TOKEN, token);
		
		this.saveEditor(editor);
	}
	
	public void setZDAVersion(String version) {
		Editor editor = this.getEditor();
		editor.putString(KEY_ZDA_VERSION, version);
		
		this.saveEditor(editor);
	}
	
	public boolean isLoginRequired() {
		return this.getAAToken() == null;
	}
	
	private Editor getEditor() {
		return this.mPreferences.edit();
	}
	
	private void saveEditor(Editor editor) {
		editor.apply();
	}
}
