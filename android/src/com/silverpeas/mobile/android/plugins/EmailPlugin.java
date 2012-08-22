package com.silverpeas.mobile.android.plugins;

import org.apache.cordova.api.Plugin;
import org.apache.cordova.api.PluginResult;
import org.apache.cordova.api.PluginResult.Status;
import org.json.JSONArray;

import android.content.Intent;

public class EmailPlugin extends Plugin{

	public static final String ACTION = "email";
	private int code = 0;
	
	@Override
	public PluginResult execute(String arg0, JSONArray arg1, String arg2) {
		PluginResult result = null;
		
		if (ACTION.equals(arg0)) {
			try {
				String email = arg1.getString(0);
				LoadEmail(email);
				return new PluginResult(Status.OK);
			} catch (Exception Ex) {
				result = new PluginResult(Status.ERROR);
			}
		} else {
			result = new PluginResult(Status.INVALID_ACTION);
		}
		return result;
	}
	
	public void LoadEmail(String email){
		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("message/rfc822"); 
		i.putExtra(Intent.EXTRA_EMAIL, email);
		Intent.createChooser(i, "Choisissez une application.");
		this.cordova.startActivityForResult((Plugin) this, i, code);
	}

}
