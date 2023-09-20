package com.wearup.wearup.user;


import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wearup.wearup.favorite.Favorite;
import com.wearup.wearup.security.AuthenticatedEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@JsonIgnoreProperties({ "password", "accountNonExpired", "authorities", "credentialsNonExpired", "accountNonLocked" })
public class User implements AuthenticatedEntity {
	@Id
	@GeneratedValue
	private UUID id;
	
	@Column(nullable = false)
	private String name;
	@Column(nullable = false)
	private String surname;
	@Column(nullable = false, unique = true)
	private String email;
	@Column(nullable = false)
	private String password;
	
	@Enumerated(EnumType.STRING)
	private User_Role role = User_Role.USER;
	
	private String profilePicture;
	private Date subscriptionDate;
	
	@OneToMany(mappedBy = "user")
	private List<Favorite> favorites;

	public User(String name, String surname, String email, String password, String profilePicture) {
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.password = password;
		this.role = User_Role.USER;
		this.profilePicture = (profilePicture != null) ? profilePicture : "https://res.cloudinary.com/wearup/image/upload/v1693993428/WearUp/images/WearUp_Logo_Color_profile-picture_hvac5z.png";
		this.subscriptionDate = new Date();

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
