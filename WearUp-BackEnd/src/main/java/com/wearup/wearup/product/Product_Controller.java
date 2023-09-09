package com.wearup.wearup.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.wearup.wearup.brand.Brand_Controller;
import com.wearup.wearup.product.payloads.ProductRequestPayload;
import com.wearup.wearup.product.payloads.ProductResponseList;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/products")
public class Product_Controller {

	private final Product_Service prodSrv;

	@Autowired
	public Product_Controller(Product_Service prodSrv) {
		this.prodSrv = prodSrv;
	}
	
	// -------------------------------------------- GET TOP 4 PRODUCT LIST HOME 
	@GetMapping("/home")
	public List<ProductResponseList> homeProduct() {
		return prodSrv.findTop4ProductsByLikes();
	}
	
	// -------------------------------------------- GET ALL PRODUCT LIST x [BRAND/USER/ADMIN]
	@PreAuthorize("hasAuthority('ADMIN') "
			+ "or hasAuthority('BRAND') ")
	@GetMapping
	public Page<ProductResponseList> getProducts(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, 
			@RequestParam(defaultValue = "id") String sortBy) {
		return prodSrv.find(page, size, sortBy);
	}
	
	// -------------------------------------------- CREATE PRODUCT x [BRAND]
	
	@PostMapping
	@PreAuthorize("hasAuthority('BRAND') ")
	@ResponseStatus(HttpStatus.CREATED)
	public ProductResponseList createProduct(@RequestBody ProductRequestPayload body) {
		ProductResponseList newProduct = prodSrv.createProduct(body);
		return newProduct;
	}
	
	// -------------------------------------------- DELETE PRODUCT x [BRAND]
	
	@PreAuthorize("hasAuthority('ADMIN') "
			+ "or hasAuthority('BRAND') ")
	@DeleteMapping("/{prductId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteProductById(@PathVariable long prductId) {
		prodSrv.findByIdAndDelete(prductId);
		log.warn("Product [" + prductId + "] deleted correctly");
	}
}
