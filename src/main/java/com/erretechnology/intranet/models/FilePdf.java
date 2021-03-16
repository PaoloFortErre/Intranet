package com.erretechnology.intranet.models;

import javax.persistence.*;

@Entity
@Table(name = "filePdf")
public class FilePdf {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	private String nomeFile;
	
	private String settore;
	@Lob
    private byte[] data;
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getNomeFile() {
		return nomeFile;
	}
	
	public void setNomeFile(String nomeFile) {
		this.nomeFile = nomeFile;
	}
	
	public String getSettore() {
		return settore;
	}
	
	public void setSettore(String settore) {
		this.settore = settore;
	}
	
	public byte[] getData() {
		return data;
	}
	
	public void setData(byte[] data) {
		this.data = data;
	}
}
