package org.airtribe.employee_tracking_system.Entity;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.*;

@Entity
public class Employee {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String email;

	private String firstName;

	private String lastName;

	@Enumerated(EnumType.STRING)
	private Role role; // e.g., ADMIN, MANAGER, EMPLOYEE

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "department_id") // Ensure consistent column naming
	private Department department;


	@ManyToMany
	@JoinTable(
			name = "employee_project", // Name of the join table
			joinColumns = @JoinColumn(name = "employee_id"), // Foreign key in the join table referencing Employee
			inverseJoinColumns = @JoinColumn(name = "project_id") // Foreign key in the join table referencing Project
	)
	private List<Project> projects;

	public Employee() {
	}

	public Employee(Long id, String lastName, String firstName, String email, Role role, Department department, List<Project> projects) {
		this.id = id;
		this.lastName = lastName;
		this.firstName = firstName;
		this.email = email;
		this.role = Role.EMPLOYEE;
		this.department = department;
		this.projects = projects;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public List<Project> getProjects() {
		return projects;
	}

	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}

	public Collection<String> getRoles() {
		return role != null ? List.of(role.name()) : List.of();
	}
}

