/**
 * 
 */
package com.example.mobility.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.mobility.model.Employee;

import jakarta.transaction.Transactional;

/**
 * 
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
	@Transactional
	@Modifying
	@Query("UPDATE Employee e SET e.emailId = :emailId WHERE e.id = :id")
	void updateEmail(Long id, String emailId);
}