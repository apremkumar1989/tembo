package com.premkumar.tembo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpServer {

	private static final int NUM_OF_SERVER_THREADS = 5;

	private static Logger LOGGER = LoggerFactory.getLogger(SocketThread.class);

	ServerSocket server = null;

	private static int SERVER_PORT = 9090;
	

	public HttpServer() throws Exception {
		server = new ServerSocket(SERVER_PORT);

	}

	public void run() throws Exception {
		LOGGER.info("server listening on port " + server.getLocalPort());
		try {
			ExecutorService threadPool = Executors.newFixedThreadPool(NUM_OF_SERVER_THREADS);
			while (true) {
				LOGGER.info("listening....");
				Socket socket = server.accept();
				threadPool.execute(new SocketThread(socket));
			}
		} finally {
			if (server != null) {
				try {
					server.close();
				} catch (IOException e) {
					LOGGER.error(e.getMessage(), e);
				}
			}
		}
	}

	public static void main(String[] args) throws Exception {
		HttpServer myServer = new HttpServer();
		myServer.run();
	}

}
