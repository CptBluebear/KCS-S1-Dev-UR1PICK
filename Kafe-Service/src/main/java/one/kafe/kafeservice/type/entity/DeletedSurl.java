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
import one.kafe.kafeservice.type.etc.URLStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Entity
public class DeletedSurl extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "deleted_url_seq")
	private Long seq;

	private Long originalSeq;

	private String shortUrl;

	private String originalUrl;

	private String password;

	@Column(name = "custom_url_bool")
	private boolean isCustomURL;

	@Column(name = "passwd_bool")
	private boolean hasPassword;

	private LocalDateTime startDate;

	private LocalDateTime endDate;

	private LocalDateTime lastAccess;

	private Long accessCount;

	private String ownerIp;

	private LocalDateTime registerDate;

	@Enumerated(EnumType.STRING)
	private URLStatus urlStatus;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_seq")
	private User user;
}
