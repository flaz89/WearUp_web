package com.wearup.wearup.brand;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wearup.wearup.brand.payloads.BrandRequestPayload;
import com.wearup.wearup.exception.BadRequestException;
import com.wearup.wearup.exception.NotFoundException;

@Service
public class Brand_Service {

	private final Brand_Repository brandRepo;

	@Autowired
	public Brand_Service(Brand_Repository brandRepo) {
		this.brandRepo = brandRepo;
	}
	
	public Brand create(BrandRequestPayload body) {
		// check if VATnumber already in use
		brandRepo.findBrandByVatNumber(body.getVatNumber()).ifPresent(user -> {
			throw new BadRequestException("Vat number is already used");
		});
		
		Brand newBrand = new Brand(
				body.getBrandName(),
				body.getAddress(),
				body.getCity(),
				body.getPhoneNumber(),
				body.getVatNumber(),
				body.getEmail(),
				body.getPassword()
				);

		return brandRepo.save(newBrand);
	}
	
	public Brand findbyEmail(String email) {
		return brandRepo.findBrandByEmail(email)
				.orElseThrow(() -> new NotFoundException("Account with this mail:  [" + email + "] not found"));
	}
	
	
}
