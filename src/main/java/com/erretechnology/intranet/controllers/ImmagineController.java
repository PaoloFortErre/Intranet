package com.erretechnology.intranet.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.erretechnology.intranet.services.ServiceFileSystem;

@RestController
public class ImmagineController {

	@Autowired
	private ServiceFileSystem fileSystemService;
	
	private String path = "/photos/";
	
	
	@PostMapping("/uploadImage")
	public String uploadImage(@RequestParam("imageFile") MultipartFile imagefile) {
		String returnValue = "";
		
		System.err.println("Entro nel controller");
		try {
			fileSystemService.saveImage("/photos/", imagefile);
			System.err.println("File caricato");
		} catch (Exception e) {
			System.err.println("Non riesco a caricare il file");
			//e.printStackTrace();
		}
		
		
		return "<h1>Caricata</h1>";
	}
	
	@GetMapping(value = "/image")
	public @ResponseBody byte[] getImage() throws IOException {
	    /*
		InputStream in = getClass()
	      .getResourceAsStream("/photos/orario.PNG");
	    */
	    Path path = Paths.get("/photos/orario.PNG");
	    try {
			return Files.readAllBytes(path);
		} catch (IOException e) {
			System.err.println("Non ho trovato l'immagine");
			
			InputStream in = getClass()
					.getResourceAsStream("/com/erretechnology/intranet/image/" + "not_found_image.jpg");
			return IOUtils.toByteArray(in);
		}
	    //return IOUtils.toByteArray(in);
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
