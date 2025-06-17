package upeu.ms.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
/**
 * http://localhost:9001/api/sqa/swagger-ui/index.html
 * https://prezi.com/view/r4DP4TCmYUJk1eaqjKG4/
 */
@Configuration
public class SwaggerConfig {

    @Bean
    OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().
                        title("SQAPe API")
                        .description("Sistema de Gestion de Aseguramiento de la Calidad de Softwaren")
                        .version("v1.0.0")
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")));
    }
}
