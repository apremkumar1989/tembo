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

import com.premkumar.tembo.Configuration.PortLevelConfig;

public class SocketThread implements Runnable {
	private static Logger LOGGER = LoggerFactory.getLogger(SocketThread.class);

	private Socket socket;
	private PortLevelConfig portConfig;

	public SocketThread(Socket socket, PortLevelConfig portConfig) {
		this.socket = socket;
		this.portConfig = portConfig;
	}

	public void run() {
		try {
			HttpRequest request = constructHttpRequest();
			LOGGER.info("request " + request);
			HttpResponse response = new HttpResponse(socket.getOutputStream());
			handleRequest(request, response);
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

	private void handleRequest(HttpRequest request, HttpResponse response) throws Exception {
		ServerBlock serverBlock = portConfig.getHostMapping().get(request.getHost());
		if (serverBlock == null) {
			serverBlock = portConfig.getDefaultBlock();
		}
		new RequestDispatcher().dispatch(serverBlock, request, response);
		LOGGER.info("handle request properly");
	}

	private HttpRequest constructHttpRequest() throws IOException {
		InputStream inputStream = socket.getInputStream();
		InputStreamReader input = new InputStreamReader(inputStream);
		BufferedReader br = new BufferedReader(input);
		String firstLine = br.readLine();
		String[] lineSplit = firstLine.split(" ");
		HttpRequest.RequestMethod rt = HttpRequest.RequestMethod.valueOf(lineSplit[0]);
		if (rt == null) {
			return null;
		}

		HttpRequest httpRequest = new HttpRequest();
		boolean isGet = rt == HttpRequest.RequestMethod.GET;
		boolean isPost = rt == HttpRequest.RequestMethod.POST;
		HashMap<String, String> headerMap = new HashMap<String, String>();
		httpRequest.setRequestMethod(rt);
		httpRequest.setResource(lineSplit[1]);
		httpRequest.setHttpVersion(lineSplit[2]);

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
			LOGGER.debug("content length : " + content_length);

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
