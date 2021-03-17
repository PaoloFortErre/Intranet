package com.erretechnology.intranet.models;

import java.util.Base64;

import javax.persistence.*;

@Entity
@Table(name = "settore")
public class Settore {
	@Id
	private String nomeSettore;
	@Lob
    private byte[] logo;
	
	public String getNomeSettore() {
		return nomeSettore;
	}
	public void setNomeSettore(String nomeSettore) {
		this.nomeSettore = nomeSettore;
	}
	public byte[] getLogo() {
		return logo;
	}
	public void setLogo(byte[] logo) {
		this.logo = logo;
	}
	
	public String getImgData(byte[] byteData) {
        return Base64.getMimeEncoder().encodeToString(byteData);
    }
}
