package com.example.mobility.client;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.example.mobility.model.Employee;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

public class SpringClientController {

	private static final String GET_EMPLOYEES_ENDPOINT_URL = "http://localhost:8080/api/v1/employees";
	private static final String GET_EMPLOYEE_ENDPOINT_URL = "http://localhost:8080/api/v1/employees/{id}";
	private static final String CREATE_EMPLOYEE_ENDPOINT_URL = "http://localhost:8080/api/v1/employees";
	private static final String UPDATE_EMPLOYEE_ENDPOINT_URL = "http://localhost:8080/api/v1/employees/{id}";
	private static final String DELETE_EMPLOYEE_ENDPOINT_URL = "http://localhost:8080/api/v1/employees/{id}";
	private static RestTemplate restTemplate = new RestTemplate();

	public static void main(String[] args) {
		SpringClientController springRestClient = new SpringClientController();

		// Step1: first create a new employee
		//springRestClient.createEmployee();

		// Step 2: get new created employee from step1
		springRestClient.getEmployeeById("0");

		// Step3: get all employees
		//springRestClient.getEmployees();

		// Step4: Update employee with id = 1
		springRestClient.updateEmployee();

		// Step5: Delete employee with id = 1
		//springRestClient.deleteEmployee();
	}

	private void getEmployees() {

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

		ResponseEntity<String> result = restTemplate.exchange(GET_EMPLOYEES_ENDPOINT_URL, HttpMethod.GET, entity,
				String.class);

		System.out.println(result);
	}
	@CircuitBreaker(name = "exampleCircuitBreaker", fallbackMethod = "getEmployeeByIdFallback")
	private void getEmployeeById(String empId) {

		Map<String, String> params = new HashMap<String, String>();
		params.put("id", empId);

		RestTemplate restTemplate = new RestTemplate();
		Employee result = restTemplate.getForObject(GET_EMPLOYEE_ENDPOINT_URL, Employee.class, params);

		System.out.println(result);
	}

	private void createEmployee() {

		Employee newEmployee = new Employee("admin", "admin", "admin@gmail.com");

		RestTemplate restTemplate = new RestTemplate();
		Employee result = restTemplate.postForObject(CREATE_EMPLOYEE_ENDPOINT_URL, newEmployee, Employee.class);

		System.out.println(result);
	}

	private void updateEmployee() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", "3");
		Employee updatedEmployee = new Employee("user", "system123", "manjutha7645785@gmail.com");
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.put(UPDATE_EMPLOYEE_ENDPOINT_URL, updatedEmployee, params);
	}

	private void deleteEmployee() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", "1");
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.delete(DELETE_EMPLOYEE_ENDPOINT_URL, params);
	}
	

	  // Fallback method for Circuit Breaker
  public String getEmployeeByIdFallback(String empId,Throwable throwable) {
      return "Fallback response due to: " + throwable.getMessage();
  }

}