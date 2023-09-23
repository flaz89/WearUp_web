package com.wearup.wearup.product;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.multipart.MultipartFile;

import com.wearup.wearup.brand.Brand;
import com.wearup.wearup.brand.Brand_Controller;
import com.wearup.wearup.product.payloads.ProductRequestPayload;
import com.wearup.wearup.product.payloads.ProductResponseList;
import com.wearup.wearup.uploadCloudinary.Cloudinary_Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/products")
public class Product_Controller {
	
	@Autowired
	Cloudinary_Service cloudSrv;

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
	
	// -------------------------------------------- GET ALL PRODUCT LIST x ordinati per i più recenti\ [BRAND/USER/ADMIN]
	@PreAuthorize("hasAuthority('ADMIN') "
			+ "or hasAuthority('BRAND') "
			+ "or hasAuthority('USER') ")
	@GetMapping("/all")
	public Page<ProductResponseList> getProducts(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, 
			@RequestParam(defaultValue = "creationDate,desc") String sortBy) {
		return prodSrv.find(page, size, sortBy);
	}
	
	// -------------------------------------------- SEARCH PRODUCT by FILTER x [BRAND/USER/ADMIN]
	@PreAuthorize("hasAuthority('ADMIN') "
			+ "or hasAuthority('BRAND') "
			+ "or hasAuthority('USER') ")
	@GetMapping("/search")
    public Page<ProductResponseList> getProducts(
        @RequestParam(required = false) String productCode,
        @RequestParam(required = false) String productName,
        @RequestParam(required = false) String brand,
        @RequestParam(required = false) Product_Type type,
        @RequestParam(required = false) Double minPrice,
        @RequestParam(required = false) Double maxPrice,
        @RequestParam(required = false) Integer minLikeCounter,
        @RequestParam(required = false) Integer maxLikeCounter,
        @RequestParam(defaultValue = "creationDate") String sortBy,  // Default a creationDate
        @RequestParam(defaultValue = "ASC") String sortDirection,  // Nuovo parametro, con default a ASC
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    ) {
        return prodSrv.findProducts(
            productCode, 
            productName, 
            brand,
            type, 
            minPrice, 
            maxPrice, 
            minLikeCounter, 
            maxLikeCounter,
            sortBy, 
            sortDirection,
            page, 
            size
        );
	}
	
	// -------------------------------------------- GET ALL PRODUCT LIST by PRICE RANGE x [BRAND/USER/ADMIN]
	
	@PreAuthorize("hasAuthority('ADMIN') "
			+ "or hasAuthority('BRAND') "
			+ "or hasAuthority('USER') ")
	@GetMapping("/byPriceRange")
	public Page<ProductResponseList> getProductsByPrice(
			@RequestParam double minPrice,
			@RequestParam double maxPrice,
			@RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size) {
		Page<ProductResponseList> products = prodSrv.getProductsByPriceRange(minPrice, maxPrice, page, size);
	    return products;
	}
		
		
	// -------------------------------------------- GET ALL CATEGORIES LIST [BRAND]
	@PreAuthorize("hasAuthority('BRAND') ")
	@GetMapping("/categories")
    public ResponseEntity<Map<String, List<String>>> getAllProductTypes() {
        Map<String, List<String>> productTypes = prodSrv.getAllProductTypes();
        return new ResponseEntity<>(productTypes, HttpStatus.OK);
    }
		
		
	
	// -------------------------------------------- CREATE PRODUCT x [BRAND]
	
	@PostMapping
	@PreAuthorize("hasAuthority('BRAND') ")
	@ResponseStatus(HttpStatus.CREATED)
	public ProductResponseList createProduct(@RequestBody ProductRequestPayload body) {
		ProductResponseList newProduct = prodSrv.createProduct(body);
		log.warn("Product [" + newProduct.getId() + "] created correctly");
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
	
	// -------------------------------------------- FIND PRODUCT by ID x [USER / BRAND / ADMIN]
	
	@PreAuthorize("hasAuthority('ADMIN') "
			+ "or hasAuthority('BRAND') "
			+ "or hasAuthority('USER') ")
	@GetMapping("/{prductId}")
	public ProductResponseList getProduct(@PathVariable long prductId){
		return prodSrv.findByProductId(prductId);
	}
	
	
	// -------------------------------------------- UPLOAD 3D MODEL x [BRAND]
	
	@PreAuthorize("hasAuthority('BRAND')")
	@PostMapping("/upload-3d-product")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Map<String, Object>> uploadProductModel(@RequestParam(value = "file", required = false) MultipartFile file) {
	    Map<String, Object> response = new HashMap<>();
	    try {
	    	
	    	if (file == null || file.isEmpty()) {
	            response.put("error", "File is required.");
	            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	        }
	    	
	        String originalFilename = file.getOriginalFilename();
	        String fileExtension = "";
	        
	        if (originalFilename != null && originalFilename.lastIndexOf(".") > 0) {
	            fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
	        }
	        
	        if (!"fbx".equals(fileExtension)) {
	            response.put("error", "Invalid file extension. Only .fbx files are allowed.");
	            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	        }
	        
	        String folderName = "WearUp/product-model";
	        String url = cloudSrv.upload3DModel(file, folderName);
	        response.put("url", url);
	        return new ResponseEntity<>(response, HttpStatus.OK);
	        
	    } catch (IOException e) {
	        response.put("error", "Failed to upload file: " + e.getMessage());
	        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	    }
	}
	
	
}
