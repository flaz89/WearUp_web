package com.wearup.wearup.product.payloads;

import java.util.List;

import com.wearup.wearup.brand.payloads.BrandResponseList;
import com.wearup.wearup.product.Product_Type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProductResponseList {
	
	private long id;
	private String productCode;
	private String productName;
	private String description;	
	private BrandResponseList brand;	
	private Product_Type type;	
	private double price;
	private String link3Dk;
	private List<String> linkTexture;
	private long likeCounter;
	private String productPicture;
}
