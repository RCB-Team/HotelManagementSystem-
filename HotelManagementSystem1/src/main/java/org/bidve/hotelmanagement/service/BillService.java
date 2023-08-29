package org.bidve.hotelmanagement.service;

import java.util.List;
import java.util.Map;

import org.bidve.hotelmanagement.POJO.Bill;
import org.springframework.http.ResponseEntity;

public interface BillService {

	ResponseEntity<String> generateReport(Map<String, Object> requestMap);

	ResponseEntity<List<Bill>> getBills();

}
