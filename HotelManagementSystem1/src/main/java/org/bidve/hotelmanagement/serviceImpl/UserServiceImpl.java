package org.bidve.hotelmanagement.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.bidve.hotelmanagement.JWT.CustomerUsersDetailsService;
import org.bidve.hotelmanagement.JWT.JwtFilter;
import org.bidve.hotelmanagement.JWT.JwtUtil;
import org.bidve.hotelmanagement.POJO.User;
import org.bidve.hotelmanagement.constents.HotelConstants;
import org.bidve.hotelmanagement.dao.UserDao;
import org.bidve.hotelmanagement.service.UserService;
import org.bidve.hotelmanagement.utils.EmailUtils;
import org.bidve.hotelmanagement.utils.HotelUtils;
import org.bidve.hotelmanagement.wrapper.UserWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

//import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class UserServiceImpl<AuthenticationManager> implements UserService {

	@Autowired
	UserDao userDao;
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	CustomerUsersDetailsService customerUserDetailsService;
	
	@Autowired
	JwtUtil jwtUtil;
	
	@Autowired
	JwtFilter jwtFilter;
	
	@Autowired
	EmailUtils emailUtils;
	
	@Override
	public ResponseEntity<String> signUp(Map<String, String> requestMap) {
		//log.info("Inside signup {}", requestMap);
	try {
		if(validateSignUpMap(requestMap)) {
			User user = userDao.findByEmailId(requestMap.get("email"));
			if(Objects.isNull(user)) {
				userDao.save(getUserFromMap(requestMap));
				return HotelUtils.getResponseEntity("responseMessage:" + "Successfully Registered.",HttpStatus.OK);
			}else {
				return HotelUtils.getResponseEntity("responseMessage:"+ "Email already exits.", HttpStatus.BAD_REQUEST);
			}
		}
		else {
			return HotelUtils.getResponseEntity(HotelConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
		}
	}catch(Exception ex) {
		ex.printStackTrace();
	}
	return HotelUtils.getResponseEntity(HotelConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
		
}


	private boolean validateSignUpMap(Map<String, String> requestMap) {
		if(requestMap.containsKey("name") && requestMap.containsKey("contactNumber")
				&& requestMap.containsKey("email") && requestMap.containsKey("password"))
				{
					return true;
				}
		return false;
	}
	
	private User getUserFromMap(Map<String,String> requestMap){
		User user = new User();
		user.setName(requestMap.get("name"));
		user.setContactNumber(requestMap.get("contactNumber"));
		user.setEmail(requestMap.get("email"));
		user.setPassword(requestMap.get("password"));
		user.setStatus(requestMap.get("status"));
		user.setRole(requestMap.get("role"));
		return user;
		
	}


	@Override
	public ResponseEntity<String> login(Map<String, String> requestMap) {
		//log.info("Inside login");
		try {
			Authentication auth = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(requestMap.get("email"),requestMap.get("password"))
				);
				if(auth.isAuthenticated()) {
					if(customerUserDetailsService.getUserDetail().getStatus().equalsIgnoreCase(anotherString: "true")) {
						return new ResponseEntity<String>(body: "{\"token\":\""+
								jwtUtil.generateToken(customerUserDetailsService.getUserDetail().getEmail(),
										customerUserDetailsService.getUserDetail().getRole()) + "\"}",
								HttpStatus.OK);
					}
					else {
						return new ResponseEntity<String>(status: "{\"message\":\""+"Wait for admin approval."+"\"}",
								HttpStatus.BAD_REQUEST);
					}
				}
		}catch(Exception ex) {
			log.error("{}",ex);
		}
		return new ResponseEntity<String>(status: "{\"message\":\""+"Bad Credentials."
				+ ""+"\"}",
				HttpStatus.BAD_REQUEST);

	}


	@Override
	public ResponseEntity<List<UserWrapper>> getAllUser() {
		try {
			if(jwtFilter.isAdmin()) {
				return new ResponseEntity<>(userDao.getAllUser(),HttpStatus.OK);
			}else {
				return new ResponseEntity<>(new ArrayList<>(),HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
	}


	@Override
	public ResponseEntity<String> update(Map<String, String> requestMap) {
		try {
			if(jwtFilter.isAdmin()) {
				Optional<User> optional = userDao.findById(Integer.parseInt(requestMap.get("id")));
				sendMailToAllAdmin(requestMap.get("status"), optional.get().getEmail(),userDao.getAllAdmin());
				if(!optional.isEmpty()) {
					userDao.updateStatus(requestMap.get("status"),Integer.parseInt(requestMap.get("id")));
					return HotelUtils.getResponseEntity("User Status Updated Successfully", HttpStatus.OK);
				}
				else {
					return HotelUtils.getResponseEntity("User id does not exist", HttpStatus.OK);
				}
			}
			else {
				return HotelUtils.getResponseEntity(HotelConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return HotelUtils.getResponseEntity(HotelConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}


	private void sendMailToAllAdmin(String string, String email, List<String> allAdmin) {
		allAdmin.remove(jwtFilter.getCurrentUser());
		if(status != null && status.equalsIgnoreCase(anotherString: "true")) {
			emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(), subject: "Account Approved", text: "USER:-" + user +" \n is approved by \nADMIN:-" + jwtFilter.getCurrentUser(), allAdmin);
		}else {
			emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(), subject: "Account Disabled", text: "USER:-" + user +" \n is disabled by \nADMIN:-" + jwtFilter.getCurrentUser(), allAdmin);
			
		}
		
	}


	@Override
	public ResponseEntity<String> checkToken() {
		return HotelUtils.getResponseEntity(responseMessage: "true", HttpStatus.OK);
	}


	@Override
	public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
		try {
			User userObj = userDao.findByEmail(jwtFilter.getCurrentUser());
			if(!userObj.equals(null)) 
			{
				if(userObj.getPassword().equals(requestMap.get("oldPassword"))) {
					userObj.setPassword(requestMap.get("newPassword"));
					userDao.save(userObj);
					return HotelUtils.getResponseEntity(responseMessage: "Password Updated Successfully ", HttpStatus.OK); 
				}
				return HotelUtils.getResponseEntity(responseMessage: "Incorrect Old Password ", HttpStatus.BAD_REQUEST);
			}
			return HotelUtils.getResponseEntity(HotelConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR); 
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		return HotelUtils.getResponseEntity(HotelConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}


	@Override
	public ResponseEntity<String> forgotPassword(Map<String, String> requestMap) {
		try {
			User user = userDao.findByEmail(requestMap.get("email"));
			if(!Objects.isNull(user) && !Strings.isNullOrEmpty(user.getEmail()))
					emailUtils.forgotMail(user.getEmail(), subject: "Credentials by hotel Management System",user.getPassword());
				return HotelUtils.getResponseEntity(responseMessage: "Check your mail for credentials", HttpStatus.OK);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return HotelUtils.getResponseEntity(HotelConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	
	

}
