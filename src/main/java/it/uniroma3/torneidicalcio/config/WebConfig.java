package it.uniroma3.torneidicalcio.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:5173")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    /**
     * Da quando la home è React (build Vite statica in resources/static),
     * non esiste più un HomeController che "renderizzi" una pagina: serve
     * solo dire a Spring quale file statico servire per "/".
     *
     * Senza questo, GET "/" non ha nessun handler: Spring Security lo
     * autorizza (permitAll in SecurityConfiguration), ma DispatcherServlet
     * non trova nulla da servire — da qui il redirect "fantasma" a /login
     * visto anche per utenti già loggati.
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/index.html");
    }
}