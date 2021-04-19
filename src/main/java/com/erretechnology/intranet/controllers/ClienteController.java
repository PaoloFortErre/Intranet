package com.erretechnology.intranet.controllers;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.erretechnology.intranet.models.Cliente;
import com.erretechnology.intranet.models.FileImmagine;
import com.erretechnology.intranet.models.UtenteDatiPersonali;

@Controller
@RequestMapping("cliente")
public class ClienteController extends BaseController{

	@GetMapping("/new")
	public String form(Model model) {
		model.addAttribute("cliente", new Cliente()); 
		return "clienteForm";
	}

	@PostMapping(value = "/insert")
	public String post(@ModelAttribute("cliente") Cliente cliente, @RequestParam(required=false) MultipartFile immagine, 
			@RequestParam("date") String date, HttpSession session, ModelMap model) throws Exception {

		//formatting date
		DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM");
		YearMonth yearMonth = YearMonth.parse(date, dateFormat);
		LocalDate parsedDate = yearMonth.atDay(1);
		Timestamp timestamp = Timestamp.valueOf(parsedDate.atStartOfDay());
		cliente.setDataInizio(timestamp.getTime());
		cliente.setVisibile(true);
		int idUser = Integer.parseInt(session.getAttribute("id").toString());
		UtenteDatiPersonali utenteLoggato= serviceDatiPersonali.findById(idUser);
		
		if(!immagine.getOriginalFilename().isEmpty()) {
			FileImmagine img = new FileImmagine();
			if(compressImage(immagine, 0.5f).length == 0)
				img.setData(immagine.getBytes());
			else
				img.setData(compressImage(immagine, 0.5f));
			img.setAutore(utenteLoggato);
			img.setTimestamp(Instant.now().getEpochSecond());
			img.setNomeFile(StringUtils.cleanPath(immagine.getOriginalFilename()));
			serviceFileImmagine.insert(img);
			cliente.setLogo(img);

		}

		serviceCliente.save(cliente);
		saveLog("inserito un nuovo cliente", utenteLoggato);
		return "redirect:/myWork/";
	}

	@RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable int id, Model model) {
		Cliente cliente = serviceCliente.findById(id);
		model.addAttribute("cliente", cliente);
		Timestamp date = new Timestamp(cliente.getDataInizio());
		model.addAttribute("date", date);
		return "clienteFormUpdate";
	}

	@RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
	public String update(@PathVariable int id, String nome, String dataInizio, @RequestParam(required=false) MultipartFile immagine, 
			HttpSession session, Model model) throws Exception {
		Cliente cliente = serviceCliente.findById(id);
		cliente.setNome(nome);

		DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM");
		YearMonth yearMonth = YearMonth.parse(dataInizio, dateFormat);
		LocalDate parsedDate = yearMonth.atDay(1);
		Timestamp timestamp = Timestamp.valueOf(parsedDate.atStartOfDay());
		cliente.setDataInizio(timestamp.getTime());
		int idUser = Integer.parseInt(session.getAttribute("id").toString());
		UtenteDatiPersonali utenteLoggato= serviceDatiPersonali.findById(idUser);

		if(!immagine.getOriginalFilename().isEmpty()) {
			FileImmagine img = new FileImmagine();			
			if(compressImage(immagine, 0.5f).length == 0)
				img.setData(immagine.getBytes());
			else
				img.setData(compressImage(immagine, 0.5f));
			if(!serviceFileImmagine.contains(img.getData())) {
				
				img.setAutore(utenteLoggato);
				img.setTimestamp(Instant.now().getEpochSecond());
				img.setNomeFile(StringUtils.cleanPath(immagine.getOriginalFilename()));
				serviceFileImmagine.insert(img);
				cliente.setLogo(img);
			}else {
				cliente.setLogo(serviceFileImmagine.getImmagineByData(img.getData()));
			}

		}

		serviceCliente.save(cliente);
		model.addAttribute("cliente", cliente);
		saveLog("modificato le informazioni di un cliente",utenteLoggato);
		return "redirect:/myWork/";
	}

	@RequestMapping("/delete/{id}")
	public String delete(@PathVariable int id, HttpSession session) {
		Cliente cliente = serviceCliente.findById(id);
		cliente.setVisibile(false);
		cliente.setTimestampEliminazione(Instant.now().getEpochSecond());
		serviceCliente.save(cliente);
		saveLog("cancellato un nuovo cliente", serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString())));
		return "redirect:/myWork/";
	}
	
	@RequestMapping("/cancellaCliente")
	public String cancellaCliente(int id, HttpSession session) {
		serviceCliente.deleteById(id);
		return "redirect:/profile/mostraEliminati";
		}
	
	@RequestMapping("/ripristinaCliente")
	public String ripristinaCliente(int id, HttpSession session) {
		Cliente cliente = serviceCliente.findById(id);
		cliente.setVisibile(true);
		cliente.setTimestampEliminazione(0);
		serviceCliente.save(cliente);
		return "redirect:/profile/mostraEliminati";
		}
}
