package com.premkumar.tembo;

public class ServerBlock {

	private String[] hosts;
	private int port;
	private String basePath;
	private String[] indexFiles = {};

	public String[] getHosts() {
		return hosts;
	}

	public void setHosts(String[] hosts) {
		this.hosts = hosts;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getBasePath() {
		return basePath;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	public String[] getIndexFiles() {
		return indexFiles;
	}

	public void setIndexFiles(String[] indexFiles) {
		this.indexFiles = indexFiles;
	}

}
