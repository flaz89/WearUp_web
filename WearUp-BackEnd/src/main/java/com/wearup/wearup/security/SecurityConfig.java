package com.wearup.wearup.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity 
public class SecurityConfig {
	@Autowired
	JWTAuthFilter jwtFilter;
	@Autowired
	CorsFilter corsFilter;

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		// http.cors(c -> c.disable());

		// ************************* CORS (CROSS-ORIGIN RESOURCE SHARING)
		// **************************

		// I browser di default adottano la Same Origin Policy, una politica stringente
		// che non permette
		// richieste http su origin differenti da quella del frontend

		// ************ Esempi di origin differenti ***************
		// http://localhost:3000 e http://localhost:3001 sono 2 diverse origin perché
		// hanno porta diversa
		// https://fe.com e https://be.com sono 2 diverse origin perché sono 2 domini
		// diversi
		// https://domain.com e http://domain.com sono 2 diverse origin perché hanno
		// protocolli diversi

		// Quello che possiamo fare è configurare il backend in maniera che istruisca il
		// frontend affinchè sia più permissivo. Ciò significa configurare CORS per
		// "rilassare" questa stringente politica

		http.csrf(c -> c.disable());

		http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		http.authorizeHttpRequests(auth -> auth.requestMatchers("/users/**").authenticated());
		http.authorizeHttpRequests(auth -> auth.requestMatchers("/auth/**").permitAll());

		http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
		http.addFilterBefore(corsFilter, JWTAuthFilter.class);

		return http.build();
	}

	@Bean
	PasswordEncoder encoder() {
		return new BCryptPasswordEncoder(11);
	}

}
