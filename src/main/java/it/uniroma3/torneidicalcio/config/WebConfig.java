package it.uniroma3.torneidicalcio.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configurazione CORS globale.
 *
 * NOTA: con il frontend React embedded (servito da /dashboard/index.html
 * dentro src/main/resources/static/), le chiamate API usano URL relativi
 * e quindi NON necessitano di CORS. Questa configurazione rimane attiva
 * per eventuali client esterni (es. app mobile o frontend separato su
 * localhost:3000) che potrebbero chiamare le API in futuro.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
