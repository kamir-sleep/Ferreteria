package com.proyecto.Fer;
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

import com.proyecto.Fer.config.JWTAuthorizationFilter;
import com.proyecto.Fer.model.UsuariosModel;
import com.proyecto.Fer.repository.UsuariosRepo;

@SpringBootApplication
public class TallerApplication {
	@Autowired
    private UsuariosRepo usuariosRepository;

    public static void main(String[] args) {
        SpringApplication.run(TallerApplication.class, args);
        TallerApplication app = new TallerApplication(); 
        UsuariosModel nuevoUsuario = new UsuariosModel();
        nuevoUsuario.setLogin("Kamir");
        nuevoUsuario.setPasswd("kamir");
        nuevoUsuario.setEstado(1);
        app.usuariosRepository.save(nuevoUsuario); 
    }

	
	@Configuration 
	@EnableWebSecurity
	public class WebSecurityConfig {
		@Value("${jwt.secret.key}")
		private String secret;
		
	   @Bean
	   public SecurityFilterChain filterChain(HttpSecurity http) throws Exception { 
	      http.cors().and().csrf().disable()
	      	.addFilterAfter(new JWTAuthorizationFilter(secret), UsernamePasswordAuthenticationFilter.class)
	        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	        .and().authorizeRequests() 
	        .requestMatchers(HttpMethod.POST, "/api/login").permitAll()
	        .anyRequest().authenticated();
	      return http.build();
	   }
	}





}
