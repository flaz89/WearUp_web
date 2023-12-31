package com.wearup.wearup.user;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.wearup.wearup.exception.BadRequestException;
import com.wearup.wearup.exception.NotFoundException;
import com.wearup.wearup.user.payloads.UserRequestPayload;



@Service
public class User_Service {
	

	private final User_Repository userRepo;

	@Autowired
	public User_Service(User_Repository _userRepo) {
		this.userRepo = _userRepo;
	}
	
	// -------------------------------------------------------- CREO UN UTENTE
	
	public User create(UserRequestPayload body) {
		// check if email already in use
		userRepo.findByEmail(body.getEmail()).ifPresent(user -> {
			throw new BadRequestException("This Email [" + body.getEmail() +"] already exist");
		});
		
		User newUser = new User(
				body.getName(), 
				body.getSurname(),
				body.getEmail(),
				body.getPassword(),
				body.getProfilePicture()
				);
		return userRepo.save(newUser);
	}

	// ------------------------------------------------------- OTTENGO LA LISTA DI TUTTI GLI UTENTI
	
	public Page<User> find(int page, int size, String sort) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(sort)); 
															
		return userRepo.findAll(pageable);
	}

	// ------------------------------------------------------- CERCO UTENTE by ID
	
	public User findById(UUID id) throws NotFoundException {
		return userRepo.findById(id).orElseThrow(() -> new NotFoundException(id));
	}
	
	// ------------------------------------------------------- AGGIORNO e CERCO UTENTE by ID

	public User findByIdAndUpdate(UUID id, UserRequestPayload body) throws NotFoundException {
		User found = this.findById(id);
		found.setEmail(body.getEmail());
		found.setName(body.getName());
		found.setSurname(body.getSurname());
		if (body.getProfilePicture() != null && !body.getProfilePicture().isEmpty()) {
	        found.setProfilePicture(body.getProfilePicture());
	    }

		return userRepo.save(found);
	}

	
	// ------------------------------------------------------- CANCELLO UTENTE by ID
	
	public void findByIdAndDelete(UUID id) throws NotFoundException {
		User found = this.findById(id);
		userRepo.delete(found);
	}
	
	// ------------------------------------------------------- CERCO UTENTE by EMAIL

//	public User findByEmail(String email) {
//		return userRepo.findByEmail(email)
//				.orElseThrow(() -> new NotFoundException("Account with this mail:  [" + email + "] not found"));
//	}
	
	public User findByEmail(String email) {
		User user = userRepo.findByEmail(email).orElse(null);
		return user;
	}
	
	
}
