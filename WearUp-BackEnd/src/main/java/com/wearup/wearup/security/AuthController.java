package com.wearup.wearup.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.wearup.wearup.brand.Brand;
import com.wearup.wearup.brand.Brand_Service;
import com.wearup.wearup.brand.payloads.BrandRequestPayload;
import com.wearup.wearup.exception.UnauthorizedException;
import com.wearup.wearup.user.User;
import com.wearup.wearup.user.User_Service;
import com.wearup.wearup.user.payloads.LoginSuccessfullPayload;
import com.wearup.wearup.user.payloads.UserLoginPayload;
import com.wearup.wearup.user.payloads.UserRequestPayload;

@RestController
@RequestMapping("/auth")
public class AuthController {
	@Autowired
	User_Service userSrv;
	
	@Autowired
	Brand_Service brandSrv;
	
	@Autowired
	JWTTools jwtTools;

	@Autowired
	PasswordEncoder bcrypt;
	
	// ---------------------------------------------- USER

	@PostMapping("/register")
	@ResponseStatus(HttpStatus.CREATED)
	public User saveUser(@RequestBody @Validated UserRequestPayload body) {
		body.setPassword(bcrypt.encode(body.getPassword()));
		User created = userSrv.create(body);

		return created;
	}

//	@PostMapping("/login")
//	public LoginSuccessfullPayload login(@RequestBody UserLoginPayload body) {
//
//	    // Cerca tra gli utenti
//	    User user = userSrv.findByEmail(body.getEmail());
//
//	    if (user != null && bcrypt.matches(body.getPassword(), user.getPassword())) {
//	        String token = jwtTools.createUserToken(user);
//	        return new LoginSuccessfullPayload(token);
//	    }
//	    // Se l'utente non è stato trovato tra gli utenti, cerca tra le entità brand
//	    Brand brand = brandSrv.findbyEmail(body.getEmail());
//	
//
//	    if (brand != null && bcrypt.matches(body.getPassword(), brand.getPassword())) {
//	        String token = jwtTools.createBrandToken(brand); // Implementa il metodo per creare il token per il brand
//	        return new LoginSuccessfullPayload(token);
//	    }
//
//	    // Se non è stato trovato né l'utente né il brand, restituisci UnauthorizedException
//	    throw new UnauthorizedException("Invalid credentials!");
//	}
	@PostMapping("/login")
	public LoginSuccessfullPayload login(@RequestBody UserLoginPayload body) {
		
		User user = userSrv.findByEmail(body.getEmail());
		Brand brand = brandSrv.findbyEmail(body.getEmail());
		
		if (user != null && bcrypt.matches(body.getPassword(), user.getPassword())) {
			String token = jwtTools.createUserToken(user);
	        return new LoginSuccessfullPayload(token);
		} else if (brand != null && bcrypt.matches(body.getPassword(), brand.getPassword())) {
			String token = jwtTools.createBrandToken(brand); 
	        return new LoginSuccessfullPayload(token);
		} else {
			throw new UnauthorizedException("Invalid credentials, retry again!");
		}
	}
	// ---------------------------------------------- BRAND
	
	@PostMapping("/register/brand")
	@ResponseStatus(HttpStatus.CREATED)
	public Brand saveBrand(@RequestBody @Validated BrandRequestPayload body) {
		body.setPassword(bcrypt.encode(body.getPassword()));
		Brand created = brandSrv.create(body);
		return created;
	}

}
