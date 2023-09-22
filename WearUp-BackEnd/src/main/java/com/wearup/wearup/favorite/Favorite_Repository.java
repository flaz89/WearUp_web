package com.wearup.wearup.favorite;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wearup.wearup.product.Product;
import com.wearup.wearup.user.User;

@Repository
public interface Favorite_Repository extends JpaRepository<Favorite, Long> {
	
	Optional<Favorite> findByUserIdAndProductId(UUID userId, Long productId);
	
	List<Favorite> findByUserId(UUID userId);
	
	Optional<Favorite> findByUserAndProduct(User user, Product product);
	
	List<Favorite> findAllByUserId(UUID userId);
}
