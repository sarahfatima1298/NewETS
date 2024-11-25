package org.airtribe.employee_tracking_system.Controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.airtribe.employee_tracking_system.Entity.Department;
import org.airtribe.employee_tracking_system.Entity.Project;
import org.airtribe.employee_tracking_system.Service.DepartmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(DepartmentController.class)
class DepartmentControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private DepartmentService service;

	@WithMockUser(roles = {"ADMIN"})
	@Test
	void testGetAllDepartmentsWithAdminRole() throws Exception {
		// Mock the service response
		Department department = new Department(1L, null, 50000.0, "HR", null);
		when(service.getAll()).thenReturn(Arrays.asList(department));

		// Simulate OAuth2 login with admin role
		mockMvc.perform(get("/departments")
						.with(oauth2Login().authorities(() -> "ROLE_ADMIN")))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id").value(1))
				.andExpect(jsonPath("$[0].name").value("HR"));
	}

	@Test
	@WithMockUser(roles = {"ADMIN", "MANAGER"})
	void testGetDepartmentById() throws Exception {
		Department department = new Department(1L, null, 50000.0, "HR", null);
		when(service.getById(1L)).thenReturn(department);

		mockMvc.perform(get("/departments/1")
						.with(oauth2Login().authorities(() -> "ROLE_ADMIN")))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("HR"));
	}

	@WithMockUser(username = "admin", roles = {"ADMIN"})
	@Test
	void testAddDepartment() throws Exception {
		Department department = new Department(1L, null, 50000.0, "Finance", null);
		when(service.save(any(Department.class))).thenReturn(department);

		String jsonPayload = """
				{
				  "name": "Finance",
				  "budget": 50000
				}
				""";

		mockMvc.perform(post("http://localhost:8088/departments")
						.with(oauth2Login().authorities(() -> "ROLE_ADMIN"))
						.with(csrf())
						.contentType(MediaType.APPLICATION_JSON)
						.content(jsonPayload))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.name").value("Finance"));
	}

	@Test
	@WithMockUser(roles = {"ADMIN"})
	void testUpdateDepartment() throws Exception {
		Department updatedDepartment = new Department(1L, null, 50000.0, "Updated Name", null);
		when(service.update(eq(1L), any(Department.class))).thenReturn(updatedDepartment);

		mockMvc.perform(put("/departments/1")
						.with(oauth2Login().authorities(() -> "ROLE_ADMIN"))
						.with(csrf()) // Required for security testing with CSRF protection enabled
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"name\":\"Updated Name\"}"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.name").value("Updated Name"));
	}

	@Test
	@WithMockUser(roles = {"ADMIN"})
	void testDeleteDepartment() throws Exception {
		doNothing().when(service).delete(1L);

		mockMvc.perform(delete("/departments/1")
						.with(oauth2Login().authorities(() -> "ROLE_ADMIN"))
						.with(csrf()))

				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(roles = {"ADMIN", "MANAGER"})
	void testGetProjectsByDepartment() throws Exception {
		List<Project> projects = Arrays.asList(new Project(1L, null, 20000.0, "Project B", null));
		when(service.getProjectsByDepartment(1L)).thenReturn(projects);

		mockMvc.perform(get("/departments/1/projects")
						.with(oauth2Login().authorities(() -> "ROLE_ADMIN")))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(1))
				.andExpect(jsonPath("$[0].name").value("Project B"));

	}
}
