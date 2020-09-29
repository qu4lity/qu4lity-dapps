package it.eng.sidrestapi.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class Swagger2UiConfiguration {
    // The select() method called on Docket bean returns an "ApiSelectorBuilder". This provides "apis()" and "paths()" methods to filter the controllers and methods being documented using string predicates.
    @Bean
    public Docket postsApi() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(metadata()).select().paths(regex("/identity.*")).build();
    }

    private ApiInfo metadata() {
        return new ApiInfoBuilder().title("Rest API SID").version("1.0").build();
    }
}