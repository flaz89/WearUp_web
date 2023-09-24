package com.wearup.wearup.product;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wearup.wearup.brand.Brand;
import com.wearup.wearup.favorite.Favorite;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
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
	@JsonIgnore
	private Brand brand;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private Product_Type type;
	
	@Column(nullable = false)
	private double price;
	@Column(nullable = false)
	private String link3D;
	@Column(nullable = false)
	@NotNull(message = "You must upload at least albedo texture")
	private Texture textures;
	@Column(nullable = false)
	private String productPicture;
	@Column(nullable = false)
	private String productLink;
	
	private Date creationDate;
	private long likeCounter;
	
	@OneToMany(mappedBy = "product")
	private List<Favorite> favorites;
	
	
	public Product(String productCode,String productName, String description, Brand brand, Product_Type type, double price, String link3D,
			Texture textures, String productPicture, String productLink) {
		this.productCode = productCode;
		this.productName = productName;
		this.description = description;
		this.brand = brand;
		this.type = type;
		this.price = price;
		this.link3D = link3D;
		this.textures = textures;
		this.productPicture = productPicture;
		this.productLink = productLink;
		this.creationDate = new Date();
	}

	
}
