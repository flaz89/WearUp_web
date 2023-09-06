package com.wearup.wearup.brand;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Brand_Repository extends JpaRepository<Brand, Long>{
	Optional<Brand> findBrandByVatNumber(String vatNumber);
	
	Optional<Brand> findBrandByEmail(String email);
}
