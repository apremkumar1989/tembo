package com.premkumar.tembo;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestDispatcher {
	private static Logger LOGGER = LoggerFactory.getLogger(RequestDispatcher.class);

	public RequestDispatcher() {
	}

	public void dispatch(ServerBlock serverBlock, HttpRequest request, HttpResponse response) throws Exception {
		// Handles only Static get requests correctly
		String static_file_path = serverBlock.getBasePath();
		String resource = request.getResource();
		// remove query string from resource
		int questionMarkPos = resource.indexOf("?");
		if (questionMarkPos > -1)
			resource = resource.substring(0, questionMarkPos);

		if ((resource.trim().length() == 0 || resource.equals("/")) && serverBlock.getIndexFiles() != null) {
			// see if index files are present
			for (String file : serverBlock.getIndexFiles()) {
				File f = new File(static_file_path + "/" + file);
				if (!f.exists())
					continue;

				resource = file;
				break;
			}
		}
		resource = resource.startsWith("/") ? resource : ("/".concat(resource));
		File f = new File(static_file_path + resource);
		if (!f.exists() || f.isDirectory()) {
			LOGGER.info("resource does not exist. " + f.getAbsolutePath());
			OutputStream outputStream = response.getOutputStream();
			outputStream.write((request.getHttpVersion() + " 404 Not Found\n").getBytes());
			outputStream.write(("\n").getBytes());
			outputStream.close();
			return;
		}
		int lastDotPosition = resource.lastIndexOf(".");
		String fileExtension = resource.substring(lastDotPosition + 1);
		FileInputStream fis = new FileInputStream(f);
		OutputStream outputStream = response.getOutputStream();
		outputStream.write((request.getHttpVersion() + " 200 OK\n").getBytes());
		String contentType = getContentType(fileExtension);
		if (contentType != null)
			outputStream.write(("Content-Type:" + contentType + "\n").getBytes());
		// TODO headers and cookie addition
		outputStream.write(("myheader1: value2\n").getBytes());
		outputStream.write(("Set-Cookie: yummy_cookie=choco124\n").getBytes());
		outputStream.write(("\n").getBytes());

		while (fis.available() != 0) {
			outputStream.write(fis.read());
		}

		fis.close();
		response.getOutputStream().close();
	}

	private String getContentType(String fileExtension) {
		switch (fileExtension.toLowerCase()) {
		case "html":
			return "text/html";
		case "css":
			return "text/css";
		case "js":
			return "text/javascript";
		case "jpeg":
			return "image/jpeg";
		case "jpg":
			return "image/jpeg";
		case "png":
			return "image/png";
		case "gif":
			return "image/gif";
		case "txt":
			return "text/plain";

		default:
			return null;
		}
	}
}
