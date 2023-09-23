package com.wearup.wearup.product.payloads;

import java.util.List;

import com.wearup.wearup.brand.payloads.BrandResponseList;
import com.wearup.wearup.product.Product_Type;
import com.wearup.wearup.product.Texture;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseList {
	
	private long id;
	private String productCode;
	private String productName;
	private String description;	
	private BrandResponseList brand;	
	private Product_Type type;	
	private double price;
	private String link3D;
	private Texture textures;
	private long likeCounter;
	private String productPicture;
	private String productLink;
}
