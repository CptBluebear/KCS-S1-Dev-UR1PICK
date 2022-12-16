package one.kafe.kafeservice.type.vo;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.experimental.Accessors;
import one.kafe.kafeservice.type.entity.SUrl;
import one.kafe.kafeservice.type.etc.URLStatus;

public class SUrlVo {

	@Getter
	public static class SimpleInfo {
		private Long seq;
		private String shortUrl;
		private String originalUrl;
		private LocalDateTime registerDate;
		private LocalDateTime lastAccess;
		private Long accessCount;
		@Accessors(fluent = true)
		private boolean isCustomUrl;
		@Accessors(fluent = true)
		@JsonProperty(value = "hasPassword")
		private boolean hasPassword;
		@Accessors(fluent = true)
		private boolean isActive;

		public SimpleInfo(SUrl entity) {
			this.seq = entity.getSeq();
			this.shortUrl = entity.getShortUrl();
			this.originalUrl = entity.getOriginalUrl();
			this.registerDate = entity.getRegisterDate();
			this.lastAccess = entity.getLastAccess();
			this.accessCount = entity.getAccessCount();
			this.isCustomUrl = entity.isCustomUrl();
			this.hasPassword = entity.hasPassword();
			this.isActive = entity.isActive();
		}

		@JsonProperty(value = "isCustomUrl")
		public boolean isCustomUrl() {
			return isCustomUrl;
		}

		@JsonProperty(value = "isActive")
		public boolean isActive() {
			return isActive;
		}
	}

	@Getter
	public static class DetailInfo {
		private Long seq;
		private String shortUrl;
		private String originalUrl;
		private LocalDateTime registerDate;
		private LocalDateTime lastAccess;
		private Long accessCount;
		@Accessors(fluent = true)
		private boolean isCustomUrl;
		@Accessors(fluent = true)
		@JsonProperty(value = "hasPassword")
		private boolean hasPassword;
		private LocalDateTime startDate;
		private LocalDateTime endDate;
		private URLStatus urlStatus;
		@Accessors(fluent = true)
		private boolean isActive;

		public DetailInfo(SUrl entity) {
			this.seq = entity.getSeq();
			this.shortUrl = entity.getShortUrl();
			this.originalUrl = entity.getOriginalUrl();
			this.registerDate = entity.getRegisterDate();
			this.lastAccess = entity.getLastAccess();
			this.accessCount = entity.getAccessCount();
			this.isCustomUrl = entity.isCustomUrl();
			this.hasPassword = entity.hasPassword();
			this.startDate = entity.getStartDate();
			this.endDate = entity.getEndDate();
			this.urlStatus = entity.getUrlStatus();
			this.isActive = entity.isActive();
		}

		@JsonProperty(value = "isCustomUrl")
		public boolean isCustomUrl() {
			return isCustomUrl;
		}

		@JsonProperty(value = "isActive")
		public boolean isActive() {
			return isActive;
		}
	}
}
