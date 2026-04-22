package com.mobimoney.pcl.configuration;

import java.io.InputStream;

/**
 * Class com.mobimoney.pcl.configuration.ConfigurationFileWrapper
 * created at 25.04.16 - 16:58
 */
public class ConfigurationFileWrapper {
	private InputStream inputStream;
	private String fileName;

	public ConfigurationFileWrapper(InputStream inputStream, String fileName) {
		this.inputStream = inputStream;
		this.fileName = fileName;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
