package com.erretechnology.intranet.models;

import javax.persistence.*;

@Entity
@Table(name = "filePdf")
public class FilePdf{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private String settore;
	private String nomeFile;
	@Lob
    private byte[] data;
	@ManyToOne
	@JoinColumn(name = "id_autore")
	private UtenteDatiPersonali autore;
	private boolean visibile;

	public UtenteDatiPersonali getAutore() {
		return autore;
	}

	public void setAutore(UtenteDatiPersonali autore) {
		this.autore = autore;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public String getNomeFile() {
		return nomeFile;
	}

	public void setNomeFile(String nomeFile) {
		this.nomeFile = nomeFile;
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getSettore() {
		return settore;
	}
	
	public void setSettore(String settore) {
		this.settore = settore;
	}

	public boolean isVisibile() {
		return visibile;
	}

	public void setVisibile(boolean visibile) {
		this.visibile = visibile;
	}
}
