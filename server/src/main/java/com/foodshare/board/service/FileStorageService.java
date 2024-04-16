// package com.foodshare.board.service;
//
// import com.foodshare.board.exception.FileStorageException;
//
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Service;
// import org.springframework.util.StringUtils;
// import org.springframework.web.multipart.MultipartFile;
//
// import java.io.IOException;
// import java.nio.file.Files;
// import java.nio.file.Path;
// import java.nio.file.Paths;
// import java.nio.file.StandardCopyOption;
//
// @Service
// public class FileStorageService {
//
// 	@Value("${file.upload-dir}")
// 	private String uploadDir;
// 	public String storeFile(MultipartFile file) throws FileStorageException {
// 		String fileName = StringUtils.cleanPath(System.currentTimeMillis() + "_" + file.getOriginalFilename());
// 		try {
// 			Path targetLocation = Paths.get(uploadDir).resolve(fileName);
// 			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
// 			return fileName;
// 		} catch (IOException ex) {
// 			throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
// 		}
// 	}
// }