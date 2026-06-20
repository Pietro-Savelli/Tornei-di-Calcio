package it.uniroma3.torneidicalcio.authentication;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

//per caricamento
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static it.uniroma3.torneidicalcio.model.Credentials.ADMIN_ROLE;

import javax.sql.DataSource;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final DataSource dataSource;

    public SecurityConfiguration(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);
        manager.setUsersByUsernameQuery(
                "SELECT username, password, 1 as enabled FROM credentials WHERE username=?");
        manager.setAuthoritiesByUsernameQuery(
                "SELECT username, role FROM credentials WHERE username=?");
        return manager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(
                "http://localhost:5173",  // dev Vite
                "https://tornei-di-calcio-production.up.railway.app"
        ));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);
        return source;
    }

    @Bean
    protected SecurityFilterChain configure(final HttpSecurity httpSecurity) throws Exception {

        httpSecurity.authorizeHttpRequests(authorize -> {
            // PUBBLICO
            authorize.requestMatchers(HttpMethod.GET, "/", "/index", "/css/**", "/images/**", "/favicon.ico").permitAll();
            // Asset statici della SPA React (build Vite) + API della Home
            authorize.requestMatchers(HttpMethod.GET, "/app/**", "/assets/**", "/js/**", "/api/home").permitAll();
            authorize.requestMatchers("/login", "/register").permitAll();
            // API preferiti (autenticate)
            authorize.requestMatchers("/api/tornei/*/preferito", "/api/utente/preferiti").authenticated();

            //TORNEI SQUADRE GIOCATORI
            authorize.requestMatchers(HttpMethod.GET, "/tornei/**", "/squadre/**", "/giocatori/**").permitAll();

            // UTENTI REGISTRATI
            authorize.requestMatchers("/partite/*/commenti/**").authenticated();

            // ADMIN
            // Tutte le operazioni di gestione
            authorize.requestMatchers("/admin/**").hasAnyAuthority(ADMIN_ROLE);

            authorize.anyRequest().authenticated();
        });

        httpSecurity.formLogin(form -> {
            form.loginPage("/login").permitAll();
            form.defaultSuccessUrl("/", true);
            form.failureUrl("/login?error=true");
        });

        httpSecurity.logout(logout -> {
            logout.logoutUrl("/logout");
            logout.logoutSuccessUrl("/");
            logout.invalidateHttpSession(true);
            logout.deleteCookies("JSESSIONID");
            logout.clearAuthentication(true);
            logout.permitAll();
        });

        // API REST: disabilita CSRF per le chiamate AJAX da React (stesso dominio)
        httpSecurity.csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"));

        httpSecurity.cors(cors -> cors.configurationSource(corsConfigurationSource()));
        return httpSecurity.build();
    }
}