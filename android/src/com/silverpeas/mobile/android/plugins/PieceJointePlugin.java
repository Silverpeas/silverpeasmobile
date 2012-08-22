package com.silverpeas.mobile.android.plugins;

import java.io.File;

import org.apache.cordova.api.Plugin;
import org.apache.cordova.api.PluginResult;
import org.apache.cordova.api.PluginResult.Status;
import org.json.JSONArray;

import android.content.Intent;
import android.net.Uri;
import android.webkit.MimeTypeMap;

public class PieceJointePlugin extends Plugin{

	public static final String ACTION = "path";
	private int code = 0;
	
	@Override
	public PluginResult execute(String arg0, JSONArray arg1, String arg2) {
		PluginResult result = null;
		if (ACTION.equals(arg0)) {
			try {
				String filePath = arg1.getString(0);
				OpenDocument(filePath);
				result = new PluginResult(Status.OK);
			} catch (Exception Ex) {
				result = new PluginResult(Status.ERROR);
			}
		} else {
			result = new PluginResult(Status.INVALID_ACTION);
		}
		return result;
	}
	
	public void OpenDocument(String filePath){
		Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        File file = new File(filePath);
       
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        String ext=file.getName().substring(file.getName().lastIndexOf(".") + 1).toLowerCase();
        String type = mime.getMimeTypeFromExtension(ext);
      
        intent.setDataAndType(Uri.fromFile(file),type);
       
        this.cordova.startActivityForResult((Plugin) this, intent, code);
	}
}
