package com.wearup.wearup.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.wearup.wearup.product.payloads.ProductRequestPayload;

@RestController
@RequestMapping("/products")
public class Product_Controller {

	private final Product_Service prodSrv;

	@Autowired
	public Product_Controller(Product_Service prodSrv) {
		this.prodSrv = prodSrv;
	}
	
	// -------------------------------------------- GET PRODUCT HOME LIST
	@GetMapping("/home")
	public List<Product> homeProduct() {
		return prodSrv.findTop4ProductsByLikes();
	}
	
	// -------------------------------------------- GET ALL PRODUCT LIST
	@GetMapping
	public Page<Product> getProducts(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, 
			@RequestParam(defaultValue = "id") String sortBy) {
		return prodSrv.find(page, size, sortBy);
	}
	
	// -------------------------------------------- CREATE PRODUCT
	
	@PostMapping
	@PreAuthorize("hasAuthority('ADMIN') "
			+ "or hasAuthority('BRAND') ")
	@ResponseStatus(HttpStatus.CREATED)
	public Product createProduct(@RequestBody ProductRequestPayload body) {
		Product newProduct = prodSrv.createProduct(body);
		return newProduct;
	}
}
