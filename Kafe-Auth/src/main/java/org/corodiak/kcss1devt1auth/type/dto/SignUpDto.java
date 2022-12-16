package org.corodiak.kcss1devt1auth.type.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SignUpDto {

	@Email
	private String email;

	@Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,32}$")
	private String password;

	@Size(min = 1, max = 16)
	@Pattern(regexp = "^[^!@#$%^&*()_\\-+=`~\\[\\{\\]\\}:;\"'<,>.?/\\\\|]+$")
	private String name;

}
