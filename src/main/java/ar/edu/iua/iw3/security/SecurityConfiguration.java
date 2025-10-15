package ar.edu.iua.iw3.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import ar.edu.iua.iw3.auth.IUserBusiness;
import ar.edu.iua.iw3.auth.custom.CustomAuthenticationManager;
import ar.edu.iua.iw3.auth.filters.JWTAuthorizationFilter;
import ar.edu.iua.iw3.controllers.Constants;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {

	/*
	 * @Bean SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	 * // CORS: https://developer.mozilla.org/es/docs/Web/HTTP/CORS // CSRF:0
	 * https://developer.mozilla.org/es/docs/Glossary/CSRF
	 * http.cors(CorsConfigurer::disable);
	 * http.csrf(AbstractHttpConfigurer::disable); http.authorizeHttpRequests(auth
	 * -> auth .requestMatchers("/**").permitAll() .anyRequest().authenticated() );
	 * return http.build(); }
	 */

	@Bean
	PasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedMethods("*").allowedHeaders("*").allowedOrigins("*");
			}
		};
	}

	@Autowired
	private IUserBusiness userBusiness;

	@Bean
	AuthenticationManager authenticationManager() {
		return new CustomAuthenticationManager(bCryptPasswordEncoder(), userBusiness);
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		// CORS: https://developer.mozilla.org/es/docs/Web/HTTP/CORS

		// CSRF: https://developer.mozilla.org/es/docs/Glossary/CSRF
		http.csrf(AbstractHttpConfigurer::disable);
		http.authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.POST, Constants.URL_LOGIN).permitAll()
				.requestMatchers("/v3/api-docs/**").permitAll().requestMatchers("/swagger-ui.html").permitAll()
				.requestMatchers("/swagger-ui/**").permitAll().requestMatchers("/ui/**").permitAll()
				.requestMatchers("/demo/**").permitAll().anyRequest().authenticated());
		//http.httpBasic(Customizer.withDefaults());
		http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		http.addFilter(new JWTAuthorizationFilter(authenticationManager()));
		return http.build();

	}

}
