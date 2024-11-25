package org.airtribe.employee_tracking_system.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.airtribe.employee_tracking_system.Entity.Employee;
import org.airtribe.employee_tracking_system.Entity.Role;
import org.airtribe.employee_tracking_system.Error.ResourceNotFoundException;
import org.airtribe.employee_tracking_system.Repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.test.context.support.WithMockUser;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

	@Mock
	private EmployeeRepository repository;

	@InjectMocks
	private EmployeeService service;

	@WithMockUser(username = "admin", roles = {"ADMIN"})
	@Test
	void testGetByIdSuccess() {
		Employee employee = new Employee(1L, "John", "Doe", "john.doe@example.com", Role.EMPLOYEE, null, null);
		when(repository.findById(1L)).thenReturn(Optional.of(employee));

		Employee result = service.getById(1L);

		assertEquals("John", result.getFirstName());
		verify(repository, times(1)).findById(1L);
	}

	@WithMockUser(username = "admin", roles = {"ADMIN"})
	@Test
	void testGetByIdNotFound() {
		when(repository.findById(1L)).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> service.getById(1L));
		verify(repository, times(1)).findById(1L);
	}

	@WithMockUser(username = "admin", roles = {"ADMIN"})
	@Test
	void testGetAll() {
		List<Employee> employees = Arrays.asList(
				new Employee(1L, "John", "Doe", "john.doe@example.com", Role.EMPLOYEE, null, null),
				new Employee(2L, "Jane", "Doe", "jane.doe@example.com", Role.EMPLOYEE, null, null)
		);

		when(repository.findAll()).thenReturn(employees);

		List<Employee> result = service.getAll();
		assertEquals(2, result.size());
		assertEquals("John", result.get(0).getFirstName());
		verify(repository, times(1)).findAll();
	}

	@WithMockUser(username = "admin", roles = {"ADMIN"})
	@Test
	void testSave() {
		Employee employee = new Employee(1L, "John", "Doe", "john.doe@example.com", Role.EMPLOYEE, null, null);
		Employee savedEmployee = new Employee(2L, "Jane", "Doe", "jane.doe@example.com", Role.EMPLOYEE, null, null);

		when(repository.save(employee)).thenReturn(savedEmployee);

		Employee result = service.save(employee);
		assertNotNull(result);
		assertEquals(2L, result.getId());
		assertEquals("Jane", result.getFirstName());
		verify(repository, times(1)).save(employee);
	}

	@WithMockUser(username = "admin", roles = {"ADMIN"})
	@Test
	void testUpdate() {
		Employee employee = new Employee(1L, "John", "Doe", "john.doe@example.com", Role.EMPLOYEE, null, null);
		Employee updatedEmployee = new Employee(2L, "Jane", "Doe", "jane.doe@example.com", Role.EMPLOYEE, null, null);

		when(repository.findById(1L)).thenReturn(Optional.of(employee));
		when(repository.save(employee)).thenReturn(updatedEmployee);

		Employee result = service.update(1L, new Employee(1L, "John", "Doe", "john.doe@example.com", Role.EMPLOYEE, null, null));
		assertNotNull(result);
		assertEquals("Jane", result.getFirstName());
		verify(repository, times(1)).findById(1L);
		verify(repository, times(1)).save(employee);
	}

	@WithMockUser(username = "admin", roles = {"ADMIN"})
	@Test
	void testDelete() {
		doNothing().when(repository).deleteById(1L);

		service.delete(1L);

		verify(repository, times(1)).deleteById(1L);
	}

	@WithMockUser(username = "admin", roles = {"ADMIN"})
	@Test
	void testSearchWithoutPaths() {
		// Mock the repository behavior
		when(repository.findAll(any(Specification.class))).thenAnswer((Answer<List<Employee>>) invocation -> {
			// Simulating the search result based on the input
			List<Employee> employees = Arrays.asList(
					new Employee(1L, "John", "Doe", "john.doe@example.com", Role.EMPLOYEE, null, null),
					new Employee(2L, "John", "Doe", "john.doe@example.com", Role.EMPLOYEE, null, null)
			);
			// You can mock your filtering logic here if necessary
			return employees;
		});

		// Call the search method
		List<Employee> result = service.search("John", "Doe", "john.doe@example.com", 1L);

		// Verify the results
		assertNotNull(result);
		assertEquals(2, result.size());
		assertEquals("John", result.get(0).getFirstName());
		assertEquals("Doe", result.get(0).getLastName());
		assertEquals("john.doe@example.com", result.get(0).getEmail());

		// Ensure the repository method was called
		verify(repository, times(1)).findAll(any(Specification.class));
	}


}
