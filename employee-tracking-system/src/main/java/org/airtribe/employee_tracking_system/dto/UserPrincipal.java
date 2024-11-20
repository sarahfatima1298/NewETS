package org.airtribe.employee_tracking_system.dto;

import org.airtribe.employee_tracking_system.Entity.Employee;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Map;

public class UserPrincipal extends User implements UserDetails {

	private final Employee employee;
	private final Map<String, Object> attributes;

	public UserPrincipal(Employee employee, Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes) {
		super(employee.getEmail(), "", authorities); // Using email as the username and assuming password is not needed for OAuth
		this.employee = employee;
		this.attributes = attributes;
	}

	public static UserPrincipal create(Employee employee, Map<String, Object> attributes) {
		return new UserPrincipal(employee, authoritiesFromRoles(employee.getRoles()), attributes);
	}

	private static Collection<? extends GrantedAuthority> authoritiesFromRoles(Collection<String> roles) {
		// Convert roles (if any) into GrantedAuthorities (e.g., ROLE_USER, ROLE_ADMIN)
		return roles.stream()
				.map(role -> (GrantedAuthority) () -> role)
				.toList();
	}

	public Employee getEmployee() {
		return employee;
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	@Override
	public String getUsername() {
		return employee.getEmail(); // Custom username (email in this case)
	}

	@Override
	public String getPassword() {
		return null; // No password for OAuth2
	}

	@Override
	public boolean isAccountNonExpired() {
		return true; // Adjust logic based on your needs
	}

	@Override
	public boolean isAccountNonLocked() {
		return true; // Adjust logic based on your needs
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true; // Adjust logic based on your needs
	}

	@Override
	public boolean isEnabled() {
		return true; // Adjust logic based on your needs
	}
}

