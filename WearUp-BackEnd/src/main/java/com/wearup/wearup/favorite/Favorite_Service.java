package com.wearup.wearup.favorite;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wearup.wearup.product.Product;
import com.wearup.wearup.product.Product_Repository;
import com.wearup.wearup.user.User;
import com.wearup.wearup.user.User_Repository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class Favorite_Service {
	
	@Autowired
	private Favorite_Repository favRepo;
	
	@Autowired
	private User_Repository userRepo;
	
	@Autowired
	private Product_Repository prodRepo;
	
	
	// ---------------------------------------------- USER
	
	public Favorite addToFav_user(UUID userId, long productId ) {
		
		User user = userRepo.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
        Product product = prodRepo.findById(productId).orElseThrow(() -> new EntityNotFoundException("Product not found"));
        
        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setProduct(product);
		return favRepo.save(favorite);
	}
	
	public void removefavById_user(UUID userId, long productId ) {
		
		Optional<Favorite> favorite = favRepo.findByUserIdAndProductId(userId, productId);
		favorite.ifPresent(fav -> {
	        favRepo.delete(fav);
	    });
	}
	
	public List<Favorite> getFavoritesByUserId(UUID userId) {
        return favRepo.findByUserId(userId);
    }


	// ---------------------------------------------- BRAND

}
