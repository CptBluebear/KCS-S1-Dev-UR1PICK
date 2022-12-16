package one.kafe.kafeservice.type.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ModifiedSUrlDto {
	@NotBlank
	@Length(max = 2083)
	private String originalUrl;

	private String password = "";

	private LocalDateTime startDate = LocalDateTime.now();

	private LocalDateTime endDate = LocalDateTime.now().plusMonths(1);

	@JsonProperty
	@Accessors(fluent = true)
	private boolean hasPassword;
}
