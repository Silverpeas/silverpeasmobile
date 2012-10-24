package com.silverpeas.mobile.client.common.phonegap;

public class Downloader {
	public static native void downloadFile(String targetUrl) /*-{		
		$wnd.plugins.downloader.downloadFile(targetUrl, {overwrite: true}, null, null);
	}-*/;
}
