package one.kafe.kafeservice.util;

import javax.servlet.http.HttpServletRequest;

public class ExtractIPUtils {

	public static String getIP(HttpServletRequest request) {
		String ip = request.getHeader("X-FORWARDED-FOR");
		if (ip == null) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

}
