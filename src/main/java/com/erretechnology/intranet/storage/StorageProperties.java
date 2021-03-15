package com.erretechnology.intranet.storage;

import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageProperties {

	private String commonPath = "/IntranetResources/";
	private String imagePath = commonPath + "photos/";
	
	public String getImagePath() {
		return imagePath;
	}
	
	
}
