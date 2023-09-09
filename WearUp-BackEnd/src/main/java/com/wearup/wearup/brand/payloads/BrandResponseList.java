package com.wearup.wearup.brand.payloads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BrandResponseList {
	
	private String brandName;
	private String address;
	private String city;
	private String country;
	private String phoneNumber;
	private String email;
	private String profilePicture;
	private String  webSite;

}
