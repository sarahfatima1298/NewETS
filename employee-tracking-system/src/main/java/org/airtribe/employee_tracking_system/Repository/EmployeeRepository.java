package org.airtribe.employee_tracking_system.Repository;

import org.airtribe.employee_tracking_system.Entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {
	Optional<Employee> findByEmail(String email);  // Find employee by email

	//Optional<Employee> findByUsername(String username);
}