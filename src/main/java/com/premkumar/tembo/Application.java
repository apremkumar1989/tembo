package com.premkumar.tembo;

import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.premkumar.tembo.Configuration.PortLevelConfig;

public class Application {
	private static Logger LOGGER = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) throws Exception {
		LOGGER.info("starting main");
		InputStream is = Application.class.getClassLoader().getResourceAsStream("tembo-conf.json");
		Configuration config = ConfigurationParser.buildConf(is);
		LOGGER.info("parsed the config file...");

		ExecutorService pool = Executors.newFixedThreadPool(config.getTotalWorkerCount());
		LOGGER.info("created worker thread pool. Total: " + config.getTotalWorkerCount());

		for (PortLevelConfig portConfig : config.getPortMap().values()) {
			LOGGER.info("spawning a server for port:" + portConfig.getPort());
			HttpServer server = new HttpServer(portConfig, pool);
			Thread serverThread = new Thread(server, "server-" + portConfig.getPort());
			serverThread.start();
			LOGGER.info("spawned a server for port:" + portConfig.getPort());
		}
	}

}
