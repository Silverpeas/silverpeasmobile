package com.silverpeas.mobile.android;

import org.apache.cordova.api.Plugin;
import org.apache.cordova.api.PluginResult;
import org.apache.cordova.api.PluginResult.Status;
import org.json.JSONArray;

import android.media.ExifInterface;

public class OrientationExifPlugin extends Plugin {

	public static final String ACTION = "path";

	@Override
	public PluginResult execute(String arg0, JSONArray arg1, String arg2) {
		PluginResult result = null;
		if (ACTION.equals(arg0)) {
			try {
				String filePath = arg1.getString(0);
				ExifInterface exifReader = new ExifInterface(filePath);
				int orientation = exifReader.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
				return new PluginResult(Status.OK, orientation);
			} catch (Exception Ex) {
				result = new PluginResult(Status.ERROR);
			}
		} else {
			result = new PluginResult(Status.INVALID_ACTION);
		}
		return result;
	}
}
