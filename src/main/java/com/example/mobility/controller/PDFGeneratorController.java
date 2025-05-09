package com.example.mobility.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mobility.model.Employee;
import com.example.mobility.repository.EmployeeRepository;
import com.example.mobility.service.PdfGeneratorService;

@RestController
@RequestMapping("/document/v1")
@CrossOrigin()
public class PDFGeneratorController {

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private PdfGeneratorService pdfGeneratorService;

	@GetMapping(value = "/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<byte[]> generatePdf() throws Exception {
		List<Employee> employees = employeeRepository.findAll();

		byte[] pdfBytes = pdfGeneratorService.generateEmployeePdf(employees);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PDF);
		headers.setContentDisposition(ContentDisposition.inline().filename("employees.pdf").build());
		headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(pdfBytes);
	}

}