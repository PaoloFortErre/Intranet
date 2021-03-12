package com.erretechnology.intranet.services;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
@Service("fileSystemService")
public class ServiceFileSystemImpl implements ServiceFileSystem{

	@Override
	public void saveImage(String folder, MultipartFile imageFile) throws Exception {
		byte[] bytes = imageFile.getBytes();
		Path path = Paths.get(folder + imageFile.getOriginalFilename());
		Files.write(path, bytes);
		
	}

}
