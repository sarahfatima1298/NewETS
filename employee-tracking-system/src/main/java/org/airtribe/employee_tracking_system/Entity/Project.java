package org.airtribe.employee_tracking_system.Entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Project {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NonNull
	private String name;

	@NonNull
	private Double budget;

	@ManyToMany(mappedBy = "projects")
	@JsonBackReference
	private List<Employee> employees;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "department_id")
	@JsonBackReference
	private Department department;

	public Project() {}

	public Project(Long id, List<Employee> employees, Double budget, String name, Department department) {
		this.id = id;
		this.employees = employees;
		this.budget = budget;
		this.name = name;
		this.department = department;
	}
}

