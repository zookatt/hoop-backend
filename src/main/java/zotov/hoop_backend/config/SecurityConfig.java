package zotov.hoop_backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:5173"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        config.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            Environment environment,
            @Value("${api-endpoint}") String apiEndpoint) throws Exception {

        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));

        boolean local = environment.acceptsProfiles(Profiles.of("local"));
        http.authorizeHttpRequests(auth -> {
            auth
                    .requestMatchers("/images/**", "/error").permitAll()
                    .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/api-docs",
                            "/api-docs/**")
                    .permitAll()
                    .requestMatchers(HttpMethod.GET, apiEndpoint + "/incidents", apiEndpoint + "/incidents/*")
                    .permitAll();

            if (local) {
                auth
                        .requestMatchers(HttpMethod.POST, apiEndpoint + "/incidents").permitAll()
                        .requestMatchers(HttpMethod.GET, apiEndpoint + "/users", apiEndpoint + "/user-roles")
                        .permitAll()
                        .requestMatchers(HttpMethod.POST, apiEndpoint + "/users", apiEndpoint + "/user-roles")
                        .permitAll()
                        .requestMatchers(
                                HttpMethod.PUT,
                                apiEndpoint + "/incidents/*",
                                apiEndpoint + "/incidents/*/assignment",
                                apiEndpoint + "/incidents/*/status")
                        .permitAll();
            }

            auth.anyRequest().authenticated();
        });

        if (local) {
            http.csrf(csrf -> csrf
                    .ignoringRequestMatchers(request -> request.getServletPath().startsWith(apiEndpoint + "/incidents")
                            || request.getServletPath().startsWith(apiEndpoint + "/users")
                            || request.getServletPath().startsWith(apiEndpoint + "/user-roles")));
        }
        return http.build();
    }
}
