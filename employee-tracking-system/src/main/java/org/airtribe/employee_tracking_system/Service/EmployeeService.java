package org.airtribe.employee_tracking_system.Service;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.Predicate;

import org.airtribe.employee_tracking_system.Entity.Employee;
import org.airtribe.employee_tracking_system.Error.ResourceNotFoundException;
import org.airtribe.employee_tracking_system.Repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {
	@Autowired
	private EmployeeRepository repository;

	@Cacheable(value = "employees")
	public List<Employee> getAll() {
		return repository.findAll();
	}

	@Cacheable(value = "employee", key = "#id")
	public Employee getById(Long id) {
		return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
	}

	@CachePut(value = "employee", key = "#employee.id")
	public Employee save(Employee employee) {
		return repository.save(employee);
	}

	@CachePut(value = "employee", key = "#id")
	public Employee update(Long id, Employee employee) {
		Employee existing = getById(id);
		existing.setFirstName(employee.getFirstName());
		existing.setLastName(employee.getLastName());
		existing.setEmail(employee.getEmail());
		return repository.save(existing);
	}

	@CacheEvict(value = "employee", key = "#id")
	public void delete(Long id) {
		repository.deleteById(id);
	}

	@Cacheable(value = "employeeSearch", key = "#root.args")
	public List<Employee> search(String firstName, String lastName, String email, Long departmentId) {
		return repository.findAll((root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();

			// Search by first name
			if (firstName != null) {
				predicates.add(cb.like(root.get("firstName"), "%" + firstName + "%"));
			}

			// Search by last name
			if (lastName != null) {
				predicates.add(cb.like(root.get("lastName"), "%" + lastName + "%"));
			}

			// Search by email
			if (email != null) {
				predicates.add(cb.equal(root.get("email"), email));
			}

			// Search by department ID
			if (departmentId != null) {
				predicates.add(cb.equal(root.get("department").get("id"), departmentId));
			}

			// Combine all predicates with 'AND' logic
			return cb.and(predicates.toArray(new Predicate[0]));
		});
	}
}
