package com.example.mobility.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.mobility.MobilityApplication;
import com.example.mobility.exception.ResourceNotFoundException;
import com.example.mobility.model.Employee;
import com.example.mobility.repository.EmployeeRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

@Service
public class EmployeeService {

	Logger logger = LoggerFactory.getLogger(MobilityApplication.class);

	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired
	private EntityManager entityManager;

	@Cacheable(value = "employeeCache", key = "#ids != null ? #ids : 'defaultKey'")
	public List<Employee> getAllEmployees() {
		return employeeRepository.findAll();
	}

	@CircuitBreaker(name = "default", fallbackMethod = "getEmployeeByIdFallback")
	@Cacheable(value = "employeeCache", key = "#id")
	public Optional<Employee> getEmployeeById(Long id) {
		return employeeRepository.findById(id);
	}

	public Employee createEmployee(Employee employee) {
		return employeeRepository.save(employee);
	}

	public List<Employee> createEmployees(@RequestBody List<Employee> employeeList) {
		return employeeRepository.saveAll(employeeList);
	}

	@CacheEvict(value = "employeeCache", allEntries = true)
	public Employee updateEmployee(Long employeeId, Employee employeeDetails) throws ResourceNotFoundException {

		final Employee updatedEmployee = employeeRepository.save(employeeDetails);
		return updatedEmployee;
	}

	@CacheEvict(value = "employeeCache", key = "#id")
	public Map<String, Boolean> deleteEmployee(Long employeeId) throws ResourceNotFoundException {
		Employee employee = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + employeeId));

		employeeRepository.delete(employee);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}

	public Employee updateEmail(Long employeeId, Employee employeeDetails) throws ResourceNotFoundException {
		employeeRepository.updateEmail(employeeId, employeeDetails.getEmailId());

		return null;
	}

	public Optional<Employee> getEmployeeByIdFallback(Long id, NumberFormatException throwable) {
		// You can log the exception if needed
		System.out.println("Fallback executed due to: " + throwable.getMessage());

		logger.info("***************************getEmployeeByIdFallback************************************");

		// Return a default response or log it
		return Optional.empty(); // Return an empty Optional as a fallback response
	}

	@Cacheable(value = "employeeCache", key = "#pageNumber + '-' + #pageSize")
	public List<Employee> getPaginatedResults(int pageNumber, int pageSize) {
		// Calculate the first result to skip
		int firstResult = (pageNumber - 1) * pageSize;

		// Create the typed query
		TypedQuery<Employee> query = entityManager.createQuery("SELECT m FROM Employee m ORDER BY m.id",
				Employee.class);

		// Set the pagination parameters
		query.setFirstResult(firstResult);
		query.setMaxResults(pageSize);

		// Execute the query and return the results
		return query.getResultList();
	}

	@Cacheable(value = "employeeCache", key = "'totalEmployeeCount'")
	public long getTotalEmployeeCount() {
		Query countQuery = entityManager.createQuery("SELECT COUNT(m) FROM Employee m");
		return (long) countQuery.getSingleResult();
	}

	@CachePut(value = "employeeCache", key = "#pageNumber + '-' + #pageSize")
	public List<Employee> refreshPaginatedResults(int pageNumber, int pageSize) {
		int firstResult = (pageNumber - 1) * pageSize;
		TypedQuery<Employee> query = entityManager.createQuery("SELECT m FROM Employee m ORDER BY m.id",
				Employee.class);
		query.setFirstResult(firstResult);
		query.setMaxResults(pageSize);
		return query.getResultList();
	}

}
