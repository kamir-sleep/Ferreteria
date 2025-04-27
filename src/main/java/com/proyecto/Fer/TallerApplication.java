package com.proyecto.Fer;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.proyecto.Fer.config.JWTAuthorizationFilter;
import com.proyecto.Fer.model.UsuariosModel;
import com.proyecto.Fer.repository.UsuariosRepo;

@SpringBootApplication
public class TallerApplication {
    public static void main(String[] args) {
        SpringApplication.run(TallerApplication.class, args);
    }

	
	@Configuration 
@EnableWebSecurity
public class WebSecurityConfig {
    @Value("${jwt.secret.key}")
    private String secret;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception { 
        http
            .cors().and() // 🔹 Habilitar CORS en la configuración de seguridad
            .csrf().disable()
            .addFilterAfter(new JWTAuthorizationFilter(secret), UsernamePasswordAuthenticationFilter.class)
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeHttpRequests() 
            .requestMatchers(HttpMethod.POST, "/api/login").permitAll()
            .requestMatchers(HttpMethod.GET, "/uploads/**").permitAll() //Aqui mostrar la imagen permiso
            .requestMatchers(HttpMethod.GET, "/api/archivos/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/uploads/images/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/files/**").permitAll()
            .anyRequest().authenticated();
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200")); // 🔹 Permitir Angular
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true); 

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}






}
