package com.premkumar.tembo;

import java.io.OutputStream;
import java.util.Map;

import com.premkumar.tembo.IContants.RequestType;

public class HttpRequest {

	private RequestType requestType;
	private String resource;
	private Map<String, String> headers;
	private String body;
	private String httpVersion;
	private OutputStream outputStream;
	
	//TODO
	private Map<String, String[]> parameters;

	public RequestType getRequestType() {
		return requestType;
	}

	public void setRequestType(RequestType requestType) {
		this.requestType = requestType;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public String getHttpVersion() {
		return httpVersion;
	}

	public void setHttpVersion(String httpVersion) {
		this.httpVersion = httpVersion;
	}

	@Override
	public String toString() {
		return "HttpRequest [requestType=" + requestType + ", resource=" + resource + ", httpVersion=" + httpVersion + "]";
	}

	public OutputStream getOutputStream() {
		return outputStream;
	}

	public void setOutputStream(OutputStream outputStream) {
		this.outputStream = outputStream;
	}

}
