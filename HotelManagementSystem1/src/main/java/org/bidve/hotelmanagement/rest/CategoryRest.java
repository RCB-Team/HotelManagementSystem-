package org.bidve.hotelmanagement.rest;

import java.util.List;
import java.util.Map;

import org.bidve.hotelmanagement.POJO.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping(path = "/category")
public interface CategoryRest {
	
	@PostMapping(path = "/add")
	ResponseEntity<String> addNewCategory(@RequestBody(required = true)
														Map<String, String> requestMap);
	
	@GetMapping(path = "/get")
	ResponseEntity<List<Category>> getAllCategory(@RequestParam(required = false)
																String filterValue);
	
	@PostMapping(path = "/update")
	ResponseEntity<String> updateCategory(@RequestBody(required = true)
														Map<String, String> requestMap);
}
