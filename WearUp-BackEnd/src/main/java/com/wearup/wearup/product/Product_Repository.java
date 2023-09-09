package com.wearup.wearup.product;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface Product_Repository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product>{
	
	List<Product> findByType(Product_Type type);
	
	Optional<Product> findByProductCode(String productCode);
	
	@Query("SELECT p FROM Product p WHERE p.price >= :minPrice AND p.price <= :maxPrice")
	Page<Product> findProductsByPriceRange(double minPrice, double maxPrice, Pageable pageable);
	
	List<Product> findTop4ByOrderByLikeCounterDesc();
	
}
