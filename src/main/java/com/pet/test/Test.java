package com.pet.test;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("test")
public class Test {
	
	
	@Value("${file.upload-dir}")
	private String uploadDir;
	
	@Value("${server.tomcat.basedir}")
	private String rootURL;
	
	
	@GetMapping("/{param}")
	public String get(@RequestParam String param) {
		return new String();
	}
	
	
	@GetMapping
	public String get() throws IOException {
		 Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
	        Files.createDirectories(uploadPath);
		return new String(uploadPath.toUri().toString());
	}
	

}
