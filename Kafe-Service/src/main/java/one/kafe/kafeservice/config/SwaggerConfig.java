package one.kafe.kafeservice.config;

import java.util.function.Predicate;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.OAS_30)
			.useDefaultResponseMessages(false)
			.select()
			.apis(RequestHandlerSelectors.basePackage("one.kafe.kafeservice.controller"))
			.paths(Predicate.not(PathSelectors.regex("/")))
			.build()
			.apiInfo(apiInfo());
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
			.title("Kafe api")
			.description("카카오 클라우드 스쿨 1기 개발자 과정 1팀 ur1pick")
			.build();
	}
}
