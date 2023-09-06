package com.wearup.wearup.user;

import java.util.UUID;

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

import com.wearup.wearup.user.payloads.UserRequestPayload;

@RestController
@RequestMapping("/users")
public class User_Controller {
	
	private final User_Service userSrv;

	@Autowired
	public User_Controller(User_Service _userSrv) {
		this.userSrv = _userSrv;
	}

	// -------------------------------------------- GET USER LIST
	@GetMapping
	@PreAuthorize("hasAuthority('ADMIN')")
	public Page<User> getUsers(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, 
			@RequestParam(defaultValue = "id") String sortBy) {
		return userSrv.find(page, size, sortBy);
	}
	
	// -------------------------------------------- FIND USER by ID
	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping("/{userId}")
	public User findById(@PathVariable UUID userId) {
		return userSrv.findById(userId);

	}
	
	// -------------------------------------------- UPDATE USER by ID
	@PreAuthorize("hasAuthority('ADMIN')")
	@PutMapping("/{userId}")
	@ResponseStatus(HttpStatus.OK)
	public User updateUser(@PathVariable UUID userId, @RequestBody UserRequestPayload body) {
		return userSrv.findByIdAndUpdate(userId, body);
	}
	
	// -------------------------------------------- DELETE USER by ID
	@PreAuthorize("hasAuthority('ADMIN')")
	@DeleteMapping("/{userId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteUser(@PathVariable UUID userId) {
		userSrv.findByIdAndDelete(userId);
	}

}

