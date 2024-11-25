package org.airtribe.employee_tracking_system.Controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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

import org.airtribe.employee_tracking_system.Entity.Employee;
import org.airtribe.employee_tracking_system.Entity.Role;
import org.airtribe.employee_tracking_system.Service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private EmployeeService service;

	@WithMockUser(username = "admin", roles = {"ADMIN"})
	@Test
	void testGetAllEmployees() throws Exception {
		Employee employee = new Employee(1L, "John", "Doe", "john.doe@example.com", Role.EMPLOYEE, null, null);
		Mockito.when(service.getAll()).thenReturn(Arrays.asList(employee));

		mockMvc.perform(get("/employees")
						.with(oauth2Login().authorities(() -> "ROLE_ADMIN")))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id").value(1))
				.andExpect(jsonPath("$[0].firstName").value("John"));
	}

	@WithMockUser(username = "admin", roles = {"ADMIN"})
	@Test
	void testAddEmployee() throws Exception {
		Employee employee = new Employee(1L, "Jane", "Doe", "jane.doe@example.com", Role.EMPLOYEE, null, null);
		Mockito.when(service.save(any(Employee.class))).thenReturn(employee);

		String jsonPayload = """
				{
				  "firstName": "Jane",
				  "lastName": "Doe",
				  "email": "jane.doe@example.com",
				  "role": "EMPLOYEE"
				}
				""";

		mockMvc.perform(post("/employees")
						.with(oauth2Login().authorities(() -> "ROLE_ADMIN"))
						.with(csrf())
						.contentType(MediaType.APPLICATION_JSON)
						.content(jsonPayload))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.firstName").value("Jane"));
	}

	@Test
	@WithMockUser(roles = {"ADMIN", "MANAGER"})
	void testGetEmployeeById() throws Exception {
		Employee employee = new Employee(1L, "Jane", "Doe", "jane.doe@example.com", Role.EMPLOYEE, null, null);
		Mockito.when(service.getById(1L)).thenReturn(employee);

		mockMvc.perform(get("/employees/1")
						.with(oauth2Login().authorities(() -> "ROLE_ADMIN"))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.firstName").value("Jane"));
	}

	@Test
	@WithMockUser(roles = {"ADMIN"})
	void testUpdateEmployee() throws Exception {
		Employee employee = new Employee(1L, "John", "Doe", "jane.doe@example.com", Role.EMPLOYEE, null, null);
		Mockito.when(service.update(eq(1L), any(Employee.class))).thenReturn(employee);

		mockMvc.perform(put("/employees/1")
						.with(oauth2Login().authorities(() -> "ROLE_ADMIN"))
						.with(csrf())
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"firstName\":\"Updated John\",\"lastName\":\"Doe\",\"email\":\"john.doe@example.com\",\"departmentId\":1001}"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.firstName").value("John"));
	}

	@Test
	@WithMockUser(roles = {"ADMIN"})
	void testDeleteEmployee() throws Exception {
		Mockito.doNothing().when(service).delete(1L);

		mockMvc.perform(delete("/employees/1")
						.with(oauth2Login().authorities(() -> "ROLE_ADMIN"))
						.with(csrf())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(roles = {"ADMIN", "MANAGER"})
	void testSearchEmployees() throws Exception {
		List<Employee> employees = Arrays.asList(
				new Employee(1L, "John", "Doe", "jane.doe@example.com", Role.EMPLOYEE, null, null),
				new Employee(1L, "Jane", "Doe", "jane.doe@example.com", Role.EMPLOYEE, null, null)
		);
		Mockito.when(service.search("John", null, null, null)).thenReturn(employees);

		mockMvc.perform(get("/employees/search?firstName=John")
						.with(oauth2Login().authorities(() -> "ROLE_ADMIN"))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.size()").value(2))
				.andExpect(jsonPath("$[0].firstName").value("John"));
	}
}
