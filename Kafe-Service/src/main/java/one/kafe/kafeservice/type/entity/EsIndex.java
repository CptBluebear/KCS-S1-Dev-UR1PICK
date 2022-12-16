package one.kafe.kafeservice.type.entity;

import org.elasticsearch.geometry.Point;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.GeoPointField;

import co.elastic.clients.util.DateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(indexName = "ur1pick_web_log", createIndex = false)
public class EsIndex {
	@Id
	private String id;

	@Field("method")
	private String method;

	@Field("@timestamp")
	private DateTime timestamp;

	@Field("currentDate")
	private LocalDateTime localdate;

	@Field("time")
	private String localtime;

	@Field("useragent")
	private Object useragent;

	@Field("geoip")
	private Object geoIP;

	@Field("localIP")
	private String localIP;

	@Field("Referer")
	private String referer;

	@Field("XForwardedFor")
	private String xForwardedFor;

	@Field("current_path")
	private String path;

	@GeoPointField
	private Point location;
}



