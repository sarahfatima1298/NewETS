package org.airtribe.employee_tracking_system.Entity;

import java.util.List;

import jakarta.persistence.*;

@Entity
public class Project {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private Double budget;

	@ManyToMany(mappedBy = "projects")
	private List<Employee> employees;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "department_id") // Ensure consistent column naming
	private Department department;


	public Project() {
	}

	public Project(Long id, List<Employee> employees, Double budget, String name, Department department) {
		this.id = id;
		this.employees = employees;
		this.budget = budget;
		this.name = name;
		this.department = department;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getBudget() {
		return budget;
	}

	public void setBudget(Double budget) {
		this.budget = budget;
	}

	public List<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}
}

