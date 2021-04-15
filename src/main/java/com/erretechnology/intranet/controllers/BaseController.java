package com.erretechnology.intranet.controllers;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import com.erretechnology.intranet.models.Log;
import com.erretechnology.intranet.models.Notifica;
import com.erretechnology.intranet.models.UtenteDatiPersonali;
import com.erretechnology.intranet.repositories.RepositoryCliente;
import com.erretechnology.intranet.services.ServiceAforisma;
import com.erretechnology.intranet.services.ServiceAuthority;
import com.erretechnology.intranet.services.ServiceCinema;
import com.erretechnology.intranet.services.ServiceCliente;
import com.erretechnology.intranet.services.ServiceCommento;
import com.erretechnology.intranet.services.ServiceCommentoModificato;
import com.erretechnology.intranet.services.ServiceComunicazioniHR;
import com.erretechnology.intranet.services.ServiceElementiMyLife;
import com.erretechnology.intranet.services.ServiceElementiMyWork;
import com.erretechnology.intranet.services.ServiceEventoLife;
import com.erretechnology.intranet.services.ServiceFileImmagini;
import com.erretechnology.intranet.services.ServiceFilePdf;
import com.erretechnology.intranet.services.ServiceLog;
import com.erretechnology.intranet.services.ServiceNotifica;
import com.erretechnology.intranet.services.ServicePermesso;
import com.erretechnology.intranet.services.ServicePodcast;
import com.erretechnology.intranet.services.ServicePost;
import com.erretechnology.intranet.services.ServicePostModificato;
import com.erretechnology.intranet.services.ServiceRuolo;
import com.erretechnology.intranet.services.ServiceSettore;
import com.erretechnology.intranet.services.ServiceSondaggio;
import com.erretechnology.intranet.services.ServiceUtente;
import com.erretechnology.intranet.services.ServiceUtenteDatiPersonali;
import com.erretechnology.intranet.services.ServiceVideo;

public abstract class BaseController {
	@Autowired
	protected ServiceAuthority serviceAuthority;
	@Autowired
	protected ServiceCommento serviceCommento;
	@Autowired
	protected ServiceCommentoModificato serviceCommentoModificato;
	@Autowired
	protected ServiceFileImmagini serviceFileImmagine;
	@Autowired
	protected ServiceFilePdf serviceFilePdf;
	@Autowired
	protected ServiceLog serviceLog;
	@Autowired
	protected ServicePermesso servicePermesso;
	@Autowired
	protected ServicePost servicePost;
	@Autowired
	protected ServicePostModificato servicePostModificato;
	@Autowired
	protected ServiceRuolo serviceRuolo;
	@Autowired
	protected ServiceUtente serviceUtente;
	@Autowired
	protected ServiceUtenteDatiPersonali serviceDatiPersonali;
	@Autowired
	protected ServiceSettore serviceSettore;
	@Autowired
	protected ServicePodcast servicePodcast;
	@Autowired
	protected ServiceSondaggio serviceSondaggio;
	@Autowired
	protected ServiceComunicazioniHR serviceComunicazioni;
	@Autowired
	protected ServiceElementiMyWork serviceElementiMyWork;
	@Autowired
	protected ServiceElementiMyLife serviceElementiMyLife;
	@Autowired
	protected ServiceVideo serviceVideo;
	@Autowired
	protected ServiceNotifica serviceNotifica;
	@Autowired
	protected ServiceAforisma serviceAforisma;
	@Autowired
	protected ServiceCinema serviceCinema;
	@Autowired
	protected ServiceCliente serviceCliente;
	@Autowired
	protected ServiceEventoLife serviceEventoLife;

	protected void saveLog(String testo, UtenteDatiPersonali autore) {
		Log log = new Log();
		log.setTimestamp(Instant.now().getEpochSecond());
		log.setUtente(autore);
		log.setAzione(testo);
		serviceLog.save(log);
	}
	
	protected void notificaSingola(String testo, UtenteDatiPersonali utente, UtenteDatiPersonali utenteCreatore, String destinazione) {
		if(utente.equals(utenteCreatore)) return;
		Notifica n = new Notifica();
		n.setDescrizione(testo);
		n.setDestinazione(destinazione);
		n.setTimestamp(Instant.now().getEpochSecond());
		n.addUtente(utente);
		n.setUtente(utenteCreatore);
		serviceNotifica.save(n);
		utente.addNotifica(n);
		serviceDatiPersonali.save(utente);
		
	}
	
	protected void notificaTutti(String testo, UtenteDatiPersonali utenteCreatore, String destinazione) {
		Notifica n = new Notifica();
		n.setDescrizione(testo);
		n.setUtente(utenteCreatore);
		n.setDestinazione(destinazione);
		n.setTimestamp(Instant.now().getEpochSecond());
		serviceNotifica.save(n);
		List<UtenteDatiPersonali> utenti = serviceDatiPersonali.getAttivi();
		for(UtenteDatiPersonali u : utenti) {
			if(!u.equals(utenteCreatore)) {
				u.addNotifica(n);
				serviceDatiPersonali.save(u);
				n.addUtente(u);
			}
		}
		serviceNotifica.save(n);
	}
	
	protected void notificaSelezionati(String testo, String settore, UtenteDatiPersonali utenteCreatore, String destinazione) {
		Notifica n = new Notifica();
		n.setDescrizione(testo);
		n.setUtente(utenteCreatore);
		n.setDestinazione(destinazione);
		n.setTimestamp(Instant.now().getEpochSecond());
		serviceNotifica.save(n);
		List<UtenteDatiPersonali> utenti = serviceDatiPersonali.getAttivi().stream().filter(x->x.getSettore().getNomeSettore().equals(settore)).collect(Collectors.toList());
		for(UtenteDatiPersonali u : utenti) {
			if(!u.equals(utenteCreatore)) {
				u.addNotifica(n);
				serviceDatiPersonali.save(u);
				n.addUtente(u);
			}
		}
		serviceNotifica.save(n);
	}
	
	protected byte[] compressImage(MultipartFile mpFile, float qualita) {
	    float quality = qualita;
	    String imageName = mpFile.getOriginalFilename();
	    String imageExtension = imageName.substring(imageName.lastIndexOf(".") + 1);
	    ImageWriter imageWriter = ImageIO.getImageWritersByFormatName(imageExtension).next();
	    ImageWriteParam imageWriteParam = imageWriter.getDefaultWriteParam();
	    if(!imageExtension.equalsIgnoreCase("png")) {
	    	imageWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
	    	imageWriteParam.setCompressionQuality(quality);
	    	
	    }
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    ImageOutputStream imageOutputStream = new MemoryCacheImageOutputStream(baos);
	    imageWriter.setOutput(imageOutputStream);
	    BufferedImage originalImage = null;
	    try (InputStream inputStream = mpFile.getInputStream()) {
	        originalImage = ImageIO.read(inputStream); 
	    } catch (IOException e) {
	        return baos.toByteArray();
	    }
	    if(originalImage == null) return baos.toByteArray();
	    IIOImage image = new IIOImage(originalImage, null, null);
	    try {
	        imageWriter.write(null, image, imageWriteParam);
	    } catch (IOException e) {
	    } finally {
	        imageWriter.dispose();
	    }
	    return baos.toByteArray();
	}
}
