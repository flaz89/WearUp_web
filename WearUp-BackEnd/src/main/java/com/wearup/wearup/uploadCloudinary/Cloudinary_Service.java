package com.wearup.wearup.uploadCloudinary;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class Cloudinary_Service implements FileUpload{
	
	private final Cloudinary cloudinary;



	@Override
	public String upload3DModel(MultipartFile multipartFile, String folderName)
			throws IOException {
		Map<String, Object> uploadOptions = new HashMap<>();
	    uploadOptions.put("public_id", UUID.randomUUID().toString());
	    uploadOptions.put("folder", folderName);
	    uploadOptions.put("resource_type", "raw");
		
	    return cloudinary.uploader()
	            .upload(multipartFile.getBytes(), uploadOptions)
	            .get("url")
	            .toString();
	}
	

	@Override
	public String uploadFile(MultipartFile multipartFile, String folderName) throws IOException {
		Map<String, Object> uploadOptions = new HashMap<>();
	    uploadOptions.put("public_id", UUID.randomUUID().toString());
	    uploadOptions.put("folder", folderName);
	    
	    return cloudinary.uploader()
	            .upload(multipartFile.getBytes(), uploadOptions)
	            .get("url")
	            .toString();
	}


	@Override
	public String uploadCustomFile(MultipartFile multipartFile, String folderName, String textureType)
			throws IOException {
		Map<String, Object> uploadOptions = new HashMap<>();
		String publicId = textureType + "-" + UUID.randomUUID().toString();
	    uploadOptions.put("public_id", publicId);
	    uploadOptions.put("folder", folderName);
	    
	    return cloudinary.uploader()
	            .upload(multipartFile.getBytes(), uploadOptions)
	            .get("url")
	            .toString();
	}


	
	

	
	

}
