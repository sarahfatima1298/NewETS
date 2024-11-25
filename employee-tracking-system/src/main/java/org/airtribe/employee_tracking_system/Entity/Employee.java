package org.airtribe.employee_tracking_system.Entity;

import java.util.Collection;
import java.util.List;

import org.antlr.v4.runtime.misc.NotNull;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Employee {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NonNull
	@Column(unique = true)
	private String email;

	@NonNull
	private String firstName;

	@NonNull
	private String lastName;

	@Enumerated(EnumType.STRING)
	private Role role; // e.g., ADMIN, MANAGER, EMPLOYEE

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "department_id", nullable = true) // Add nullable=true if no department is required
	private Department department;

	@ManyToMany
	@JoinTable(
			name = "employee_project",
			joinColumns = @JoinColumn(name = "employee_id"),
			inverseJoinColumns = @JoinColumn(name = "project_id")
	)
	private List<Project> projects;

	public Employee() {
	}

	public Employee(Long id, String firstName, String lastName, String email, Role role, Department department, List<Project> projects) {
		this.id = id;
		this.lastName = lastName;
		this.firstName = firstName;
		this.email = email;
		this.role = Role.EMPLOYEE;
		this.department = department;
		this.projects = projects;
	}

	public Collection<String> getRoles() {
		return role != null ? List.of(role.name()) : List.of();
	}

	public String getName() {
		return null;
	}
}

