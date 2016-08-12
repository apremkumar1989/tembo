package com.premkumar.tembo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.Socket;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.premkumar.tembo.IContants.RequestType;

public class SocketThread implements Runnable {
	private static Logger LOGGER = LoggerFactory.getLogger(SocketThread.class);
	
	private Socket socket;

	public SocketThread(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		try {
			HttpRequest request = constructHttpRequest();
			LOGGER.info("request " + request);
			handleRequest(request);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				LOGGER.error(e.getMessage(), e);
			}
		}

	}

	private void handleRequest(HttpRequest request) throws Exception {
		new RequestDispatcher().dispatch(request);
//		OutputStream outputStream = request.getOutputStream();
//		outputStream.write("accepted".getBytes());
//		LOGGER.info("wrote to output stream");
		LOGGER.info("handle request properly");
	}

	private HttpRequest constructHttpRequest() throws IOException {
		InputStream inputStream = socket.getInputStream();
		InputStreamReader input = new InputStreamReader(inputStream);
		BufferedReader br = new BufferedReader(input);
		String firstLine = br.readLine();
		String[] lineSplit = firstLine.split(" ");
		IContants.RequestType rt = RequestType.valueOf(lineSplit[0]);
		if (rt == null) {
			return null;
		}

		HttpRequest httpRequest = new HttpRequest();
		boolean isGet = rt == RequestType.GET;
		boolean isPost = rt == RequestType.POST;
		HashMap<String, String> headerMap = new HashMap<String, String>();
		httpRequest.setRequestType(rt);
		httpRequest.setResource(lineSplit[1]);
		httpRequest.setHttpVersion(lineSplit[2]);
		httpRequest.setOutputStream(socket.getOutputStream());

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
			httpRequest.setHeaders(headerMap);
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
			LOGGER.debug("content length : "+content_length);

			StringWriter bodyWriter = new StringWriter(content_length);
			int count = 0;
			while (count < content_length) {
				bodyWriter.append((char) br.read());
				count++;
			}
			String body = bodyWriter.toString();
			LOGGER.debug("body:\n" + body);
			httpRequest.setHeaders(headerMap);
			httpRequest.setBody(body);
		}

		return httpRequest;
	}

	private String[] parseHeader(String headerLine) {
		return headerLine.split(": ");
	}

}
