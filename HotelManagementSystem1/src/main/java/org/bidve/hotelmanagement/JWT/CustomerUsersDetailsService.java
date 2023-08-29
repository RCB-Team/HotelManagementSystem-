package org.bidve.hotelmanagement.JWT;

import java.util.ArrayList;
import java.util.Objects;

import org.bidve.hotelmanagement.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomerUsersDetailsService implements UserDetailsService {
	
	@Autowired
	UserDao userDao;
	
	private org.bidve.hotelmanagement.POJO.User userDetail;
	
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
	log.info("Inside loadUserByUsername {}",username);
	userDetail = userDao.findByEmailId(username);
	if(!Objects.isNull(userDetail))
		return new User(userDetail.getEmail(),userDetail.getPassword(),new ArrayList<>());
	else
		throw new UsernameNotFoundException("User not found.");
	}
	
	public org.bidve.hotelmanagement.POJO.User getUserDetail(){
		return userDetail;
		
	}
	
	


}
