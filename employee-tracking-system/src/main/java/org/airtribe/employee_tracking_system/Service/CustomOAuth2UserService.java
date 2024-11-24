package org.airtribe.employee_tracking_system.Service;

import org.airtribe.employee_tracking_system.Entity.Employee;
import org.airtribe.employee_tracking_system.Entity.Role;
import org.airtribe.employee_tracking_system.Repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {


	@Autowired
	private EmployeeRepository employeeRepository;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) {
		// Load the user details from Google's OAuth2 API
		OAuth2User oauth2User = super.loadUser(userRequest);

		Map<String, Object> attributes = oauth2User.getAttributes();
		String email = (String) attributes.get("email");

		if (email == null) {
			throw new IllegalArgumentException("Email not provided by Google.");
		}

		String firstName = (String) attributes.getOrDefault("given_name", "Unknown");
		String lastName = (String) attributes.getOrDefault("family_name", "Unknown");

		String tokenValue = userRequest.getAccessToken().getTokenValue();
		System.out.println("JWT Token: " + tokenValue);

		// Extract ID Token from the OAuth2UserRequest (if available)
		String idToken = userRequest.getAdditionalParameters().get("id_token") != null
				? userRequest.getAdditionalParameters().get("id_token").toString()
				: null;

		if (idToken != null) {
			System.out.println("Bearer " + idToken);
			// Use the ID token if further validation or claims extraction is required
		} else {
			System.out.println("ID Token not available in the response.");
		}

		// Check if the employee exists in the database
		Employee employee = employeeRepository.findByEmail(email).orElseGet(() -> {
			Employee newEmployee = new Employee();
			newEmployee.setEmail(email);
			newEmployee.setFirstName(firstName);
			newEmployee.setLastName(lastName);

			// Assign the default EMPLOYEE role
			newEmployee.setRole(Role.EMPLOYEE);

			// Assign other roles based on first or last name
			// Example: Assign ADMIN role if first name is "Admin"
			if (firstName.equalsIgnoreCase("Admin")) {
				newEmployee.setRole(Role.ADMIN); // Overwrites any default role
			}
			// Example: Assign MANAGER role if last name is "Manager"
			if (lastName.equalsIgnoreCase("Unknown")) {
				newEmployee.setRole(Role.MANAGER); // Overwrites previous role
			}

			return employeeRepository.save(newEmployee);
		});

		// Map the single role to a GrantedAuthority for Spring Security
		SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + employee.getRole().name());

		return new DefaultOAuth2User(List.of(authority), attributes, "email");
	}
}