package com.wearup.wearup.uploadCloudinary;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface FileUpload {
	String uploadFile(MultipartFile multipartFile, String folderName) throws IOException;

	String upload3DModel(MultipartFile multipartFile, String folderName) throws IOException;


}
