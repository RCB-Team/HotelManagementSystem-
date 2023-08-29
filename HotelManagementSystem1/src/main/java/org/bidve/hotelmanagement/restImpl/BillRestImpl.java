package org.bidve.hotelmanagement.restImpl;

import java.util.List;
import java.util.Map;

import org.bidve.hotelmanagement.POJO.Bill;
import org.bidve.hotelmanagement.constents.HotelConstants;
import org.bidve.hotelmanagement.rest.BillRest;
import org.bidve.hotelmanagement.service.BillService;
import org.bidve.hotelmanagement.utils.HotelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BillRestImpl implements BillRest{

	
	@Autowired
	BillService billService;
	
	@Override
	public ResponseEntity<String> generatedReport(Map<String, Object> requestMap) {
		try {
			return billService.generateReport(requestMap);
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return HotelUtils.getResponseEntity(HotelConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);  
	}

	@Override
	public ResponseEntity<List<Bill>> getBills() {
		try {
			return billService.getBills();
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

}
