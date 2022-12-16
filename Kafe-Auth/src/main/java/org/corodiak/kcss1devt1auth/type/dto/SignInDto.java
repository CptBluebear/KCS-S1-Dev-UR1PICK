package org.corodiak.kcss1devt1auth.type.dto;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
@Setter
@NoArgsConstructor
public class SignInDto {

	@NotNull
	private String email;

	@NotNull
	private String password;

}
