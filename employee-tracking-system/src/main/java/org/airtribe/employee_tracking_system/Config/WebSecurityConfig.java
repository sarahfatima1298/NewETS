package org.airtribe.employee_tracking_system.Config;

import java.util.List;
import java.util.stream.Collectors;

import org.airtribe.employee_tracking_system.Service.RoleService; // Service to fetch roles from DB
import org.airtribe.employee_tracking_system.Service.CustomOAuth2UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

	private final CustomOAuth2UserService customOAuth2UserService;
	private final RoleService roleService; // Inject RoleService for DB-based role fetching

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		System.out.println("Configuring http filterChain");

		http
				.authorizeHttpRequests((authz) -> authz
						.requestMatchers("/hello").permitAll()
						.requestMatchers("/api/hello").hasRole("ADMIN")
						.requestMatchers("/abcde").hasRole("MANAGER")
						.requestMatchers("/employees/**").hasRole("ADMIN")
						.requestMatchers("/departments/**", "/projects/**").hasAnyRole("ADMIN", "MANAGER")
						.anyRequest().authenticated()
				)
				.oauth2Login(oauth2 -> oauth2
						.userInfoEndpoint(userInfo -> userInfo
								.userService(customOAuth2UserService)
						)
						.defaultSuccessUrl("/", true)
						.failureUrl("/login?error")
				)
				.oauth2Client(Customizer.withDefaults())
				.oauth2ResourceServer(oauth2 -> oauth2
						.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
				)

				.csrf(csrf -> csrf.disable());

		return http.build();
	}

	@Bean
	public JwtAuthenticationConverter jwtAuthenticationConverter() {
		JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();

		// Convert JWT claims to authorities
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
			// Extract the user's email or subject (sub) from the JWT
			String email = jwt.getClaimAsString("email"); // Adjust claim key if needed
			if (email == null) {
				throw new RuntimeException("Email claim is missing in the JWT.");
			}

			// Fetch roles from the database using the email
			List<String> roles = roleService.getRolesByEmail(email);

			if (roles == null || roles.isEmpty()) {
				throw new RuntimeException("No roles found for user: " + email);
			}

			// Map roles into SimpleGrantedAuthority list, prefixing with ROLE_
			return roles.stream()
					.map(role -> new SimpleGrantedAuthority("ROLE_" + role))
					.collect(Collectors.toList());
		});

		return jwtAuthenticationConverter;
	}
}
