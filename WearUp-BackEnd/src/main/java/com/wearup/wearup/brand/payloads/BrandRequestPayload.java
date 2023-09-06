package com.wearup.wearup.brand.payloads;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BrandRequestPayload {
	@NotNull(message = "Name is required")
	private String brandName;
	private String address;
	private String city;
	private String phoneNumber;
	@NotNull(message = "VAT is required")
	private String vatNumber;
	@NotNull(message = "Email is required")
	private String email;
	@NotNull(message = "Password is required")
	private String password;
}
