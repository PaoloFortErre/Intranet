package com.erretechnology.intranet.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Video")
public class VideoDelGiorno {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String link;
	private String sottoTitolo;
	private String pagina;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getPagina() {
		return pagina;
	}
	public void setPagina(String pagina) {
		this.pagina = pagina;
	}
	public String getSottoTitolo() {
		return sottoTitolo;
	}
	public void setSottoTitolo(String sottoTitolo) {
		this.sottoTitolo = sottoTitolo;
	}
}
