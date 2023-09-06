package com.wearup.wearup.brand;

import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.wearup.wearup.user.User_Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "brands")
@Data
@NoArgsConstructor
public class Brand implements UserDetails{

	@Id
	@GeneratedValue
	private long id;
	@Column(nullable = false, unique = true)
	private String brandName;
	private String address;
	private String city;
	private String phoneNumber;
	@Column(nullable = false, unique = true)
	private String vatNumber;
	@Column(nullable = false, unique = true)
	private String email;
	private String password;
	@Enumerated(EnumType.STRING)
	private User_Role role; 
	private String profilePicture;
	
	public Brand(String brandName, String address, String city, String phoneNumber, String vATnumber, String email,
			String password) {
		this.brandName = brandName;
		this.address = address;
		this.city = city;
		this.phoneNumber = phoneNumber;
		this.vatNumber = vATnumber;
		this.email = email;
		this.password = password;
		this.role = User_Role.BRAND;
		this.profilePicture = "https://res.cloudinary.com/wearup/image/upload/v1693993428/WearUp/images/WearUp_Logo_Color_profile-picture_hvac5z.png";
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority(role.name()));
	}

	@Override
	public String getUsername() {
		return this.email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
	
	
}
