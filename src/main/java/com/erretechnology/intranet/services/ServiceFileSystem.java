package com.erretechnology.intranet.services;

import org.springframework.web.multipart.MultipartFile;

public interface ServiceFileSystem {
	public String saveImage(String folder, MultipartFile imageFile, int idUser) throws Exception;
	public void deleteImage(String subFolder, String fileName);
}
