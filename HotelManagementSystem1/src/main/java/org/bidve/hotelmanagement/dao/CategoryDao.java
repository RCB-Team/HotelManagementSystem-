package org.bidve.hotelmanagement.dao;

import java.util.List;

import org.bidve.hotelmanagement.POJO.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryDao extends JpaRepository<Category, Integer>{
	
	List<Category> getAllCategory();

}
