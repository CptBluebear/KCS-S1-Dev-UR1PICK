package org.corodiak.kcss1devt1auth.type.vo;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmailTemplate {

	private String type;

	Map<String, String> properties;

}
