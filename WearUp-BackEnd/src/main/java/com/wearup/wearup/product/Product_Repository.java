package com.wearup.wearup.product;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface Product_Repository extends JpaRepository<Product, Long>{
	
	List<Product> findByType(Product_Type type);
	
	Optional<Product> findByProductCode(String productCode);
	
	@Query("SELECT p FROM Product p WHERE p.price >= :minPrice AND p.price <= :maxPrice")
    List<Product> findProductsByPriceRange(double minPrice, double maxPrice);
	
	List<Product> findTop4ByOrderByLikeCounterDesc();
	
}
