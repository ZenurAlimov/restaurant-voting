package com.github.zenuralimov.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.media.Schema;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.SpringDocUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Configuration
//https://sabljakovich.medium.com/adding-basic-auth-authorization-option-to-openapi-swagger-documentation-java-spring-95abbede27e9
@SecurityScheme(
        name = "basicAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "basic"
)
@OpenAPIDefinition(
        info = @Info(
                title = "REST API documentation",
                version = "1.0",
                description = """
                        Приложение по <a href='https://javaops.ru/view/topjava2'>курсу TopJava-2</a> (решение выпускного проекта)
                        <p><b>Тестовые креденшелы:</b><br>
                        - user@yandex.ru / password<br>
                        - admin@gmail.com / admin<br></p>
                        """,
                contact = @Contact(url = "https://github.com/zenuralimov", name = "Zenur Alimov", email = "zenuralimov@gmail.com")
        ),
        security = @SecurityRequirement(name = "basicAuth")
)
public class OpenApiConfig {

    //    https://ru.stackoverflow.com/a/1276885/209226
    static {
        var schema = new Schema<LocalTime>();
        schema.example(LocalTime.now().format(DateTimeFormatter.ISO_TIME));
        SpringDocUtils.getConfig().replaceWithSchema(LocalTime.class, schema);
    }

    @Bean
    public GroupedOpenApi api() {
        return GroupedOpenApi.builder()
                .group("REST API")
                .pathsToMatch("/api/**")
                .build();
    }
}
