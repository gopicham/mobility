
/**
 * 
 */
package com.example.mobility.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.mobility.exception.ResourceNotFoundException;
import com.example.mobility.model.Employee;
import com.example.mobility.model.ResponseData;
import com.example.mobility.security.AuthService;
import com.example.mobility.security.JWTAuthResponse;
import com.example.mobility.security.LoginDto;
import com.example.mobility.service.EmployeeService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 
 */
@RestController
@RequestMapping("/api/v1")
@CrossOrigin()
public class MobilityController {

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private AuthService authService;

	@PostMapping("/login")
	public ResponseData authenticate(@RequestBody LoginDto loginDto, HttpServletResponse response) {
		String token = authService.login(loginDto);

		JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();
		jwtAuthResponse.setAccessToken(token);

		return ResponseData.getRespone(jwtAuthResponse);

	}

	@GetMapping("/employees")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseData getAllEmployees(@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer limit, HttpServletResponse response) {
		List<Employee> empList = employeeService.getPaginatedResults(page, limit);
		long totalCount = employeeService.getTotalEmployeeCount();
		return ResponseData.getRespone(empList, totalCount);
	}

	@GetMapping("/employees/{id}")
	public ResponseEntity<Employee> getEmployeeById(@PathVariable(value = "id") Long employeeId,
			HttpServletResponse response) throws ResourceNotFoundException {
		Employee employee = null;
		employee = employeeService.getEmployeeById(employeeId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + employeeId));

		return ResponseEntity.ok().body(employee);
	}

	@PostMapping("/employees")
	public Employee createEmployee(@RequestBody Employee employee) {
		return employeeService.createEmployee(employee);
	}

	@PostMapping("/employees/create")
	public List<Employee> createEmployees(@RequestBody List<Employee> employeeLits, HttpServletResponse response) {
		return employeeService.createEmployees(employeeLits);
	}

	@PutMapping("/employees/{id}")
	public ResponseEntity<Employee> updateEmployee(@PathVariable(value = "id") Long employeeId,
			@RequestBody Employee employeeDetails, HttpServletResponse response) throws ResourceNotFoundException {
		Employee updatedEmployee = employeeService.updateEmployee(employeeId, employeeDetails);
		return ResponseEntity.ok(updatedEmployee);
	}

	@PatchMapping("/employees/uppdate/{id}")
	public ResponseEntity<Employee> updateEmployeeEmail(@PathVariable(value = "id") Long employeeId,
			@RequestBody Employee employeeDetails, HttpServletResponse response) throws ResourceNotFoundException {
		Employee updatedEmployee = employeeService.updateEmail(employeeId, employeeDetails);
		return ResponseEntity.ok(updatedEmployee);
	}

	@PutMapping("/employees/bulk")
	public ResponseEntity<String> updateBuilkEmployees(@RequestBody Map<String, Object> employees)
			throws ResourceNotFoundException {
		ObjectMapper mapper = new ObjectMapper();
		Object data = employees.get("employees");
		List<Employee> employeeList = mapper.convertValue(data, new TypeReference<List<Employee>>() {
		});
		employeeService.updateBuilkEmployees(employeeList);
		return ResponseEntity.ok("updated");
	}

	@DeleteMapping("/employees/{id}")
	public Map<String, Boolean> deleteEmployee(@PathVariable(value = "id") Long employeeId,
			HttpServletResponse response) throws ResourceNotFoundException {
		return employeeService.deleteEmployee(employeeId);
	}
}
