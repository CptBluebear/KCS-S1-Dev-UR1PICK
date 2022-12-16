package org.corodiak.kcss1devt1auth.type.vo;

import java.time.LocalDateTime;

import org.corodiak.kcss1devt1auth.type.entity.User;
import org.corodiak.kcss1devt1auth.type.etc.UserStatus;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserVo {

	private Long seq;
	private String name;
	private String email;
	private String plan;
	private LocalDateTime lastLogin;
	private UserStatus userStatus;

	public UserVo(User user) {
		this.seq = user.getSeq();
		this.name = user.getName();
		this.email = user.getEmail();
		this.plan = user.getPlan();
		this.lastLogin = user.getLastLogin();
		this.userStatus = user.getUserStatus();
	}
}
