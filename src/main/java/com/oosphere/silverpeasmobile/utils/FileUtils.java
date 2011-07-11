package com.oosphere.silverpeasmobile.utils;

import java.io.File;

public class FileUtils {

	public static String getExtension(File file) {
		String fileName = file.getName();
		int index = fileName.lastIndexOf(".");
		return (index != -1 ? fileName.substring(index + 1).toLowerCase() : null);
	}
	
	public static String getPath(String fileName) {
	  return fileName.replace('\\', File.separatorChar).replace('/', File.separatorChar);
	}

}
