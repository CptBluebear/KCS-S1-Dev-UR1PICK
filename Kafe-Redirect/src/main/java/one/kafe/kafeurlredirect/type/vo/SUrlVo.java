package one.kafe.kafeurlredirect.type.vo;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.experimental.Accessors;
import one.kafe.kafeurlredirect.type.entity.SUrl;
import one.kafe.kafeurlredirect.type.etc.URLStatus;

@Getter
public class SUrlVo {
	private Long seq;
	private String shortUrl;
	private String originalUrl;
	private String password;
	@Accessors(fluent = true)
	private boolean hasPassword;
	private LocalDateTime startDate;
	private LocalDateTime endDate;
	private URLStatus urlStatus;
	private String preview;
	private boolean isActive;

	public SUrlVo(SUrl entity) {
		this.seq = entity.getSeq();
		this.shortUrl = entity.getShortUrl();
		this.originalUrl = entity.getOriginalUrl();
		this.password = entity.getPassword();
		this.hasPassword = entity.hasPassword();
		this.startDate = entity.getStartDate();
		this.endDate = entity.getEndDate();
		this.urlStatus = entity.getUrlStatus();
		this.preview = entity.getPreview();
		this.isActive = entity.isActive();
	}
}
