package org.corodiak.kcss1devt1auth.auth.factory;

import java.util.Map;

import org.corodiak.kcss1devt1auth.type.dto.GoogleOAuthUserInfo;
import org.corodiak.kcss1devt1auth.type.dto.OAuthUserInfo;
import org.corodiak.kcss1devt1auth.type.etc.OAuthProvider;

public class OAuth2UserInfoFactory {
	public static OAuthUserInfo of(OAuthProvider oAuthProvider, Map<String, Object> attributes) throws
		IllegalArgumentException {
		switch (oAuthProvider) {
			case GOOGLE:
				return new GoogleOAuthUserInfo(attributes);
			default:
				throw new IllegalArgumentException("OAuthProvider Not Excepted!!");
		}
	}
}
