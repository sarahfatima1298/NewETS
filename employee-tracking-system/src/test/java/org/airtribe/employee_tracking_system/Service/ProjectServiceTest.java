package org.airtribe.employee_tracking_system.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.airtribe.employee_tracking_system.Entity.Project;
import org.airtribe.employee_tracking_system.Error.ResourceNotFoundException;
import org.airtribe.employee_tracking_system.Repository.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.WithMockUser;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

	@Mock
	private ProjectRepository repository;

	@InjectMocks
	private ProjectService service;

	@WithMockUser(username = "admin", roles = {"ADMIN"})
	@Test
	void testGetByIdSuccess() {
		Project project = new Project(1L, null, 10000.0, "Project A", null);
		when(repository.findById(1L)).thenReturn(Optional.of(project));

		Project result = service.getById(1L);

		assertEquals("Project A", result.getName());
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
		List<Project> projects = Arrays.asList(
				new Project(1L, null, 10000.0, "Project A", null),
				new Project(2L, null, 20000.0, "Project B", null)
		);

		when(repository.findAll()).thenReturn(projects);

		List<Project> result = service.getAll();
		assertEquals(2, result.size());
		assertEquals("Project A", result.get(0).getName());
		verify(repository, times(1)).findAll();
	}

	@WithMockUser(username = "admin", roles = {"ADMIN"})
	@Test
	void testSave() {
		Project project = new Project(null, null, 10000.0, "Project A", null);
		Project savedProject = new Project(1L, null, 10000.0, "Project B", null);

		when(repository.save(project)).thenReturn(savedProject);

		Project result = service.save(project);
		assertNotNull(result);
		assertEquals(1L, result.getId());
		assertEquals("Project B", result.getName());
		verify(repository, times(1)).save(project);
	}

	@WithMockUser(username = "admin", roles = {"ADMIN"})
	@Test
	void testUpdate() {
		Project existing = new Project(1L, null, 10000.0, "Project A", null);
		Project updated = new Project(1L, null, 10000.0, "Project B", null);

		when(repository.findById(1L)).thenReturn(Optional.of(existing));
		when(repository.save(existing)).thenReturn(updated);

		Project result = service.update(1L, new Project(1L, null, 10000.0, "Project B", null));
		assertNotNull(result);
		assertEquals("Project B", result.getName());
		verify(repository, times(1)).findById(1L);
		verify(repository, times(1)).save(existing);
	}

	@WithMockUser(username = "admin", roles = {"ADMIN"})
	@Test
	void testUpdate_NotFound() {
		when(repository.findById(1L)).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> service.update(1L, new Project()));
		verify(repository, times(1)).findById(1L);
		verify(repository, times(0)).save(any());
	}

	@WithMockUser(username = "admin", roles = {"ADMIN"})
	@Test
	void testDelete() {
		doNothing().when(repository).deleteById(1L);

		service.delete(1L);

		verify(repository, times(1)).deleteById(1L);
	}
}
