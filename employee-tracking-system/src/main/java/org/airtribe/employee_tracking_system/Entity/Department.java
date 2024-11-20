package org.airtribe.employee_tracking_system.Entity;

import java.util.List;

import jakarta.persistence.*;
import jakarta.persistence.Id;

@Entity
public class Department {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

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

	public List<Project> getProjects() {
		return projects;
	}

	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}
}

