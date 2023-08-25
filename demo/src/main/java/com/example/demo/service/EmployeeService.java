package com.example.demo.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Entity.Employee;
import com.example.demo.repository.EmployeeRepo;

@Service
public class EmployeeService {

	private EmployeeRepo repository;

	Object target;
	Logger logger = LoggerFactory.getLogger(Employee.class);

	@Async
	public CompletableFuture<List<Employee>> saveEmployee(MultipartFile file) throws Exception {
		long start = System.currentTimeMillis();
		List<Employee> emp = parseCSVFile(file);
		logger.info("saving list of employees of size {}", emp.size(), "" + Thread.currentThread().getName());
		emp = repository.saveAll(emp);
		long end = System.currentTimeMillis();
		logger.info("Total time {}", (end - start));
		return CompletableFuture.completedFuture(emp);
	}

	@Async
	public CompletableFuture<List<Employee>> findAllEployee() {
		logger.info("get list of employee by " + Thread.currentThread().getName());
		List<Employee> employee = repository.findAll();
		return CompletableFuture.completedFuture(employee);
	}

	private List<Employee> parseCSVFile(final MultipartFile file) throws Exception {
		final List<Employee> employees = new ArrayList<>();
		try {
			try (final BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
				String line;
				while ((line = br.readLine()) != null) {
					final String[] data = line.split(",");
					final Employee emp = new Employee();
					emp.setName(data[0]);
					emp.setEmail(data[1]);
					emp.setGender(data[2]);
					employees.add(emp);
				}
				return employees;
			}
		} catch (final IOException e) {
			logger.error("Failed to parse CSV file {}", e);
			throw new Exception("Failed to parse CSV file {}", e);
		}
	}
}
