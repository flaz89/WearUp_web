package com.wearup.wearup.favorites.payloads;

import java.util.UUID;

import lombok.Data;

@Data
public class FavoriteResponse {
	private Long id; 
    private UUID userId;
    private Long productId; 
}
