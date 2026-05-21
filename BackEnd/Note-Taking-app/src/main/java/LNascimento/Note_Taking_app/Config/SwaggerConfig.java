package LNascimento.Note_Taking_app.Config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.webmvc.api.OpenApiActuatorResource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Gerenciamento de Notas")
                        .description("API RESTful desenvolvida com Spring Boot 3, contendo autenticação JWT, Refresh Tokens e Blacklist.\"")
                        .version("v1.0.0")
                        .contact(new Contact().name("Lucas Nascimento").url("https://www.linkedin.com/in/lucas-nascimento-661b162b1/")))

                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .name("bearerAuth")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}
