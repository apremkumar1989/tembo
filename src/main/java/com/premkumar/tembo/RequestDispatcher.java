package com.premkumar.tembo;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestDispatcher {
	private static Logger LOGGER = LoggerFactory.getLogger(RequestDispatcher.class);
	private String static_file_path = "/tmp/";

	public void dispatch(HttpRequest request) throws Exception {
		//Handles only Static get requests correctly
		File f = new File(static_file_path + request.getResource());
		if (!f.exists() || f.isDirectory()) {
			LOGGER.info("resource does not exist. "+ f.getAbsolutePath());
			OutputStream outputStream = request.getOutputStream();
			outputStream.write((request.getHttpVersion() + " 404 Not Found\n").getBytes());
			outputStream.write(("\n").getBytes());
			outputStream.close();
			return;
		}
		FileInputStream fis = new FileInputStream(f);
		OutputStream outputStream = request.getOutputStream();
		outputStream.write((request.getHttpVersion() + " 200 OK\n").getBytes());
		outputStream.write(("myheader: value1\n").getBytes());
		outputStream.write(("myheader1: value2\n").getBytes());
		outputStream.write(("\n").getBytes());
		
		
		while (fis.available() != 0) {
			outputStream.write(fis.read());
		}

		fis.close();
		request.getOutputStream().close();
	}
}
