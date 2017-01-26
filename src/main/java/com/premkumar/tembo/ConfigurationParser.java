package com.premkumar.tembo;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.premkumar.tembo.Configuration.PortLevelConfig;

public class ConfigurationParser {
	private static Logger LOGGER = LoggerFactory.getLogger(ConfigurationParser.class);

	public static Configuration buildConf(InputStream inputStream) {
		String jsonString = Util.readFile(inputStream);
		JSONObject json = new JSONObject(jsonString);
		LOGGER.info(json.toString());
		Configuration config = new Configuration();
		JSONArray arr = json.getJSONArray("mapping");
		for (int i = 0; i < arr.length(); i++) {
			JSONObject sbConf = arr.getJSONObject(i);
			ServerBlock serverBlock = buildServerBlock(sbConf);
			addServerBlock(config, serverBlock);
		}

		try {
			config.setTotalWorkerCount(json.getInt("total-workers"));
		} catch (JSONException e) {
		}
		return config;
	}

	public static void addServerBlock(Configuration config, ServerBlock serverBlock) {
		Map<Integer, PortLevelConfig> portMap = config.getPortMap();
		PortLevelConfig plc;
		if (!portMap.containsKey(serverBlock.getPort())) {
			plc = new PortLevelConfig();
			plc.setDefaultBlock(serverBlock);
			plc.setPort(serverBlock.getPort());
			portMap.put(serverBlock.getPort(), plc);
		}
		plc = portMap.get(serverBlock.getPort());
		for (String host : serverBlock.getHosts()) {
			if (plc.getHostMapping().containsKey(host)) {
				throw new RuntimeException(host + " already existing for port " + plc.getPort());
			}
			plc.getHostMapping().put(host, serverBlock);
		}
	}

	private static ServerBlock buildServerBlock(JSONObject sbConf) {
		ServerBlock serverBlock = new ServerBlock();
		JSONArray hostsFromConf = sbConf.getJSONArray("host");
		String[] hosts1 = new String[hostsFromConf.length()];
		int i = 0;
		Iterator<Object> iterator = hostsFromConf.iterator();
		while (iterator.hasNext())
			hosts1[i++] = (String) iterator.next();
		serverBlock.setHosts(hosts1);
		serverBlock.setPort(sbConf.getInt("port"));
		serverBlock.setBasePath(sbConf.getString("location"));

		if (sbConf.has("index")) {
			JSONArray indexFilesJson = sbConf.getJSONArray("index");
			String[] indexFiles = new String[hostsFromConf.length()];
			i = 0;
			iterator = indexFilesJson.iterator();
			while (iterator.hasNext())
				indexFiles[i++] = (String) iterator.next();
			serverBlock.setIndexFiles(indexFiles);

		}
		if (sbConf.has("headers")) {
			JSONArray headersJson = sbConf.getJSONArray("headers");
			i = 0;
			iterator = headersJson.iterator();
			while (iterator.hasNext()) {
				JSONObject headerJson = (JSONObject) iterator.next();
				serverBlock.getHeaders().add(new HttpHeader(headerJson.getString("name"), headerJson.getString("value")));
			}

		}
		return serverBlock;
	}
}
