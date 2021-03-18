package com.erretechnology.intranet.controllers;

import java.io.IOException;

import javax.servlet.http.HttpSession;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.erretechnology.intranet.models.FilePdf;
import com.erretechnology.intranet.models.UtenteDatiPersonali;

@Controller
@RequestMapping(value = "file")
public class UploadController extends BaseController{

	@GetMapping(value = "/")
	public ModelAndView uploadMAV() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("upload");
		mav.addObject("filePdf", new FilePdf());
		return mav;
	}

	@PostMapping(value = "/upload")
	public String uploadPdf(@RequestParam("file") MultipartFile file, RedirectAttributes attributes, 
			@ModelAttribute("filePdf") FilePdf filePdf, HttpSession session) {
		// check if file is empty
		if (file.isEmpty()) {
			attributes.addFlashAttribute("message", "Please select a file to upload.");
			return "redirect:/file/";
		}

		// normalize the file path
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());

		// save the file on the local file system
		try {
			filePdf.setNomeFile(fileName);
			filePdf.setData(file.getBytes());
			filePdf.setVisibile(true);
			serviceFilePdf.insert(filePdf);
			saveLog("Inserito un nuovo modulo", serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString())));
		} catch (IOException e) {
			e.printStackTrace();
		}

		// return success response
		attributes.addFlashAttribute("message", "You successfully uploaded " + fileName + '!');

		return "redirect:/file/";
	}
	
	@GetMapping(value = "/moduli")
	public ModelAndView downloadMAV(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("downloadPdf");
		mav.addObject("filePdf", serviceFilePdf.findAll());
		mav.addObject("user", serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString())));
		return mav;
	}
	
	@PostMapping(value = "/deleteFilePdf")
	public String deleteMessaggio(int id, HttpSession session) {
		UtenteDatiPersonali autore = serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString()));
		if(serviceFilePdf.findByAutore(autore).stream().filter(x-> x.getId() == id).count() > 0) {
			FilePdf pdf =serviceFilePdf.findById(id);
			pdf.setVisibile(false);
			serviceFilePdf.insert(pdf);
			saveLog("cancellato un modulo", autore);
			return "redirect:/file/moduli";
		} return "redirect:forbidden";
		
	}
	
	@GetMapping(value = "/downloadFile")
	public ResponseEntity<Resource> downloadFile(int id, HttpSession session) {
		FilePdf pdf = serviceFilePdf.findById(id);
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Disposition", String.format("attachment; filename=" + pdf.getNomeFile()));    
		saveLog("scaricato un pdf", serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString())));
		return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
                .body(new ByteArrayResource(pdf.getData()));
	}
}
