package org.airtribe.employee_tracking_system.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.airtribe.employee_tracking_system.Entity.Department;
import org.airtribe.employee_tracking_system.Entity.Project;
import org.airtribe.employee_tracking_system.Error.ResourceNotFoundException;
import org.airtribe.employee_tracking_system.Repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DepartmentService {
	@Autowired
	private DepartmentRepository repository;

	public List<Department> getAll() {
		return repository.findAll();
	}

	public Department getById(Long id) {
		return repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));
	}

	public Department save(Department department) {
		return repository.save(department);
	}

	public Department update(Long id, Department department) {
		Department existing = getById(id);
		existing.setName(department.getName());
		return repository.save(existing);
	}

	public void delete(Long id) {
		repository.deleteById(id);
	}

	public List<Project> getProjectsByDepartment(Long departmentId) {
		Department department = getById(departmentId);
		return department.getProjects();
	}
}
