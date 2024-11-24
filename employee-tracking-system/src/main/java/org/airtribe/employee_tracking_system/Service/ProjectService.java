package org.airtribe.employee_tracking_system.Service;

import java.util.List;

import org.airtribe.employee_tracking_system.Entity.Project;
import org.airtribe.employee_tracking_system.Error.ResourceNotFoundException;
import org.airtribe.employee_tracking_system.Repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

	@Autowired
	private ProjectRepository repository;

	// Cache the result of this method with a cache name "projects" and key as "all"
	@Cacheable(value = "projects", key = "'all'")
	public List<Project> getAll() {
		return repository.findAll();
	}

	// Cache the result of this method with a cache name "projects" and the key as project id
	@Cacheable(value = "projects", key = "#id")
	public Project getById(Long id) {
		return repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));
	}

	// Cache put: Update the cache after saving a project
	@CachePut(value = "projects", key = "#project.id")
	public Project save(Project project) {
		return repository.save(project);
	}

	// Cache put: Update the cache after updating a project
	@CachePut(value = "projects", key = "#id")
	public Project update(Long id, Project project) {
		Project existing = getById(id);
		existing.setName(project.getName());
		existing.setEmployees(project.getEmployees());
		return repository.save(existing);
	}

	// Cache evict: Remove the project from the cache after it is deleted
	@CacheEvict(value = "projects", key = "#id")
	public void delete(Long id) {
		repository.deleteById(id);
	}
}
