package org.airtribe.employee_tracking_system.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.airtribe.employee_tracking_system.Entity.Department;
import org.airtribe.employee_tracking_system.Entity.Employee;
import org.airtribe.employee_tracking_system.Entity.Project;
import org.airtribe.employee_tracking_system.Entity.Role;
import org.airtribe.employee_tracking_system.Error.ResourceNotFoundException;
import org.airtribe.employee_tracking_system.Repository.DepartmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.WithMockUser;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceTest {

	@Mock
	private DepartmentRepository repository;

	@InjectMocks
	private DepartmentService service;

	@WithMockUser(username = "admin", roles = {"ADMIN"})
	@Test
	void testGetByIdSuccess() {
		Department department = new Department(1L, null, 50000.0, "IT", null);
		when(repository.findById(1L)).thenReturn(Optional.of(department));

		Department result = service.getById(1L);

		assertEquals("IT", result.getName());
		verify(repository, times(1)).findById(1L);
	}

	@WithMockUser(username = "admin", roles = {"ADMIN"})
	@Test
	void testGetByIdNotFound() {
		when(repository.findById(1L)).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> service.getById(1L));
		verify(repository, times(1)).findById(1L);
	}

	@Test
	void testGetAll() {
		List<Department> departments = Arrays.asList(
				new Department(1L, null, 50000.0, "IT", null),
				new Department(2L, null, 60000.0, "CS", null)
		);

		when(repository.findAll()).thenReturn(departments);

		List<Department> result = service.getAll();
		assertEquals(2, result.size());
		assertEquals("IT", result.get(0).getName());
		verify(repository, times(1)).findAll();
	}

	@Test
	void testSave() {
		Department department = new Department(null, null, 50000.0, "IT", null);
		Department savedDepartment = new Department(1L, null, 50000.0, "IT", null);

		when(repository.save(department)).thenReturn(savedDepartment);

		Department result = service.save(department);
		assertNotNull(result);
		assertEquals(1L, result.getId());
		assertEquals("IT", result.getName());
		verify(repository, times(1)).save(department);
	}

	@Test
	void testUpdate() {
		Department existing = new Department(1L, null, 50000.0, "IT", null);
		Department updated = new Department(1L, null, 50000.0, "CS", null);

		when(repository.findById(1L)).thenReturn(Optional.of(existing));
		when(repository.save(existing)).thenReturn(updated);

		Department result = service.update(1L, new Department(1L, null, 50000.0, "CS", null));
		assertNotNull(result);
		assertEquals("CS", result.getName());
		verify(repository, times(1)).findById(1L);
		verify(repository, times(1)).save(existing);
	}

	@Test
	void testUpdate_NotFound() {
		when(repository.findById(1L)).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> service.update(1L, new Department()));
		verify(repository, times(1)).findById(1L);
		verify(repository, times(0)).save(any());
	}

	@Test
	void testDelete() {
		doNothing().when(repository).deleteById(1L);

		service.delete(1L);

		verify(repository, times(1)).deleteById(1L);
	}

	@Test
	void testGetProjectsByDepartment() {
		Project project1 = new Project(1L, null, 10000.0, "Project A", null);
		Project project2 = new Project(1L, null, 10000.0, "Project A", null);
		Employee employee1 = new Employee(1L, "John", "Doe", "john.doe@example.com", Role.EMPLOYEE, null, null);
		Employee employee2 = new Employee(1L, "John", "Doe", "john.doe@example.com", Role.EMPLOYEE, null, null);
		Department department = new Department(1L, List.of(employee1, employee2), 50000.0, "Finance", List.of(project1, project2));

		when(repository.findById(1L)).thenReturn(Optional.of(department));

		List<Project> result = service.getProjectsByDepartment(1L);
		assertEquals(2, result.size());
		assertTrue(result.contains(project1));
		assertTrue(result.contains(project2));
		verify(repository, times(1)).findById(1L);
	}

	@Test
	void testGetProjectsByDepartment_NotFound() {
		when(repository.findById(1L)).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> service.getProjectsByDepartment(1L));
		verify(repository, times(1)).findById(1L);
	}
}
