package com.wearup.wearup.favorite;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Favorite_Repository extends JpaRepository<Favorite, Long> {
	
	Optional<Favorite> findByUserIdAndProductId(UUID userId, Long productId);
	
	List<Favorite> findByUserId(UUID userId);
}
