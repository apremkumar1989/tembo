package com.premkumar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.Socket;
import java.util.HashMap;

public class SocketThread implements Runnable {

	private static String CONTENT_LENGTH = "Content-Length";

	private Socket socket;

	public SocketThread(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		try {
			InputStream inputStream = socket.getInputStream();

			String str = convertInputStream(inputStream);
			System.out.println(str);
			OutputStream outputStream = socket.getOutputStream();
			outputStream.write("accepted".getBytes());
			System.out.println("wrote to output stream");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private String convertInputStream(InputStream inputStream)
			throws IOException {
		char[] buffer = new char[1024 * 4];
		InputStreamReader input = new InputStreamReader(inputStream);
		BufferedReader br = new BufferedReader(input);
		StringWriter writer = new StringWriter();
		String firstLine = br.readLine();
		boolean isGet = firstLine.startsWith("GET");
		boolean isPost = firstLine.startsWith("POST");
		HashMap<String, String> headerMap = new HashMap<String, String>();

		if (isGet) {
			String line;
			while (true) {
				line = br.readLine();
				System.out.println("read: " + line);

				if (line.isEmpty())
					break;
				String[] split = parseHeader(line);
				headerMap.put(split[0], split[1]);
			}
			return "parsed get request";
		}

		if (isPost) {

			String line;
			while (true) {
				line = br.readLine();
				System.out.println("read: " + line);

				if (line.isEmpty())
					break;
				String[] split = parseHeader(line);
				headerMap.put(split[0], split[1]);
			}

			int content_length = Integer
					.parseInt(headerMap.get(CONTENT_LENGTH));
			System.out.println(content_length);

			StringWriter bodyWriter = new StringWriter(content_length);
			int count = 0;
			while (count < content_length) {
				bodyWriter.append((char) br.read());
				count++;
			}
			System.out.println("body:\n" + bodyWriter.toString());
			return "parsed post request";
		}

		return "bad request";
	}

	private String[] parseHeader(String headerLine) {
		return headerLine.split(": ");
	}

}
