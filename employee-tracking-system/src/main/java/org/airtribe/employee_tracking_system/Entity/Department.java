package org.airtribe.employee_tracking_system.Entity;

import java.util.List;

import jakarta.persistence.*;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Department {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NonNull
	@Column(unique = true)
	private String name;

	@NonNull
	private Double budget;

	@OneToMany(mappedBy = "department")
	private List<Employee> employees;

	@OneToMany(mappedBy = "department")
	private List<Project> projects;

	public Department() {
	}

	public Department(Long id, List<Employee> employees, Double budget, String name, List<Project> projects) {
		this.id = id;
		this.employees = employees;
		this.budget = budget;
		this.name = name;
		this.projects = projects;
	}

}

