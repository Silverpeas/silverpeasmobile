package com.silverpeas.mobile.android.plugins;

import org.apache.cordova.api.Plugin;
import org.apache.cordova.api.PluginResult;
import org.apache.cordova.api.PluginResult.Status;
import org.json.JSONArray;

import android.content.Intent;
import android.net.Uri;

public class CallContactPlugin extends Plugin {

	public static final String ACTION = "phone";
	private int code = 0;

	@Override
	public PluginResult execute(String arg0, JSONArray arg1, String arg2) {
		PluginResult result = null;

		if (ACTION.equals(arg0)) {
			try {
				String phoneNumber = arg1.getString(0);
				Call(phoneNumber);
				return new PluginResult(Status.OK);
			} catch (Exception Ex) {
				result = new PluginResult(Status.ERROR);
			}
		} else {
			result = new PluginResult(Status.INVALID_ACTION);
		}
		return result;
	}

	public void Call(String phoneNumber) {
		Intent callIntent = new Intent(Intent.ACTION_CALL);
		callIntent.setData(Uri.parse("tel:"+phoneNumber));
		this.cordova.startActivityForResult((Plugin) this, callIntent, code);
	}

}
