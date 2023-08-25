package com.example.demo.controller;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Entity.Employee;
import com.example.demo.service.EmployeeService;

@RestController
public class EmployeeController {

	@Autowired
	public EmployeeService service;
	
	@PostMapping(value="/saveemployee", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = "application/json")
	public ResponseEntity saveEmployee(@RequestParam(value="files") MultipartFile[] files) throws Exception
	{
		for(MultipartFile file : files)
		{
			service.saveEmployee(file);
		}
		return ResponseEntity.status(HttpStatus.CREATED).build();
		
	}
	
	   @GetMapping(value = "/employees", produces = "application/json")
	    public CompletableFuture<ResponseEntity> findAllemployee() {
	       return  service.findAllEployee().thenApply(ResponseEntity::ok);
	    }


	    @GetMapping(value = "/getEmployeesByThread", produces = "application/json")
	    public  ResponseEntity getemployee(){
	        CompletableFuture<List<Employee>> employee1=service.findAllEployee();
	        CompletableFuture<List<Employee>> employee2=service.findAllEployee();
	        CompletableFuture<List<Employee>> employee3=service.findAllEployee();
	        CompletableFuture.allOf(employee1,employee2,employee3).join();
	        return  ResponseEntity.status(HttpStatus.OK).build();
	    }
}
