package org.bidve.hotelmanagement.rest;

import java.util.List;
import java.util.Map;

import org.bidve.hotelmanagement.POJO.Bill;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(path = "/bill")
public interface BillRest {
	
	@PostMapping(path = "/generatedReport")
	ResponseEntity<String> generatedReport(@RequestBody Map<String,Object> requestMap);
	
	@GetMapping(path = "/getBills")
	ResponseEntity<List<Bill>> getBills();

}
