package com.erretechnology.intranet.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ImmagineController {
	private String path = "/com/erretechnology/intranet/images/";
	
	@GetMapping(value = "/image/{name}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
	public @ResponseBody byte[] getImage(@PathVariable String name) throws IOException {
	    InputStream in = getClass().getResourceAsStream(path + name);
	    return IOUtils.toByteArray(in);
	}

	public void saveImage(MultipartFile multipartFile) throws IOException {
		String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        Path uploadPath = Paths.get(path);
         
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
       
        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath);
        } catch (IOException ioe) {        
            throw new IOException("Non è possibile salvare l'immagine: " + ioe.getMessage());
        }      
    }
}
