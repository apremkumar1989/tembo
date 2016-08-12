package com.premkumar.tembo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {
	ServerSocket server = null;

	private static int SERVER_PORT = 9090;

	public HttpServer() throws Exception {
		server = new ServerSocket(SERVER_PORT);

	}

	public void run() throws Exception {
		try {

			while (true) {
				System.out.println("loop");
				Socket socket = server.accept();
				Thread t = new Thread(new SocketThread(socket), "t-" + System.currentTimeMillis());
				t.start();
			}
		} finally {
			if (server != null) {
				try {
					server.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) throws Exception {
		HttpServer myServer = new HttpServer();
		myServer.run();
	}

}
