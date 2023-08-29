package org.bidve.hotelmanagement.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.bidve.hotelmanagement.POJO.Product;
import org.bidve.hotelmanagement.wrapper.ProductWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

public interface ProductDao extends JpaRepository<Product, Integer> {

	List<ProductWrapper> getAllProduct();
	
	@Modifying
	@Transactional
	Integer updateProductStatus(@Param("status") String status, @Param("id") Integer id);

	List<ProductWrapper> getProductByCategory(@Param("id")Integer id);

	ProductWrapper getProductById(@Param("id")Integer id);

}
