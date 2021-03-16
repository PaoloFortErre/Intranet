package com.erretechnology.intranet.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.erretechnology.intranet.storage.StorageProperties;

@RestController
public class FileController {
	@Autowired
	private StorageProperties destination;
	
	@GetMapping(value = "/image/{folder}/{name}")
	public @ResponseBody byte[] getImage(@PathVariable String name, @PathVariable String folder) throws IOException {

	    Path path = Paths.get(destination.getImagePath() + folder + "/"+ name);
	    try {
			return Files.readAllBytes(path);
		} catch (IOException e) {
			System.err.println("Non ho trovato l'immagine");
			
			InputStream in = getClass()
					.getResourceAsStream("/com/erretechnology/intranet/storage/" + "not_found_image.jpg");
			return IOUtils.toByteArray(in);
		}

	}
	
}
