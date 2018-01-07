package com.mmall.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.mmall.service.IFileService;
import com.mmall.util.FTPUtil;

@Service("iFileServiceImpl")
public class FileServiceImpl implements IFileService {

	private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

	public String upload(MultipartFile file, String path) {
		String fileName = file.getOriginalFilename();
		// 擴展名 abc.jpg
		String fileExtensionName = fileName.substring(fileName.lastIndexOf("."));
		String uploadFileName = UUID.randomUUID().toString() + "." + fileExtensionName;
		logger.info("開始上船文件，上船文件的文件名稱:{},上船的路徑:{},新文件名:{}", fileName, path, uploadFileName);
		File fileDir = new File(path);
		if (!fileDir.exists()) {
			fileDir.setWritable(true);
			fileDir.mkdirs();
		}
		File targetFile = new File(path, uploadFileName);
		try {
			// 上傳成功
			file.transferTo(targetFile);
			// 上傳ftp
			FTPUtil.uploadFile(Lists.newArrayList(targetFile));
			// 上傳完以後刪除upload下面得文件
			targetFile.delete();
		} catch (IOException e) {
			logger.error("上傳文件異常", e);
			return null;
		}
		return targetFile.getName();
	}

}
