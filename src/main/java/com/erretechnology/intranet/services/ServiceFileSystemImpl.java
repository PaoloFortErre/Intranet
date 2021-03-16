package com.erretechnology.intranet.services;

import java.io.File;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.erretechnology.intranet.storage.StorageProperties;

@Service("fileSystemService")
public class ServiceFileSystemImpl implements ServiceFileSystem{
	@Autowired
	private StorageProperties destination;
	
	@Override
	public String saveImage(String subFolder, MultipartFile imageFile, int idUser) throws Exception {
		Long date = Instant.now().getEpochSecond();
		String extension = FilenameUtils.getExtension(imageFile.getOriginalFilename());
		String fileName = String.valueOf(date) + Integer.toString(idUser) + "." + extension;
		byte[] bytes = imageFile.getBytes();
		
		File directory = new File(destination.getImagePath() + subFolder);
		if (!directory.exists()){
	        directory.mkdirs();
	    }
		
		Path path = Paths.get(directory + "/" + fileName);

		Files.write(path, bytes);
		
		return fileName;
		
	}
	
	@Override
	public void deleteImage(String subFolder, String fileName) {
		File file = new File(destination.getImagePath() + subFolder + "/" + fileName); 
		if(file.delete()) 
        { 
            System.out.println("File deleted successfully"); 
        } 
        else
        { 
            System.out.println("Failed to delete the file"); 
        } 
	}
}
