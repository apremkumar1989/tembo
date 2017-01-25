package com.premkumar.tembo;

import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.Map;

public class HttpResponse {

	private int statusCode = 200;

	private Map<String, String> headers = new LinkedHashMap<String, String>();

	private OutputStream outputStream;

	public HttpResponse(OutputStream outputStream) {
		this.outputStream = outputStream;
	}

	public void addHeader(String header, String value) {
		headers.put(header, value);
	}

	public void removeHeader(String header) {
		headers.remove(header);
	}

	public boolean containsHeader(String header) {
		return headers.containsKey(header);
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public OutputStream getOutputStream() {
		return outputStream;
	}

}
