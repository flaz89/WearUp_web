package com.wearup.wearup.brand;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.wearup.wearup.brand.payloads.BrandRequestPayload;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/brands")
public class Brand_Controller {
	
	private final Brand_Service brandSrv;

	@Autowired
	public Brand_Controller(Brand_Service brandSrv) {
		super();
		this.brandSrv = brandSrv;
	}
	
	// -------------------------------------------- GET BRANDS LIST x HOME
	
	@GetMapping("/home")
	public Page<Brand> getBrandListHome(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, 
			@RequestParam(defaultValue = "id") String sortBy) {
		return brandSrv.find(page, size, sortBy);
	}
	
	// -------------------------------------------- GET BRANDS LIST 
	
	@PreAuthorize("hasAuthority('ADMIN') "
			+ "or hasAuthority('BRAND') "
			+ "or hasAuthority('USER')")
	@GetMapping("")
	public Page<Brand> getBrandList(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, 
			@RequestParam(defaultValue = "id") String sortBy) {
		return brandSrv.find(page, size, sortBy);
	}
	
	// -------------------------------------------- FIND BRAND by ID
	
	@PreAuthorize("hasAuthority('ADMIN') "
			+ "or hasAuthority('BRAND') "
			+ "or hasAuthority('USER')")
		@GetMapping("/{brandId}")
		public Brand findById(@PathVariable long brandId) {
			return brandSrv.findById(brandId);

		}
	
	// -------------------------------------------- UPDATE BRAND by ID
	
	@PreAuthorize("hasAuthority('ADMIN') "
			+ "or hasAuthority('BRAND') ")
		@PutMapping("/{brandId}")
		@ResponseStatus(HttpStatus.OK)
		public Brand updateUser(@PathVariable long brandId, @RequestBody BrandRequestPayload body) {
			return brandSrv.findByIdAndUpdate(brandId, body);
		}
	
	// -------------------------------------------- DELETE BRAND by ID
		
	@PreAuthorize("hasAuthority('ADMIN') ")
	@DeleteMapping("/{brandId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteBrandById(@PathVariable long brandId) {
		brandSrv.findByIdAndDelete(brandId);
		log.warn("Brand [" + brandId + "] deleted correctly");
	}
}
