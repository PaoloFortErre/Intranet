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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.erretechnology.intranet.models.Cliente;
import com.erretechnology.intranet.models.UtenteDatiPersonali;

@Controller
@RequestMapping("/my-work/clienti")
public class ClienteController extends BaseController{

	@GetMapping("/aggiungi")
	public String form(Model model) {
		model.addAttribute("cliente", new Cliente()); 
		return "clienteForm";
	}

	@PostMapping(value = "/insert")
	public String post(@ModelAttribute("cliente") Cliente cliente, @RequestParam(required=false) MultipartFile immagine, 
			@RequestParam("date") String date, HttpSession session, ModelMap model) throws Exception {

		//trasformazione data in timestamp
		DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM");
		YearMonth yearMonth = YearMonth.parse(date, dateFormat);
		LocalDate parsedDate = yearMonth.atDay(1);
		Timestamp timestamp = Timestamp.valueOf(parsedDate.atStartOfDay());
		cliente.setDataInizio(timestamp.getTime());
		cliente.setVisibile(true);
		int idUser = Integer.parseInt(session.getAttribute("id").toString());
		UtenteDatiPersonali utenteLoggato= serviceDatiPersonali.findById(idUser);
		
		//controllo se è stata passata un'immagine
		if(!immagine.getOriginalFilename().isEmpty()) 
			cliente.setLogo(salvaImmagine(immagine, utenteLoggato));

		serviceCliente.save(cliente);
		saveLog("inserito un nuovo cliente", utenteLoggato);
		return "redirect:/my-work/";
	}

	@RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
	public String update(@PathVariable int id, String nome, String dataInizio, @RequestParam(required=false) MultipartFile immagine, 
			HttpSession session, Model model) throws Exception {
		Cliente cliente = serviceCliente.findById(id);
		cliente.setNome(nome);

		//trasformazione data in timestamp
		DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM");
		YearMonth yearMonth = YearMonth.parse(dataInizio, dateFormat);
		LocalDate parsedDate = yearMonth.atDay(1);
		Timestamp timestamp = Timestamp.valueOf(parsedDate.atStartOfDay());
		cliente.setDataInizio(timestamp.getTime());
		int idUser = Integer.parseInt(session.getAttribute("id").toString());
		UtenteDatiPersonali utenteLoggato= serviceDatiPersonali.findById(idUser);

		//controllo se è stata passata un'immagine
		if(!immagine.getOriginalFilename().isEmpty()) 
			cliente.setLogo(salvaImmagine(immagine, utenteLoggato));

		serviceCliente.save(cliente);
		model.addAttribute("cliente", cliente);
		saveLog("modificato le informazioni di un cliente",utenteLoggato);
		return "redirect:/my-work/";
	}

	@RequestMapping("/delete/{id}")
	public String delete(@PathVariable int id, HttpSession session) {
		Cliente cliente = serviceCliente.findById(id);
		cliente.setVisibile(false);
		cliente.setTimestampEliminazione(Instant.now().getEpochSecond());
		serviceCliente.save(cliente);
		saveLog("cancellato un nuovo cliente", serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString())));
		return "redirect:/my-work/";
	}

	@RequestMapping("/cancella")
	public String cancellaCliente(int id, HttpSession session) {
		serviceCliente.deleteById(id);
		return "redirect:/profilo/mostra-eliminati";
	}

	@RequestMapping("/ripristina")
	public String ripristinaCliente(int id, HttpSession session) {
		Cliente cliente = serviceCliente.findById(id);
		cliente.setVisibile(true);
		cliente.setTimestampEliminazione(0);
		serviceCliente.save(cliente);
		return "redirect:/profilo/mostra-eliminati";
	}
}
