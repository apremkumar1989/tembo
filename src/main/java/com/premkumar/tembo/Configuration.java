package com.premkumar.tembo;

import java.util.HashMap;
import java.util.Map;

public class Configuration {

	private int totalWorkerCount = 10;

	private Map<Integer, PortLevelConfig> portMap = new HashMap<Integer, PortLevelConfig>();

	public PortLevelConfig getByPort(int port) {
		return portMap.get(port);
	}

	public Map<Integer, PortLevelConfig> getPortMap() {
		return portMap;
	}

	public int getTotalWorkerCount() {
		return totalWorkerCount;
	}

	public void setTotalWorkerCount(int totalWorkerCount) {
		this.totalWorkerCount = totalWorkerCount;
	}

	public static class PortLevelConfig {
		private Map<String, ServerBlock> hostMapping = new HashMap<String, ServerBlock>();
		private ServerBlock defaultBlock;
		private int port;

		public Map<String, ServerBlock> getHostMapping() {
			return hostMapping;
		}

		public void setHostMapping(Map<String, ServerBlock> hostMapping) {
			this.hostMapping = hostMapping;
		}

		public ServerBlock getDefaultBlock() {
			return defaultBlock;
		}

		public void setDefaultBlock(ServerBlock defaultBlock) {
			this.defaultBlock = defaultBlock;
		}

		public int getPort() {
			return port;
		}

		public void setPort(int port) {
			this.port = port;
		}
	}
}
