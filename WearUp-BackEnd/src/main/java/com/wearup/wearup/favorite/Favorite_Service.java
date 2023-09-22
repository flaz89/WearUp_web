package com.wearup.wearup.favorite;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wearup.wearup.exception.EntityAlreadyExistsException;
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
	
	public FavoriteResponse addToFav_user(UUID userId, long productId ) {
		
		User user = userRepo.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
        Product product = prodRepo.findById(productId).orElseThrow(() -> new EntityNotFoundException("Product not found"));
        
        Optional<Favorite> existingFavorite = favRepo.findByUserAndProduct(user, product);
        if (existingFavorite.isPresent()) {
            throw new EntityAlreadyExistsException("The product is already added to favorites for this user");
        }
        
        long currentLikeCount = product.getLikeCounter();
        product.setLikeCounter(currentLikeCount + 1);
        prodRepo.save(product);
        
        
        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setProduct(product);
       
		favRepo.save(favorite);
		
		return new FavoriteResponse(userId, productId);
	}
	
	public void removeFavorite(UUID userId, long productId) {
	    Optional<Favorite> favorite = favRepo.findByUserIdAndProductId(userId, productId);
	    
	    if (!favorite.isPresent()) {
	        throw new EntityNotFoundException("Favorite not found");
	    }
	    
	    favRepo.delete(favorite.get());
	}
	
	public List<FavoriteResponse> getFavoritesByUserId(UUID userId) {
	    List<Favorite> favorites = favRepo.findAllByUserId(userId);
	    return favorites.stream()
	                    .map(favorite -> new FavoriteResponse(userId, favorite.getProduct().getId()))
	                    .collect(Collectors.toList());
	}



	// ---------------------------------------------- BRAND

}
