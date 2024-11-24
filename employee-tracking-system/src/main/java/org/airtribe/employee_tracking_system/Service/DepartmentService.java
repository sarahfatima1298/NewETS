package org.airtribe.employee_tracking_system.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.airtribe.employee_tracking_system.Entity.Department;
import org.airtribe.employee_tracking_system.Entity.Project;
import org.airtribe.employee_tracking_system.Error.ResourceNotFoundException;
import org.airtribe.employee_tracking_system.Repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

@Service
public class DepartmentService {

	@Autowired
	private DepartmentRepository repository;

	// Cache the result of this method with a cache name "departments" and the key as "all"
	@Cacheable(value = "departments", key = "'all'")
	public List<Department> getAll() {
		return repository.findAll();
	}

	// Cache the result of this method with a cache name "departments" and the key as department id
	@Cacheable(value = "departments", key = "#id")
	public Department getById(Long id) {
		return repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));
	}

	// Cache put: Updates the cache after saving the department
	@CachePut(value = "departments", key = "#department.id")
	public Department save(Department department) {
		return repository.save(department);
	}

	// Cache put: Updates the cache after updating the department
	@CachePut(value = "departments", key = "#id")
	public Department update(Long id, Department department) {
		Department existing = getById(id);
		existing.setName(department.getName());
		return repository.save(existing);
	}

	// Cache evict: Remove the department from the cache after it is deleted
	@CacheEvict(value = "departments", key = "#id")
	public void delete(Long id) {
		repository.deleteById(id);
	}

	// Cache the list of projects for a specific department
	@Cacheable(value = "projects", key = "#departmentId")
	public List<Project> getProjectsByDepartment(Long departmentId) {
		Department department = getById(departmentId);
		return department.getEmployees().stream()
				.flatMap(employee -> employee.getProjects().stream())
				.distinct()
				.collect(Collectors.toList());
	}
}
