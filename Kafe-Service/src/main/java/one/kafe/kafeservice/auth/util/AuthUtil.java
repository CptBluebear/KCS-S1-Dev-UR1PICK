package one.kafe.kafeservice.auth.util;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import one.kafe.kafeservice.auth.exception.UnAuthorizeException;

public class AuthUtil {

	public static User getAuthenticationInfo() {
		return (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	public static long getAuthenticationInfoSeq() throws UnAuthorizeException {
		try {
			return Long.parseLong(getAuthenticationInfo().getUsername());
		} catch (NumberFormatException | NullPointerException e) {
			throw new UnAuthorizeException();
		}
	}

	public static long getAuthenticationInfoSeqOrAnonymousSeq() throws UnAuthorizeException {
		try {
			return Long.parseLong(getAuthenticationInfo().getUsername());
		} catch (NumberFormatException | NullPointerException | ClassCastException e) {
			return -1L;
		}
	}

}
