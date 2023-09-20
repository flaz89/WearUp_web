package com.wearup.wearup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.wearup.wearup.security.AuthController;
import com.wearup.wearup.user.User;
import com.wearup.wearup.user.User_Repository;
import com.wearup.wearup.user.User_Role;
import com.wearup.wearup.user.User_Service;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class WearUpBackEndApplication {
	
	@Autowired
	private User_Service userSrv;
	
	@Autowired
	private User_Repository userRepo;
	
	@Autowired
	PasswordEncoder bcrypt;
	
	public static void main(String[] args) {
		SpringApplication.run(WearUpBackEndApplication.class, args);
		
		System.out.println("INIZIAMO");
	}
	
	
	@PostConstruct
    public void init() {
        User existingAdmin = userSrv.findByEmail("info@wup.com");
        if (existingAdmin == null) {
            User admin = new User();
            admin.setName("admin");
            admin.setSurname("admin");
            admin.setProfilePicture("https://w7.pngwing.com/pngs/306/70/png-transparent-computer-icons-management-admin-silhouette-black-and-white-neck-thumbnail.png");
            admin.setEmail("info@wup.com");
            admin.setRole(User_Role.ADMIN);
            admin.setPassword(bcrypt.encode("101010"));
            
            userRepo.save(admin);
        }
    }
}
