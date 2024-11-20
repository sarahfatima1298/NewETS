package org.airtribe.employee_tracking_system.Config;

import org.airtribe.employee_tracking_system.Service.CustomOAuth2UserService;
import org.airtribe.employee_tracking_system.Service.OAuth2UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

//	@Autowired
	private final OAuth2UserService oAuth2UserService;

	private final CustomOAuth2UserService customOAuth2UserService;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		System.out.println("Configuring http filterChain");
		http
				.authorizeHttpRequests((authz) -> authz
						.requestMatchers("/hello").permitAll()  // Permit access to /abcde without authentication
						.anyRequest().authenticated()  // Require authentication for other requests
				)
				.oauth2Login(oauth2 -> oauth2
						.userInfoEndpoint(userInfo -> userInfo
								.userService(customOAuth2UserService)  // Custom OAuth2 user service
						)
						.defaultSuccessUrl("/", true)
						.failureUrl("/login?error")  // Redirect to login page on failure
				)
				.oauth2Client(Customizer.withDefaults())  // Default OAuth2 client configurations
				.csrf(csrf -> csrf.disable());  // Disable CSRF if necessary

		return http.build();

//		http
//				.authorizeHttpRequests(authorize -> authorize
//						.anyRequest()
//						.authenticated()
//				).oauth2Login(oauth2 -> oauth2
//						.userInfoEndpoint(infoEndpoint ->
//								infoEndpoint.userService(customOAuth2UserService)));
//		return http.build();
	}
}

