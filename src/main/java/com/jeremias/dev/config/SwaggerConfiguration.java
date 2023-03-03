package com.jeremias.dev.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;




@OpenAPIDefinition

//@EnableWebMvc
@Configuration
public class SwaggerConfiguration {
	 @Bean
	    public OpenAPI baseOpenAPI() {
	    
	     return new OpenAPI()
	                .info(getApiInfo());
	    }
	 
	    private Info getApiInfo() {
	        return new Info()
	                .title("Reddit Clone API")
	                .version("1.0")
	                .description("API for Reddit Clone Application");
	                //.contact(new Contact("Sasu dxd ", "https://github.com/Dllxsasu", "s@email.com"))
	             //   .license("Apache License Version 2.0")
	              //  .build();
	    }

	/*
	public static final String AUTHORIZATION_HEADER = "Authorization";
	@Bean
    public Docket redditCloneApi() {
		 Docket docket =  new Docket(DocumentationType.SWAGGER_2)
                .select()
                
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                
                //.pathMapping("/")
                .build()
                //.pathMapping("/")
                .apiInfo(getApiInfo());
		
		 return docket;
    }
	
    private ApiInfo getApiInfo() {
        return new ApiInfoBuilder()
                .title("Reddit Clone API")
                .version("1.0")
                .description("API for Reddit Clone Application")
                .contact(new Contact("Sasu dxd ", "https://github.com/Dllxsasu", "s@email.com"))
                .license("Apache License Version 2.0")
                .build();
    }
    
    
    private ApiKey apiKey() {
        return new ApiKey(AUTHORIZATION_HEADER, "JWT", "header");
    }
    */
/*
    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .build();
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope
                = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference(AUTHORIZATION_HEADER, authorizationScopes));
    }
    */
}
