package com.example.formscreator.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import com.example.formscreator.security.JWTAuthenticationFilter;


import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
    private JWTAuthenticationFilter jwtAuthenticationFilter;
	@Autowired
    private AuthenticationProvider authenticationProvider;
    
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
        .cors(
            cors -> {
                cors
                    .configurationSource(request -> {
                        CorsConfiguration configuration = new CorsConfiguration();
                        configuration.addAllowedOrigin("*"); // Allow requests from any origin
                        configuration.addAllowedMethod("*"); // Allow all HTTP methods
                        configuration.addAllowedHeader("*"); // Allow all headers
                        return configuration;
                    });
            }
        )
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests()
        .antMatchers(
            "/api/v1/auth/login",
            "/api/v1/auth/register",
            "/swagger-ui/**",
            "/swagger-ui/",
            "/v2/api-docs", 
            "/swagger-resources/configuration/ui", 
            "/swagger-resources", 
            "/swagger-resources/configuration/security", 
            "/swagger-ui.html", 
            "/swagger-ui/index.html",
            "/webjars/**",
            "/api/v1/demo",
            "/swagger-ui/swagger-ui.css?v=3.0.0",
            "/swagger-ui/springfox.css?v=3.0.0"
        )
        .permitAll()
        .anyRequest()
        .authenticated()
        .and()
        .sessionManagement(session -> 
            session.sessionCreationPolicy( 
            SessionCreationPolicy.STATELESS))
        .authenticationProvider(authenticationProvider)
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
    }
}
