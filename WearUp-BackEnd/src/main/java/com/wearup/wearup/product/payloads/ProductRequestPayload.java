package com.wearup.wearup.product.payloads;

import java.util.List;

import com.wearup.wearup.brand.Brand;
import com.wearup.wearup.product.Product_Type;

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
	private String link3Dk;
	
	@NotNull(message = "You must upload at least one texture image link")
	private List<String> linkTexture;
	
	private String productPicture;
}
