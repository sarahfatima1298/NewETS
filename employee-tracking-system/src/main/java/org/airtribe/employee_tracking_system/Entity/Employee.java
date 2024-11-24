package org.airtribe.employee_tracking_system.Entity;

import java.util.Collection;
import java.util.List;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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
	private Role role;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "department_id", nullable = true)
	@JsonManagedReference
	private Department department;

	@ManyToMany
	@JoinTable(
			name = "employee_project",
			joinColumns = @JoinColumn(name = "employee_id"),
			inverseJoinColumns = @JoinColumn(name = "project_id")
	)
	@JsonManagedReference
	private List<Project> projects;

	public Employee() {}

	public Employee(Long id, String lastName, String firstName, String email, Role role, Department department, List<Project> projects) {
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
}