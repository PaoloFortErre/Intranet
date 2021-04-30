package com.erretechnology.intranet.controllers;

import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.util.Base64;
import java.util.Comparator;
import java.util.stream.Collectors;

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
import com.erretechnology.intranet.models.ComunicazioneHR;
import com.erretechnology.intranet.models.FilePdf;
import com.erretechnology.intranet.models.UtenteDatiPersonali;

@Controller
@RequestMapping(value = "my-work/comunicazioni")
public class UploadController extends BaseController{

	/*
	 * Funzione che inidirizza al form per aggiungere un modulo
	 */
	@GetMapping(value = "/moduli/aggiungi")
	public ModelAndView uploadMAV() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("upload");
		mav.addObject("filePdf", new FilePdf());
		return mav;
	}
	
	/*
	 * Funzione che permette di visualizzare tutte le comunicazione HR inserite e non cancellate
	 * Permette inoltre di aggiungere una nuova
	 */

	@GetMapping(value = "/hr")
	public ModelAndView hrMAV(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("aggiungi_nuova_comunicazione");
		mav.addObject("comunicazioneHR", new ComunicazioneHR());
		mav.addObject("comunicazioni", serviceComunicazioni.getAll().stream().filter(x->x.isVisibile()).sorted(Comparator.comparingLong(ComunicazioneHR::getTimestamp).reversed()).collect(Collectors.toList()));
		mav.addObject("user", serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString())));
		return mav;
	}
	/*
	 * Funzione che permette di visualizzare tutti i moduli non cancellati
	 */
	@GetMapping (value= "/moduli")
	public ModelAndView moduli(HttpSession session) {
		UtenteDatiPersonali u  = serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString()));
		ModelAndView mav = new ModelAndView();
		
	
		mav.addObject("user", u);
		mav.addObject("filePdf", serviceFilePdf.getAllVisible());
		mav.setViewName("moduli");
		return mav;
	}


	/*
	 * Funzione per l'inserimento fisico dei moduli in DB 
	 */
	@PostMapping(value = "/moduli/upload")
	public String uploadPdf(@RequestParam("file") MultipartFile file, RedirectAttributes attributes, 
			@ModelAttribute("filePdf") FilePdf filePdf, HttpSession session) throws Exception{
		UtenteDatiPersonali u =  serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString()));
		// la funzione restituisce il nome del file eliminando il path
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		filePdf.setNome(fileName);
		filePdf.setTimestamp(Instant.now().getEpochSecond());
		filePdf.setData(getImgData(file.getBytes()));
		filePdf.setVisibile(true);
		filePdf.setAutore(u);
		serviceFilePdf.insert(filePdf);
		saveLog("inserito un nuovo modulo", u );
		notificaSelezionati("ha inserito un nuovo modulo!", filePdf.getSettore().getNomeSettore(), u, "Moduli");

		return "redirect:/my-work/comunicazioni/moduli/";
	}


	/*
	 * Funzione per l'inserimento fisico delle comunicazioni HR in DB 
	 */
	@PostMapping(value = "/hr/upload")
	public String uploadPdfHR(@RequestParam("file") MultipartFile file, RedirectAttributes attributes, 
			@ModelAttribute("comunicazioneHR")  ComunicazioneHR filePdfHR, HttpSession session) throws Exception{

		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		// la funzione restituisce il nome del file eliminando il path
		UtenteDatiPersonali u = serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString()));
		filePdfHR.setNome(fileName);
		filePdfHR.setData(getImgData(file.getBytes()));
		filePdfHR.setTimestamp(Instant.now().getEpochSecond());
		filePdfHR.setUtente(u);
		filePdfHR.setVisibile(true);
		serviceComunicazioni.save(filePdfHR);
		saveLog("inserito una nuova Comunicazione HR", u);
		notificaTutti("ha inserito una nuova ComunicazioneHR!", u, "HR");

		return "redirect:/my-work/comunicazioni/hr";
	}	

	/*
	 *  Funzione per il soft delete di un modulo
	 */
	@PostMapping(value = "/moduli/delete")
	public String deleteMessaggio(int id, HttpSession session) throws Exception {
		UtenteDatiPersonali autore = serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString()));
		if( autore.getUtente().getSetPermessi().contains(servicePermesso.findById("GM"))) {
			FilePdf pdf =serviceFilePdf.findById(id);
			pdf.setVisibile(false);
			pdf.setTimestampEliminazione(Instant.now().getEpochSecond());

			serviceFilePdf.insert(pdf);
			saveLog("cancellato un modulo", autore);
			return "redirect:/my-work/comunicazioni/moduli/";
		} 
		throw new Exception("Non hai i permessi per svolgere quest'azione");

	}
	
	/*
	 * Funzione per il soft delete di una comunicazione HR
	 */

	@PostMapping(value = "/hr/delete")
	public String deleteComunicazioneHR(int id, HttpSession session) throws Exception {
		UtenteDatiPersonali autore = serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString()));
		if( autore.getUtente().getSetPermessi().contains(servicePermesso.findById("GHR"))) {
			ComunicazioneHR pdf =serviceComunicazioni.findById(id);
			pdf.setVisibile(false);
			pdf.setTimestampEliminazione(Instant.now().getEpochSecond());
			serviceComunicazioni.save(pdf);
			
			saveLog("cancellato una comunicazione hr", autore);
			return "redirect:/my-work/comunicazioni/hr";
		} 
		throw new Exception("Non hai i permessi per svolgere quest'azione");
	}

	/*
	 * Funzione per richiamare il download su dispositivo di un modulo
	 */

	@GetMapping(value = "/moduli/download")
	public ResponseEntity<Resource> downloadModulo(int id, HttpSession session) throws UnsupportedEncodingException {
		FilePdf pdf = serviceFilePdf.findById(id);
		return downloadFile(pdf.getData(), pdf.getNome(), session);

	}
	/*
	 * Funzione per richiamare il download su dispositivo di una comunicazione HR
	 */
	@GetMapping(value = "/hr/download")
	public ResponseEntity<Resource> downloadHR(int id, HttpSession session) throws UnsupportedEncodingException {
		ComunicazioneHR pdf = serviceComunicazioni.findById(id);
		return downloadFile(pdf.getData(), pdf.getNome(), session);
	}


	/*
	 * Funzione di downolad vera e propria
	 */
	private ResponseEntity<Resource> downloadFile(String data, String nome, HttpSession session) throws UnsupportedEncodingException {

		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Disposition", String.format("attachment; filename=" + nome));    
		saveLog("scaricato il modulo " + nome, serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString())));
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
				.body(new ByteArrayResource(Base64.getMimeDecoder().decode(data)));
	}

	/*
	 * Funzione che permette a un utente di visualizare tutte le comunicazioni hr precedentemente inserite
	 */
	@GetMapping(value = "/hr/elenco")
	public ModelAndView listComunicazioni() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("comunicazioniList");
		mav.addObject("comunicazioni", serviceComunicazioni.getAllVisible());
		return mav;
	}
	
	/*
	 * Funzione per la cancellazione fisica di una comunicazione HR, solo per admin
	 */
	
	@GetMapping(value ="/hr/cancella")
	public String eliminaComunicazioneHR(HttpSession session, int id) {
		ComunicazioneHR hr = serviceComunicazioni.findById(id);
		serviceComunicazioni.remove(hr);
		return "redirect:/profilo/mostra-eliminati";
	}
	
	/*
	 * Funzione per il ripristino di una comunicazione HR, solo per admin
	 */
	
	@GetMapping(value ="/hr/ripristina")
	public String ripristinaComunicazioneHR(HttpSession session, int id) {
		ComunicazioneHR hr = serviceComunicazioni.findById(id);
		hr.setVisibile(true);
		hr.setTimestampEliminazione(0);
		serviceComunicazioni.save(hr);;
		return "redirect:/profilo/mostra-eliminati";
	}
	
	/*
	 * Funzione per la cancellazione fisica di un modulo, solo per admin
	 */
	
	@GetMapping(value ="/moduli/cancella")
	public String eliminaModulo(HttpSession session, int id) {
		FilePdf file = serviceFilePdf.findById(id);
		serviceFilePdf.remove(file);
		return "redirect:/profilo/mostra-eliminati";
	}
	
	/*
	 * Funzione per il ripristino di un modulo, solo per admin
	 */
	
	@GetMapping(value ="/moduli/ripristina")
	public String ripristinaModulo(HttpSession session, int id) {
		FilePdf file = serviceFilePdf.findById(id);
		file.setVisibile(true);
		file.setTimestampEliminazione(0);
		serviceFilePdf.insert(file);;
		return "redirect:/profilo/mostra-eliminati";
	}
}
