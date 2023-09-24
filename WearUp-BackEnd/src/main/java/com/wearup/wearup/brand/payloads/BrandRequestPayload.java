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
	@NotNull(message = "Address is required")
	private String address;
	@NotNull(message = "City is required")
	private String city;
	@NotNull(message = "Country is required")
	private String country;
	private String phoneNumber;
	@NotNull(message = "VAT is required")
	private String vatNumber;
	@NotNull(message = "Email is required")
	private String email;
	@NotNull(message = "Password is required")
	private String password;
	private String profilePicture;
	@NotNull(message = "Brand Logo is required")
	private String brandLogo;
	private String  webSite;
}
