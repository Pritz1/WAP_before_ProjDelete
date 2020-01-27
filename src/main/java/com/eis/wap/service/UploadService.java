package com.eis.wap.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface UploadService {
	public List<String> uploadExcel(MultipartFile file);
}