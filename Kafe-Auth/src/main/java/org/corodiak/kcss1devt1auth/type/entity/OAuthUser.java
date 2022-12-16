package org.corodiak.kcss1devt1auth.type.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.corodiak.kcss1devt1auth.type.etc.OAuthProvider;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
public class OAuthUser extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "oauthuser_seq")
	private Long seq;

	private String email;

	private String name;

	private String providerUserId;

	@Enumerated(EnumType.STRING)
	private OAuthProvider oap;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_seq")
	private User user;

}
