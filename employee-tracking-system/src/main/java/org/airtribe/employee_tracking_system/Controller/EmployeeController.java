package org.airtribe.employee_tracking_system.Controller;

import java.util.List;

import org.airtribe.employee_tracking_system.Entity.Employee;
import org.airtribe.employee_tracking_system.Service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/employees")
public class EmployeeController {
	@Autowired
	private EmployeeService service;

	@PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
	@GetMapping
	public List<Employee> getAllEmployees() {
		return service.getAll();
	}

	@PreAuthorize("hasRole('ADMIN') or (hasRole('MANAGER') and #id == principal.departmentId) or #id == principal.id")
	@GetMapping("/{id}")
	public Employee getEmployeeById(@PathVariable Long id) {
		return service.getById(id);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	public Employee addEmployee(@RequestBody Employee employee) {
		return service.save(employee);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{id}")
	public Employee updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {
		return service.update(id, employee);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id}")
	public void deleteEmployee(@PathVariable Long id) {
		service.delete(id);
	}

	@PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
	@GetMapping("/search")
	public List<Employee> searchEmployees(
			@RequestParam(required = false) String firstName,
			@RequestParam(required = false) String lastName,
			@RequestParam(required = false) String email,
			@RequestParam(required = false) Long departmentId) {
		return service.search(firstName, lastName, email, departmentId);
	}
}


