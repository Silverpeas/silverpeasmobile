package com.silverpeas.mobile.server.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemHeaders;
import org.apache.commons.io.IOUtils;

public class LocalDiskFileItem implements FileItem {

	private static final long serialVersionUID = 1L;
	private File file;
	private FileItemHeaders headers;

	@Override
	public FileItemHeaders getHeaders() {
		return headers;
	}

	@Override
	public void setHeaders(FileItemHeaders headers) {
		this.headers = headers;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		InputStream inputStream = new FileInputStream(file.getAbsolutePath());
		return inputStream;
	}

	@Override
	public String getContentType() {
		return "image/jpeg";
	}

	public LocalDiskFileItem(File file) {
		super();
		this.file = file;
	}

	@Override
	public String getName() {
		return file.getAbsolutePath();
	}

	@Override
	public boolean isInMemory() {		
		return false;
	}

	@Override
	public long getSize() {
		return file.length();
	}

	@Override
	public byte[] get() {
		try {
			return IOUtils.toByteArray(new FileInputStream(file.getAbsolutePath()));
		} catch (Exception e) {
			return null;
		} 		
	}

	@Override
	public String getString(String encoding)
			throws UnsupportedEncodingException {
		return null;
	}

	@Override
	public String getString() {
		return null;
	}

	@Override
	public void write(File file) throws Exception {
	}

	@Override
	public void delete() {
	}

	@Override
	public String getFieldName() {
		return "WAIMGVAR0";
	}

	@Override
	public void setFieldName(String name) {
	}

	@Override
	public boolean isFormField() {
		return false;
	}

	@Override
	public void setFormField(boolean state) {
	}

	@Override
	public OutputStream getOutputStream() throws IOException {
		OutputStream outputStream = new FileOutputStream(file.getAbsolutePath());
		return outputStream;
	}

}
