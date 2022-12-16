package one.kafe.kafeservice.type.etc;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
	USER("ROLE_USER", "Normal User");

	private String key;
	private String description;
}
