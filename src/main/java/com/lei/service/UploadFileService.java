package com.lei.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UploadFileService {

    String uploadFile(MultipartFile multipartFile, String dir);

    String uploadFiles(List<MultipartFile> multipartFiles, String dir);

    String getViewUrl(String viewPrefixUrl, String viewSuffixUrl);
}
