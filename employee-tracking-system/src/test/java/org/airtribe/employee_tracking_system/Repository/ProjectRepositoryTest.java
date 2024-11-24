package org.airtribe.employee_tracking_system.Repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.airtribe.employee_tracking_system.Entity.Project;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.test.context.support.WithMockUser;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProjectRepositoryTest {

	@Autowired
	private ProjectRepository repository;

	@WithMockUser(username = "admin", roles = {"ADMIN"})
	@Test
	void testSaveAndFindAll() {
		Project project = new Project(null, null, 50000.0, "Project A", null);
		repository.save(project);

		List<Project> projects = repository.findAll();
		assertEquals(1, projects.size());
		assertEquals("Project A", projects.get(0).getName());
	}
}
