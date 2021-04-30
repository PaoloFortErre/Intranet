package com.erretechnology.intranet.models;

import java.util.Base64;

import javax.persistence.*;
/*
 * 	MODELLO PER TENERE TRACCIA DEI VARI SETTORI DI ERRE (PRO, TECHNOLOGY, LAB E GES)
 */
@Entity
@Table(name = "settore")
public class Settore {
	@Id
	private String nomeSettore;
	@Lob
	private String logo;
	@Lob
	private String logoBlack;
	
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getLogoBlack() {
		return logoBlack;
	}
	public void setLogoBlack(String logoBlack) {
		this.logoBlack = logoBlack;
	}
	public String getNomeSettore() {
		return nomeSettore;
	}
	public void setNomeSettore(String nomeSettore) {
		this.nomeSettore = nomeSettore;
	}
	
	public String getImgData(byte[] byteData) {
        return Base64.getMimeEncoder().encodeToString(byteData);
    }
}
