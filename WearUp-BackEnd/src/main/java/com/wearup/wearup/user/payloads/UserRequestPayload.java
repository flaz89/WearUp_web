package com.wearup.wearup.user.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserRequestPayload {
	@NotNull(message = "Name is required")
	@Size(min = 3, max = 30, message = "\n"
			+ "must have a minimum of 3 characters and a maximum of 30")
	private String name;
	@NotNull(message = "Surname is required")
	private String surname;
	@NotNull(message = "Email is required")
	@Email(message = "Incorrect email entered")
	private String email;
	@NotNull(message = "Password is required")
	private String password;
}



