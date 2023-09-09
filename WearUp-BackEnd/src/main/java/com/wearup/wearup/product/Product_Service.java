package com.wearup.wearup.product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.wearup.wearup.brand.Brand;
import com.wearup.wearup.brand.Brand_Repository;
import com.wearup.wearup.brand.Brand_Service;
import com.wearup.wearup.brand.payloads.BrandResponseList;
import com.wearup.wearup.exception.BadRequestException;
import com.wearup.wearup.exception.NotFoundException;
import com.wearup.wearup.product.payloads.ProductRequestPayload;
import com.wearup.wearup.product.payloads.ProductResponseList;
import com.wearup.wearup.security.JWTTools;

@Service
public class Product_Service {
	
	@Autowired
	private Brand_Service brandSrv;
	
	private final Product_Repository prodRepo;

	@Autowired
	public Product_Service(Product_Repository prodRepo) {
		this.prodRepo = prodRepo;
	}
	
	
	
	//METODI per convertire le entità a entità più semplici ed evitare lo stack overflow
	
	private ProductResponseList convertToProductResponseList(Product product) {
	    Brand brand = product.getBrand();
	    BrandResponseList brandResponseList = convertToBrandResponseList(brand);
	    
	    return new ProductResponseList(
	            product.getId(),
	            product.getProductCode(),
	            product.getProductName(),
	            product.getDescription(),
	            brandResponseList,
	            product.getType(),
	            product.getPrice(),
	            product.getLink3Dk(),
	            product.getLinkTexture(),
	            product.getLikeCounter(),
	            product.getProductPicture()
	    );
	}
	
	private BrandResponseList convertToBrandResponseList(Brand brand) {
	    return new BrandResponseList(
	            brand.getBrandName(),
	            brand.getAddress(),
	            brand.getCity(),
	            brand.getCountry(),
	            brand.getPhoneNumber(),
	            brand.getEmail(),
	            brand.getProfilePicture(),
	            brand.getWebSite()
	    );
	}
	
	// ------------------------------------------------------- OTTENGO TUTTE LE CATEGORIE DEL PRODOTTO 
	
	public Map<String, List<String>> getAllProductTypes() {
        Map<String, List<String>> categoryMap = new HashMap<>();

        for (Product_Type type : Product_Type.values()) {
            String category = type.getCategory();
            String productType = type.name();

            if (!categoryMap.containsKey(category)) {
                categoryMap.put(category, new ArrayList<>());
            }

            categoryMap.get(category).add(productType);
        }

        return categoryMap;
    }
	
	// ------------------------------------------------------- OTTENGO LA LISTA DI TUTTI I PRODOTTI PER CATEGORIA
	
	public List<Product> getProductsByCategory(String categoryName) {
        List<Product> products = new ArrayList<>();

        for (Product_Type type : Product_Type.values()) {
            if (type.getCategory().equals(categoryName)) {
                products.addAll(prodRepo.findByType(type));
            }
        }

        return products;
    }
	
	
	// ------------------------------------------------------- OTTENGO LA LISTA DI TUTTI I PRODOTTI
	
	public Page<ProductResponseList> find(int page, int size, String sort) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(sort)); 
		Page<Product> productPage = prodRepo.findAll(pageable);
        return productPage.map(this::convertToProductResponseList);
	}
	
	// ------------------------------------------------------- OTTENGO LA LISTA DEI 4 MOST VIEWD
	
	public List<ProductResponseList> findTop4ProductsByLikes() {
	    List<Product> products = prodRepo.findTop4ByOrderByLikeCounterDesc();
	    List<ProductResponseList> productResponseLists = new ArrayList<>();
	    
	    for (Product product : products) {
	        ProductResponseList productResponseList = convertToProductResponseList(product);
	        productResponseLists.add(productResponseList);
	    }
	    
	    return productResponseLists;
	}
	
	
	// ------------------------------------------------------- OTTENGO LA LISTA DI TUTTI I PRODOTTI per FASCIA DI PREZZO
	
	public List<Product> getProductsByPriceRange(double minPrice, double maxPrice) {
        return prodRepo.findProductsByPriceRange(minPrice, maxPrice);
    }
	
	// -------------------------------------------------------- CREO UN PRODOTTO x BRAND
	
	public ProductResponseList createProduct(ProductRequestPayload body) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Brand currentBrand = (Brand) authentication.getPrincipal();
		
		prodRepo.findByProductCode(body.getProductCode()).ifPresent(product -> {
			throw new BadRequestException("A product with this product-code [" + body.getProductCode() +"] already exist");
		});
		

		
		Product newProduct = new Product(
				body.getProductCode(),
				body.getProductName(),
				body.getDescription(),
				currentBrand,
				body.getType(),
				body.getPrice(),
				body.getLink3Dk(),
				body.getLinkTexture(),
				body.getProductPicture()
				);
		
		prodRepo.save(newProduct);
		
		return convertToProductResponseList(newProduct);
	}
	
	// -------------------------------------------------------- CERCO UN PRODOTTO PER CODICE PRODOTTO x USER/BRAND/ADMIN
	
	public Product findByProductCode(String productCode) throws NotFoundException {
		return prodRepo.findByProductCode(productCode).orElseThrow(() -> new NotFoundException(productCode));
	}
	
	// -------------------------------------------------------- CERCO UN PRODOTTO PER ID PRODOTTO x ADMIN
	
	public Product findByProductId(long productId) throws NotFoundException {
		return prodRepo.findById(productId).orElseThrow(() -> new NotFoundException(productId));
	}
	
	// -------------------------------------------------------- CERCO e AGGIORNO UN PRODOTTO PER ID x BRAND/ADMIN
	
	public Product findByIdAndUpdate(String productCode, ProductRequestPayload body) throws NotFoundException {
		
		Product foundProduct = this.findByProductCode(productCode);
		
		foundProduct.setProductCode(body.getProductCode());
		foundProduct.setProductName(body.getProductName());
		foundProduct.setDescription(body.getDescription());
		foundProduct.setBrand(body.getBrand());		
		foundProduct.setType(body.getType());
		foundProduct.setPrice(body.getPrice());
		foundProduct.setLink3Dk(body.getLink3Dk());
		foundProduct.setLinkTexture(body.getLinkTexture());
		
		if (body.getProductPicture() != null && !body.getProductPicture().isEmpty()) {
	        foundProduct.setProductPicture(body.getProductPicture());
	    }

		return prodRepo.save(foundProduct);
	}
	
	// -------------------------------------------------------- CERCO e CANCELLO UN PRODOTTO PER ID x BRAND/ADMIN
	
	public void findByIdAndDelete(long productId) throws NotFoundException {
		Product product = this.findByProductId(productId);
		prodRepo.delete(product);
	}

}
