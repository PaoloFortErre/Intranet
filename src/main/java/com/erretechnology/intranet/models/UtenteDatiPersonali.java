package com.erretechnology.intranet.models;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "DatiUtente")
public class UtenteDatiPersonali implements Serializable, MyWorkBean{
	private static final long serialVersionUID = -828732162220395783L;
	@Id
	@Column(name="id") 
	private int id;
	@MapsId	
	@OneToOne()
	@JoinColumn(name = "id_user")
	private Utente utente;
	@Column(length = 500)
	private String descrizione;
	private long dataNascita;
	private String nome;
	private String cognome;
	@OneToOne()
	@JoinColumn(name = "settore")
	private Settore settore;
	private Boolean passwordCambiata;
	@OneToOne()
	@JoinColumn(name = "id_immagine")
	private FileImmagine immagine;
	private boolean visualizzaDataNascita;
	@ManyToMany(mappedBy = "setUtenti", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Notifica> setNotifiche;
	@ManyToMany(mappedBy = "setUtenti", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Post> setPostPiaciuti;


	public List<Post> getSetPostPiaciuti() {
		return setPostPiaciuti;
	}
	public void setSetPostPiaciuti(List<Post> setPostPiaciuti) {
		this.setPostPiaciuti = setPostPiaciuti;
	}
	
	public void addUtente(Post p) {
		setPostPiaciuti.add(p);
	}
	
	public void removeUtente(Post p) {
		setPostPiaciuti.remove(p);
	}
	
	public List<Notifica> getSetNotifiche() {
		return setNotifiche;
	}
	public void setSetNotifiche(List<Notifica> setNotifiche) {
		this.setNotifiche = setNotifiche;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public Boolean getPasswordCambiata() {
		return passwordCambiata;
	}

	public void setPasswordCambiata(Boolean passwordCambiata) {
		this.passwordCambiata = passwordCambiata;
	}

	public UtenteDatiPersonali(){	}

	public long getDataNascita() {
		return dataNascita;
	}

	public void setDataNascita(long dataNascita) {
		this.dataNascita = dataNascita;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public Utente getUtente() {
		return utente;
	}

	public void setUtente(Utente utente) {
		this.utente = utente;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Settore getSettore() {
		return settore;
	}

	public void setSettore(Settore settore) {
		this.settore = settore;
	}

	public boolean isPasswordCambiata() {
		return passwordCambiata;
	}

	public void setPasswordCambiata(boolean passwordCambiata) {
		this.passwordCambiata = passwordCambiata;
	}
	public boolean isVisualizzaDataNascita() {
		return visualizzaDataNascita;
	}
	public void setVisualizzaDataNascita(boolean visualizzaDataNascita) {
		this.visualizzaDataNascita = visualizzaDataNascita;
	}
	public FileImmagine getImmagine() {
		return immagine;
	}
	public void setImmagine(FileImmagine immagine) {
		this.immagine = immagine;
	}
	public void addNotifica(Notifica n) {
		setNotifiche.add(n);
		
	}
}
