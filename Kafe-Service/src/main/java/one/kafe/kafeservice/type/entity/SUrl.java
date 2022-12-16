package one.kafe.kafeservice.type.entity;

import java.time.LocalDateTime;

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

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import one.kafe.kafeservice.type.etc.URLStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Entity
public class SUrl extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "short_url_seq")
	private Long seq;

	private String shortUrl;

	private String originalUrl;

	private String password;

	@Accessors(fluent = true)
	@Column(name = "custom_url_bool")
	private boolean isCustomUrl;

	@Accessors(fluent = true)
	@Column(name = "passwd_bool")
	private boolean hasPassword;

	private LocalDateTime startDate;

	private LocalDateTime endDate;

	private LocalDateTime lastAccess;

	private Long accessCount;

	private String ownerIp;

	private LocalDateTime registerDate;

	private String preview;

	@Accessors(fluent = true)
	@Column(name = "active_bool")
	private boolean isActive;

	@Enumerated(EnumType.STRING)
	private URLStatus urlStatus;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_seq")
	private User user;
}
