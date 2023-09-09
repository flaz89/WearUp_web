package com.wearup.wearup.product;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wearup.wearup.brand.Brand;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
	
	@Id
	@GeneratedValue
	private long id;
	
	@Column(nullable = false, unique = true)
	private String productCode;
	@Column(nullable = false)
	private String productName;
	private String description;
	
	@ManyToOne
    @JoinColumn(name = "brand_id",nullable = false)
	private Brand brand;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private Product_Type type;
	
	@Column(nullable = false)
	private double price;
	@Column(nullable = false)
	private String link3Dk;
	@Column(nullable = false)
	private List<String> linkTexture;
	@Column(nullable = false)
	private String productPicture;
	
	private Date creationDate;
	private long likeCounter;
	//private List<Favorite> favorites;
	
	
	public Product(String productCode,String productName, String description, Brand brand, Product_Type type, double price, String link3Dk,
			List<String> linkTexture, String productPicture) {
		this.productCode = productCode;
		this.productName = productName;
		this.description = description;
		this.brand = brand;
		this.type = type;
		this.price = price;
		this.link3Dk = link3Dk;
		this.linkTexture = linkTexture;
		this.productPicture = productPicture;
		this.creationDate = new Date();
	}

	
}
