package org.bidve.hotelmanagement.service;

import java.util.List;
import java.util.Map;

import org.bidve.hotelmanagement.POJO.Category;
import org.springframework.http.ResponseEntity;

public interface CategoryService {
	
	ResponseEntity<String> addNewCategory(Map<String, String>requestMap);

	ResponseEntity<List <Category>> getAllCategory(String filterValue);

	ResponseEntity<String> updateCategory(Map<String, String> requestMap);
	


}
