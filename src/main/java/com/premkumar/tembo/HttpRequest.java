package com.premkumar.tembo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpRequest {

	private static Logger LOGGER = LoggerFactory.getLogger(HttpRequest.class);

	public static enum RequestMethod {
		GET, POST, HEAD, DELETE
	}

	private HttpRequest.RequestMethod requestMethod;
	private String resource;
	private Map<String, String> headers;
	private String body;
	private String httpVersion;

	// TODO
	private Map<String, String> parameters;

	public HttpRequest() {

	}

	public HttpRequest(InputStream inputStream) throws Exception {
		InputStreamReader input = new InputStreamReader(inputStream);
		BufferedReader br = new BufferedReader(input);
		String firstLine = br.readLine();
		String[] lineSplit = firstLine.split(" ");
		RequestMethod rt = RequestMethod.valueOf(lineSplit[0]);

		// TODO
		if (rt == null) {
			throw new Exception("request type not supported");
		}

		boolean isGet = rt == RequestMethod.GET;
		boolean isPost = rt == RequestMethod.POST;
		HashMap<String, String> headerMap = new HashMap<String, String>();
		this.setRequestMethod(rt);
		this.setResource(lineSplit[1]);
		this.setHttpVersion(lineSplit[2]);
		// this.setOutputStream(socket.getOutputStream());

		if (isGet) {
			String line;
			while (true) {
				line = br.readLine();
				LOGGER.debug("read: " + line);

				if (line.isEmpty())
					break;
				String[] split = parseHeader(line);
				headerMap.put(split[0], split[1]);
			}
			this.setHeaders(headerMap);
		}

		if (isPost) {

			String line;
			while (true) {
				line = br.readLine();
				LOGGER.debug("read: " + line);

				if (line.isEmpty())
					break;
				String[] split = parseHeader(line);
				headerMap.put(split[0], split[1]);
			}

			int content_length = Integer.parseInt(headerMap.get(IContants.CONTENT_LENGTH));
			LOGGER.debug("content length : " + content_length);

			StringWriter bodyWriter = new StringWriter(content_length);
			int count = 0;
			while (count < content_length) {
				bodyWriter.append((char) br.read());
				count++;
			}
			String body = bodyWriter.toString();
			LOGGER.debug("body:\n" + body);
			setHeaders(headerMap);
			setBody(body);
		}

	}

	private String[] parseHeader(String headerLine) {
		return headerLine.split(": ");
	}

	public RequestMethod getRequestMethod() {
		return requestMethod;
	}

	public void setRequestMethod(RequestMethod requestMethod) {
		this.requestMethod = requestMethod;
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

	public String getHost() {
		return this.headers.get("Host").split(":")[0];
	}

	public String getPort() {
		String[] split = this.headers.get("Host").split(":");
		return split.length > 1 ? split[1] : "80";
	}

	@Override
	public String toString() {
		return "HttpRequest [requestMethod=" + requestMethod + ", resource=" + resource + ", httpVersion=" + httpVersion + "]";
	}

}
