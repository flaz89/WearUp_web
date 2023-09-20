package com.wearup.wearup.favorite;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/favorites")
public class Favorite_Controller {
	
	private final Favorite_Service favSrv;

	@Autowired
	public Favorite_Controller(Favorite_Service favSrv) {
		this.favSrv = favSrv;
	}
	
	
	@GetMapping("/{userId}")
	@PreAuthorize("hasAuthority('USER')")
	public List<Favorite> getAllFavorites(@PathVariable UUID userId) {
		return favSrv.getFavoritesByUserId(userId);
	}
	
	
	@PostMapping("/add")
    @PreAuthorize("hasAuthority('USER')")
	@ResponseStatus(HttpStatus.CREATED)
    public Favorite addToFav(@RequestParam UUID userId, @RequestParam long productId) {
        return favSrv.addToFav_user(userId, productId);
    }
	
	//fare il metodo removeLike

			

}
