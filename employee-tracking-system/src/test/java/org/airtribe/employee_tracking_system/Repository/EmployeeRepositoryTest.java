package org.airtribe.employee_tracking_system.Repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.airtribe.employee_tracking_system.Entity.Employee;
import org.airtribe.employee_tracking_system.Entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.test.context.support.WithMockUser;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EmployeeRepositoryTest {

	@Autowired
	private EmployeeRepository repository;

	@WithMockUser(username = "admin", roles = {"ADMIN"})
	@Test
	void testSaveAndFindAll() {
		Employee employee = new Employee(null, "Jane", "Doe", "jane.doe@example.com", Role.EMPLOYEE, null, null);
		repository.save(employee);

		List<Employee> employees = repository.findAll();
		assertEquals(1, employees.size());
		assertEquals("Jane", employees.get(0).getFirstName());
	}
}
