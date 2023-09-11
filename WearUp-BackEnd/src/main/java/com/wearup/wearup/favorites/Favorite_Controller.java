package com.wearup.wearup.favorites;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.wearup.wearup.favorites.payloads.FavoriteResponse;

@RestController
@RequestMapping("/favorites")
public class Favorite_Controller {
	
	private final Favorite_Service favSrv;

	@Autowired
	public Favorite_Controller(Favorite_Service favSrv) {
		this.favSrv = favSrv;
	}
	
	@PreAuthorize("hasAuthority('USER')")
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public FavoriteResponse addLike(@RequestParam UUID userId, @RequestParam Long productId) {
		return favSrv.addFavorite(userId, productId);
	}
	
	@PreAuthorize("hasAuthority('USER')")
	@DeleteMapping("/{favId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void removeLike(@PathVariable Long favId) {
		favSrv.removeFavorite(favId);
	}
	
	@PreAuthorize("hasAuthority('USER')")
	@GetMapping("/{userId}")
	public List<FavoriteResponse> userFavorites(@PathVariable UUID userId) {
		return favSrv.getFavoritesByUser(userId);
	}
}
