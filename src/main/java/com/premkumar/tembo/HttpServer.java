package com.premkumar.tembo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.premkumar.tembo.Configuration.PortLevelConfig;

public class HttpServer implements Runnable {

	private static Logger LOGGER = LoggerFactory.getLogger(HttpServer.class);

	private PortLevelConfig portConfig;
	private ServerSocket server = null;
	private ExecutorService pool;

	public HttpServer(PortLevelConfig portConfig, ExecutorService pool) throws Exception {
		this.portConfig = portConfig;
		this.pool = pool;
	}

	public void run() {
		try {
			server = new ServerSocket(portConfig.getPort());
			LOGGER.info("server listening on port " + server.getLocalPort());
			try {
				ExecutorService threadPool = pool;
				while (true) {
					LOGGER.info("listening....");
					Socket socket = server.accept();
					threadPool.execute(new SocketThread(socket, portConfig));
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
		} catch (Exception e) {
			LOGGER.error("exiting server because of unhandled exception.", e);
		}
	}

}
