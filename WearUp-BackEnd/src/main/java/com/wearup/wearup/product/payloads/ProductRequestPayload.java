package com.wearup.wearup.product.payloads;

import java.util.List;

import com.wearup.wearup.brand.Brand;
import com.wearup.wearup.product.Product_Type;
import com.wearup.wearup.product.Texture;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProductRequestPayload {
	
	@NotNull(message = "Code is required")
	@Size(min = 8, max = 30, message = "\n"
			+ "must have a minimum of 8 characters and a maximum of 30")
	private String productCode;
	
	@NotNull(message = "Name is required")
	private String productName;
	
	private String description;
	
	@NotNull(message = "Brand is required")
	private Brand brand;
	
	@NotNull(message = "Category is required")
	private Product_Type type;
	
	@NotNull(message = "Price is required")
	private double price;
	
	@NotNull(message = "3D link is required")
	private String link3D;
	
	@NotNull(message = "You must upload at least albedo Texture")
	private Texture textures;
	
	@NotNull(message = "You must upload product image")
	private String productPicture;
	
	@NotNull(message = "You must upload product web link")
	private String productLink;
}
