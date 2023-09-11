package com.wearup.wearup.favorites;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface Favorite_Repository extends JpaRepository<Favorite, Long> {
	List<Favorite> findByUserId(UUID userId);
}
