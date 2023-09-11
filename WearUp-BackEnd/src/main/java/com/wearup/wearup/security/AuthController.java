package com.wearup.wearup.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.wearup.wearup.brand.Brand;
import com.wearup.wearup.brand.Brand_Service;
import com.wearup.wearup.brand.payloads.BrandRequestPayload;
import com.wearup.wearup.exception.UnauthorizedException;
import com.wearup.wearup.uploadCloudinary.Cloudinary_Service;
import com.wearup.wearup.user.User;
import com.wearup.wearup.user.User_Service;
import com.wearup.wearup.user.payloads.LoginSuccessfullPayload;
import com.wearup.wearup.user.payloads.UserLoginPayload;
import com.wearup.wearup.user.payloads.UserRequestPayload;

@RestController
@RequestMapping("/auth")
public class AuthController {
	@Autowired
	private User_Service userSrv;
	
	@Autowired
	private Brand_Service brandSrv;
	
	@Autowired
	private JWTTools jwtTools;

	@Autowired
	PasswordEncoder bcrypt;
	
	@Autowired
	private Cloudinary_Service cloudSrv;
	
	// ---------------------------------------------- UPLOAD USER PROFILE PICTURE
	@PostMapping("/upload-user-image")
    public ResponseEntity<String> uploadUserImage(@RequestParam("file") MultipartFile file) {
        try {
        	String originalFilename = file.getOriginalFilename();
            String fileExtension = "";
            
            if (originalFilename != null && originalFilename.lastIndexOf(".") > 0) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
            }
            
            List<String> allowedExtensions = Arrays.asList("jpg", "jpeg", "png");
            
            if (!allowedExtensions.contains(fileExtension)) {
                return ResponseEntity.badRequest().body("Invalid file extension. Allowed extensions are .jpg, .jpeg, .png");
            }
        	
        	String folderName = "WearUp/user-images";
            String url = cloudSrv.uploadFile(file, folderName);
            return ResponseEntity.ok(url);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Failed to upload file: " + e.getMessage());
        }
    }
	
	// ---------------------------------------------- USER

	@PostMapping("/register")
	@ResponseStatus(HttpStatus.CREATED)
	public User saveUser(@RequestBody @Validated UserRequestPayload body) {
		body.setPassword(bcrypt.encode(body.getPassword()));
		User created = userSrv.create(body);

		return created;
	}

	
	// ---------------------------------------------- LOGIN
	
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
	
	// ---------------------------------------------- UPLOAD BRAND PROFILE PICTURE
		@PostMapping("/upload-brand-image")
	    public ResponseEntity<String> uploadBrandImage(@RequestParam("file") MultipartFile file) {
	        try {
	        	String originalFilename = file.getOriginalFilename();
	            String fileExtension = "";
	            
	            if (originalFilename != null && originalFilename.lastIndexOf(".") > 0) {
	                fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
	            }
	            
	            List<String> allowedExtensions = Arrays.asList("jpg", "jpeg", "png");
	            
	            if (!allowedExtensions.contains(fileExtension)) {
	                return ResponseEntity.badRequest().body("Invalid file extension. Allowed extensions are .jpg, .jpeg, .png");
	            }
	        	
	        	String folderName = "WearUp/brand-images";
	            String url = cloudSrv.uploadFile(file, folderName);
	            return ResponseEntity.ok(url);
	        } catch (IOException e) {
	            return ResponseEntity.badRequest().body("Failed to upload file: " + e.getMessage());
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
