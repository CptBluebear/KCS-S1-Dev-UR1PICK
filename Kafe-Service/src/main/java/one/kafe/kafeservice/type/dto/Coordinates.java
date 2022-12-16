package one.kafe.kafeservice.type.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Coordinates {
	private float lon;
	private float lat;
	private Long cnt;

	public void increaseCnt() {
		cnt++;
	}
}