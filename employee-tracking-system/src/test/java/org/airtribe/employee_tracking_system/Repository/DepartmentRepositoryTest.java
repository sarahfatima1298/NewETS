package org.airtribe.employee_tracking_system.Repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.airtribe.employee_tracking_system.Entity.Department;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.test.context.support.WithMockUser;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DepartmentRepositoryTest {

	@Autowired
	private DepartmentRepository repository;

	@WithMockUser(username = "admin", roles = {"ADMIN"})
	@Test
	void testSaveAndFindAll() {
		Department department = new Department(null, null, 30000.0, "HR", null);
		repository.save(department);

		List<Department> departments = repository.findAll();
		assertEquals(1, departments.size());
		assertEquals("HR", departments.get(0).getName());
	}
}
