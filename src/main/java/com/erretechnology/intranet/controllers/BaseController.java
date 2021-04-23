package com.erretechnology.intranet.controllers;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.erretechnology.intranet.models.ElementiMyLife;
import com.erretechnology.intranet.models.ElementiMyWork;
import com.erretechnology.intranet.models.FileImmagine;
import com.erretechnology.intranet.models.Log;
import com.erretechnology.intranet.models.Notifica;
import com.erretechnology.intranet.models.UtenteDatiPersonali;
import com.erretechnology.intranet.services.*;

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
	@Autowired
	protected ServiceEventoWork serviceEventoWork;
	@Autowired
	protected ServiceLibro serviceLibro;
	@Autowired
	protected ServiceLinkedin serviceLinkedin;
	@Autowired
	protected ServiceNews serviceNews;


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
	
	protected FileImmagine salvaImmagine(MultipartFile immagine, UtenteDatiPersonali utenteLoggato) throws IOException {
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
			return img;
		}else {
			return serviceFileImmagine.getImmagineByData(img.getData());
		}
	}

	@Async
	protected CompletableFuture<List<ElementiMyWork>> findWorkElement(List<ElementiMyWork> list, String tipo){
		return CompletableFuture.completedFuture(list.stream().filter(x -> x.getTipo().equals(tipo)).collect(Collectors.toList()));
	}

	@Async
	protected CompletableFuture<List<ElementiMyLife>> findLifeElement(List<ElementiMyLife> list, String tipo){
		return CompletableFuture.completedFuture(list.stream().filter(x -> x.getTipo().equals(tipo)).collect(Collectors.toList()));
	}


}
