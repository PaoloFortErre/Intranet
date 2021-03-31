package com.erretechnology.intranet.models;

import javax.persistence.*;

@Entity
@Table(name = "filePdf")
public class FilePdf{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private String settore;
	private String nome;
	private long timestamp;
	@Lob
    private byte[] data;
	@ManyToOne
	@JoinColumn(name = "id_autore")
	private UtenteDatiPersonali autore;
	private boolean visibile;
	private String descrizione; 
	private long timestampEliminazione;

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

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String nome) {
		this.descrizione = nome;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public long getTimestampEliminazione() {
		return timestampEliminazione;
	}

	public void setTimestampEliminazione(long timestampEliminazione) {
		this.timestampEliminazione = timestampEliminazione;
	}
}
