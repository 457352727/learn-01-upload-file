package com.lei.service.impl;

import com.lei.config.ServerConfig;
import com.lei.service.UploadFileService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
public class UploadFileServiceImpl implements UploadFileService {

    private DateTimeFormatter dtF;

    private String dirPrefix;

    private List<String> fileAllowTypes;

    private ServerConfig serverConfig;

    @Value("${upload.file.view.prefix.url}")
    private String VIEW_PREFIX_URL;

    @Autowired
    public UploadFileServiceImpl(@Qualifier("dirPrefix") String dirPrefix,
                                 @Qualifier("fileAllowTypes") List<String> fileAllowTypes,
                                 ServerConfig serverConfig) {
        this.dtF = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.dirPrefix = dirPrefix;
        this.fileAllowTypes = fileAllowTypes;
        this.serverConfig = serverConfig;
    }

    @Override
    public String uploadFile(MultipartFile multipartFile, String dir) {
        String originalFilename = multipartFile.getOriginalFilename();
        if (StringUtils.isBlank(originalFilename)) {
            return "the filename cannot be empty";
        }

        String originalContentType = multipartFile.getContentType();
        if (!fileAllowTypes.contains(originalContentType)) {
            return "content-type '" + originalContentType + "' upload not allowed";
        }

        String originalFilenameSuffix = originalFilename.substring(originalFilename.indexOf("."));
        String finalFilenamePrefix = UUID.randomUUID().toString();
        String finalFilename = finalFilenamePrefix + originalFilenameSuffix;

        String dtStr = LocalDateTime.now().format(dtF);
        String[] dtArr = dtStr.split("-");
        String yearStr = dtArr[0];
        String monthStr = dtArr[1];
        String dayStr = dtArr[2];

        String finalDir = dirPrefix + dir + "/" + yearStr + "/" + monthStr + "/" + dayStr + "/";
        String viewSuffixUrl = dir + "/" + yearStr + "/" + monthStr + "/" + dayStr + "/" + finalFilename;

        try {
            File targetDir = new File(finalDir);
            if (!targetDir.exists()) {
                boolean mkdirsResult = targetDir.mkdirs();
                if (!mkdirsResult) {
                    return "cannot create target dir";
                }
            }

            File file = new File(targetDir, finalFilename);
            multipartFile.transferTo(file);
        } catch (IOException e) {
            e.printStackTrace();
            return "fail";
        }
        return "success: " + getViewUrl(VIEW_PREFIX_URL, viewSuffixUrl);
    }

    @Override
    public String uploadFiles(List<MultipartFile> multipartFiles, String dir) {
        return null;
    }

    @Override
    public String getViewUrl(String viewPrefixUrl, String viewSuffixUrl) {
        String mappingStr = viewPrefixUrl.replace("**", viewSuffixUrl);
        String serverUrl = serverConfig.getUrl();
        if (!StringUtils.isBlank(mappingStr) && !StringUtils.isBlank(serverUrl)) {
            return serverUrl + mappingStr;
        }
        return null;
    }
}
