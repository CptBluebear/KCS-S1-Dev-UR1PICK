package org.corodiak.kcss1devt1auth.type.etc;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserStatus {

	VERIFIED,
	NOT_VERIFIED,
	VANNED,
	RESIGNED;

}
