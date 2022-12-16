package one.kafe.kafeservice.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

public class UrlList {

	private Set<String> blackList;

	private final String blackListPath;

	public UrlList(String blackListPath) throws IOException {
		this.blackListPath = blackListPath;
		updateBlackList();
	}

	public boolean containBlacklist(String url) {
		return blackList.contains(url);
	}

	public void updateBlackList() throws IOException {
		try (BufferedInputStream in = new BufferedInputStream(
			new URL(blackListPath).openStream());
			 InputStreamReader isr = new InputStreamReader(in, StandardCharsets.UTF_8);
			 BufferedReader reader = new BufferedReader(isr);
		) {
			String tmp = null;
			Set<String> tmpList = new HashSet<>();
			while ((tmp = reader.readLine()) != null) {
				tmpList.add(tmp);
			}
			blackList = tmpList;
		}
	}
}
