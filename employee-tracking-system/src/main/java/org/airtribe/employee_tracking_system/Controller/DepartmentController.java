package org.airtribe.employee_tracking_system.Controller;

import java.util.List;

import org.airtribe.employee_tracking_system.Entity.Department;
import org.airtribe.employee_tracking_system.Entity.Project;
import org.airtribe.employee_tracking_system.Service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/departments")
public class DepartmentController {
	@Autowired
	private DepartmentService service;

	@PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
	@GetMapping
	public List<Department> getAllDepartments() {
		return service.getAll();
	}

	@PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
	@GetMapping("/{id}")
	public Department getDepartmentById(@PathVariable Long id) {
		return service.getById(id);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	public Department addDepartment(@RequestBody Department department) {
		return service.save(department);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{id}")
	public Department updateDepartment(@PathVariable Long id, @RequestBody Department department) {
		return service.update(id, department);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id}")
	public void deleteDepartment(@PathVariable Long id) {
		service.delete(id);
	}

	@PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
	@GetMapping("/{id}/projects")
	public List<Project> getProjectsByDepartment(@PathVariable Long id) {
		return service.getProjectsByDepartment(id);
	}
}


