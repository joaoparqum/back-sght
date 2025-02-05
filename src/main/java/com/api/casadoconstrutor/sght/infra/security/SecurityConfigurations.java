package com.api.casadoconstrutor.sght.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

    @Autowired
    SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                //.csrf(csrf -> csrf.disable())
                .csrf(AbstractHttpConfigurer::disable)
                .cors().and()// Habilita CORS global
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/api/sght/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/sght/auth/register").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/sght/solicitacoes").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/sght/solicitacoes").hasRole("USER")
                        .requestMatchers(HttpMethod.PATCH, "/api/sght/solicitacoes/*/status").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/sght/solicitacoes/*").permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/api/sght/solicitacoes/*").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/sght/solicitacoes/*").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/sght/solicitacoes/nome/*").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/sght/solicitacoes/motivo/*").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/sght/horas").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/sght/horas/*").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/sght/horas").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/sght/horas/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/sght/horas").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/sght/horas").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/sght/comprovante/view/*").permitAll()
                        //requestMatchers(HttpMethod.GET, "/**").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();//faz a senha do usuario ficar encriptada no banco de dados
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://24.144.93.247"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
