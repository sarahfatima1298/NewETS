package org.airtribe.employee_tracking_system.Service;

import java.util.List;

import org.airtribe.employee_tracking_system.Repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleService {

	private final EmployeeRepository employeeRepository;

	public List<String> getRolesByEmail(String email) {
		return employeeRepository.findByEmail(email)
				.map(employee -> List.of(employee.getRole().name())) // Assume a single role, adjust if multiple
				.orElseThrow(() -> new RuntimeException("User not found with email: " + email));
	}
}

