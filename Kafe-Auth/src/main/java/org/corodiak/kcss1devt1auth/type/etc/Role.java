package org.corodiak.kcss1devt1auth.type.etc;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
	USER("ROLE_USER", "Normal User");

	private String key;
	private String description;
}
