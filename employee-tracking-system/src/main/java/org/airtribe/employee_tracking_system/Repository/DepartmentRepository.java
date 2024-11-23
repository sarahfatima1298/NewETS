package org.airtribe.employee_tracking_system.Repository;

import org.airtribe.employee_tracking_system.Entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {}
