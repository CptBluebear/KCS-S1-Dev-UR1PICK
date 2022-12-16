package org.corodiak.kcss1devt1auth.type.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.corodiak.kcss1devt1auth.type.etc.Role;
import org.corodiak.kcss1devt1auth.type.etc.UserStatus;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
public class User extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_seq")
	private Long seq;

	private String name;

	private String email;

	private String plan;

	private LocalDateTime lastLogin;

	@Enumerated(EnumType.STRING)
	private UserStatus userStatus;

	@Enumerated(EnumType.STRING)
	private Role role;

}
