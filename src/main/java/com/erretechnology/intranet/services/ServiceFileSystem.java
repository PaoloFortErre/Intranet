package com.erretechnology.intranet.services;

import org.springframework.web.multipart.MultipartFile;

public interface ServiceFileSystem {
	public void saveImage(String folder, MultipartFile imageFile) throws Exception;
}
