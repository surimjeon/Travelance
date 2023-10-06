package com.example.bank.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/* 로컬
* http://localhost:8080/swagger-ui.html
* */
@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .version("v1.0.0")
                .title("BANK SERVER")
                .description("D210 BANK SERVER (& MY DATA) API 명세서");

        String APIKEY = "API Key";
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(APIKEY); // 헤더에 토큰 포함
        Components components = new Components().addSecuritySchemes(APIKEY, new SecurityScheme()
                .name("Authorization")  // 헤더 이름을 'Authorization'으로 변경
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER)  // 헤더에 위치하도록 지정
        );

        return new OpenAPI()
                .info(info)
                .addSecurityItem(securityRequirement)
                .components(components);

    }
}