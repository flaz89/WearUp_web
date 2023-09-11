package com.wearup.wearup.favorites;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wearup.wearup.exception.NotFoundException;
import com.wearup.wearup.favorites.payloads.FavoriteResponse;
import com.wearup.wearup.product.Product;
import com.wearup.wearup.product.Product_Service;
import com.wearup.wearup.user.User;
import com.wearup.wearup.user.User_Service;

@Service
public class Favorite_Service {
	
	private final Favorite_Repository favRepo;
	
	@Autowired
	private User_Service userSrv;
	@Autowired
	private Product_Service prodSrv;

	@Autowired
	public Favorite_Service(Favorite_Repository favRepo) {
		this.favRepo = favRepo;
	}
	
	//------------------------------------------------ converto l'entità favorite in un modello più semplice per evitare lo stack overflow
	public FavoriteResponse convertToFavSimple(Favorite favorite) {
		FavoriteResponse favSimple = new FavoriteResponse();
		favSimple.setId(favorite.getId());
	    if (favorite.getUser() != null) {
	    	favSimple.setUserId(favorite.getUser().getId());
	    }
	    if (favorite.getProduct() != null) {
	    	favSimple.setProductId(favorite.getProduct().getId());
	    }
	    return favSimple;
	}
	
	//------------------------------------------------ converto la lista favorite in una lista fatta di modelli favorite più semplici
	public List<FavoriteResponse> convertToFavSimpleList(List<Favorite> favorites) {
	    List<FavoriteResponse> favoriteResponses = new ArrayList<>();
	    for (Favorite favorite : favorites) {
	        FavoriteResponse favoriteResponse = new FavoriteResponse();
	        favoriteResponse.setId(favorite.getId());
	        favoriteResponse.setProductId(favorite.getProduct().getId());
	        favoriteResponse.setUserId(favorite.getUser().getId());
	        favoriteResponses.add(favoriteResponse);
	    }
	    return favoriteResponses;
	}
	
	
	public FavoriteResponse addFavorite(UUID userId, Long productId){
		
		User userFound = userSrv.findById(userId);
		Product productFound = prodSrv.findByProductId(productId);

		
		Favorite newFav = new Favorite();
		newFav.setUser(userFound);
		newFav.setProduct(productFound);
		favRepo.save(newFav);
		
		return convertToFavSimple(newFav);
	}
	
	public void removeFavorite(Long favoriteId) throws NotFoundException{
		System.out.println(favoriteId);
		Favorite found = favRepo.findById(favoriteId).orElseThrow(() -> new NotFoundException(favoriteId));
		favRepo.delete(found);
	}
	
	public List<FavoriteResponse> getFavoritesByUser(UUID userId) {
	    List<Favorite> favList = favRepo.findByUserId(userId);
	    return convertToFavSimpleList(favList);
	}

	
}
