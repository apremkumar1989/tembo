package com.premkumar.tembo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer {
	ServerSocket server = null;

	private static int SERVER_PORT = 9090;

	public HttpServer() throws Exception {
		server = new ServerSocket(SERVER_PORT);

	}

	public void run() throws Exception {
		try {
			ExecutorService threadPool = Executors.newFixedThreadPool(1);
			while (true) {
				System.out.println("loop");
				Socket socket = server.accept();
				threadPool.execute(new SocketThread(socket));
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
