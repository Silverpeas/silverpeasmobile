package com.silverpeas.mobile.client.common.phonegap;

import com.gwtmobile.phonegap.client.FileMgr.EntryCallback;

public class FileManagerAddOn {
	public static native void resolveLocalFileSystemURI(String uri, EntryCallback callback) /*-{
		$wnd.resolveLocalFileSystemURI(uri, function(success){
			callback.@com.gwtmobile.phonegap.client.FileMgr.EntryCallback::onSuccess(Lcom/gwtmobile/phonegap/client/FileMgr$Entry;)(success);
		}, function(error){
			callback.@com.gwtmobile.phonegap.client.FileMgr.FileSystemCallback::onError(Lcom/gwtmobile/phonegap/client/FileMgr$FileError;)(error);
		});
	}-*/;
}
