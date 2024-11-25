package org.airtribe.employee_tracking_system.Controller;

import java.util.List;

import org.airtribe.employee_tracking_system.Entity.Project;
import org.airtribe.employee_tracking_system.Service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/projects")
public class ProjectController {
	@Autowired
	private ProjectService service;

	@PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
	@GetMapping
	public List<Project> getAllProjects() {
		return service.getAll();
	}

	@PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
	@GetMapping("/{id}")
	public Project getProjectById(@PathVariable Long id) {
		return service.getById(id);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	public Project addProject(@RequestBody Project project) {
		return service.save(project);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{id}")
	public Project updateProject(@PathVariable Long id, @RequestBody Project project) {
		return service.update(id, project);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id}")
	public void deleteProject(@PathVariable Long id) {
		service.delete(id);
	}
}
