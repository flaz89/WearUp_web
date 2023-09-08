package com.wearup.wearup.brand;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.wearup.wearup.brand.payloads.BrandRequestPayload;
import com.wearup.wearup.exception.BadRequestException;
import com.wearup.wearup.exception.NotFoundException;
import com.wearup.wearup.user.User;

@Service
public class Brand_Service {

	private final Brand_Repository brandRepo;
	
	@Autowired
	PasswordEncoder bcrypt;

	@Autowired
	public Brand_Service(Brand_Repository brandRepo) {
		this.brandRepo = brandRepo;
	}
	
	// ---------------------------------------------- CREA
	
	public Brand create(BrandRequestPayload body) {
		// check if VATnumber already in use
		brandRepo.findBrandByVatNumber(body.getVatNumber()).ifPresent(user -> {
			throw new BadRequestException("Vat number is already used");
		});
		
		Brand newBrand = new Brand(
				body.getBrandName(),
				body.getAddress(),
				body.getCity(),
				body.getCountry(),
				body.getPhoneNumber(),
				body.getVatNumber(),
				body.getEmail(),
				body.getPassword(),
				body.getProfilePicture(),
				body.getWebSite()
				);

		return brandRepo.save(newBrand);
	}
	
	// ---------------------------------------------- OTTIENI LISTA BRAND
	
	public Page<Brand> find(int page, int size, String sort) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(sort)); // (numero di pagina, numero di elementi per
																		// pagina, nome del campo per cui sortare)
		return brandRepo.findAll(pageable);
	}
	
	// ---------------------------------------------- CERCA by ID
	
	public Brand findById(long id) throws NotFoundException {
		return brandRepo.findById(id).orElseThrow(() -> new NotFoundException(id));
	}
	
	// ---------------------------------------------- CERCA e AGGIORNA by ID
	
	public Brand findByIdAndUpdate(long id, BrandRequestPayload body) throws NotFoundException {
		Brand found = this.findById(id);
		
		found.setBrandName(body.getBrandName());
		found.setAddress(body.getAddress());
		found.setCity(body.getCity());
		found.setCountry(body.getCountry());
		found.setPhoneNumber(body.getPhoneNumber());
		found.setVatNumber(body.getVatNumber());
		found.setEmail(body.getEmail());
		found.setPassword(bcrypt.encode(body.getPassword()));
		found.setWebSite(body.getWebSite());
		if (body.getProfilePicture() != null && !body.getProfilePicture().isEmpty()) {
	        found.setProfilePicture(body.getProfilePicture());
	    }

		return brandRepo.save(found);
	}
	

	// ---------------------------------------------- ELIMINA by ID
	
	public void findByIdAndDelete(long id) throws NotFoundException {
		Brand found = this.findById(id);
		brandRepo.delete(found);
	}
	
	// ---------------------------------------------- CERCA by EMAIL 
	
	public Brand findbyEmail(String email) {
		Brand foundBrand = brandRepo.findBrandByEmail(email).orElse(null);
		return foundBrand;
	}
	
	
	
}
