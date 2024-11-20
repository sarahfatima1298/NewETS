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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

	private static final Logger logger = LoggerFactory.getLogger(CustomOAuth2UserService.class);

	@Autowired
	private EmployeeRepository employeeRepository;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) {
		// Delegate the user loading to the parent class
		OAuth2User oauth2User = super.loadUser(userRequest);

		// Extract user information from the OAuth2User attributes
		Map<String, Object> attributes = oauth2User.getAttributes();
		String email = (String) attributes.get("email");
		if (email == null) {
			throw new IllegalArgumentException("Email not provided by OAuth2 provider.");
		}

		String firstName = (String) attributes.getOrDefault("given_name", "Unknown");
		String lastName = (String) attributes.getOrDefault("family_name", "Unknown");

		// Check if the employee exists in the database
		Employee employee = employeeRepository.findByEmail(email).orElseGet(() -> {
			Employee newEmployee = new Employee();
			newEmployee.setEmail(email);
			newEmployee.setFirstName(firstName);
			newEmployee.setLastName(lastName);
			newEmployee.setRole(Role.EMPLOYEE); // Default role
			return employeeRepository.save(newEmployee);
		});

		// Retrieve the employee's role
		Role role = employee.getRole();

		// Return OAuth2User with the user's roles and other attributes
		return new DefaultOAuth2User(
				List.of(new SimpleGrantedAuthority("ROLE_" + role.name())),
				attributes,
				"email" // Use 'email' as the principal attribute
		);
	}
}
