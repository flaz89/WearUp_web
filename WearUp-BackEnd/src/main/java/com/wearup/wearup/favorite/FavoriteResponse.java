package com.wearup.wearup.favorite;

import java.util.UUID;

import com.wearup.wearup.product.payloads.ProductResponseList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteResponse {
	
	private UUID userId;
	private long productId;
	
}
