package com.lei.controller;

import com.lei.service.UploadFileService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/upload/file")
public class UploadFileController {

    private UploadFileService uploadFileService;

    @Value("${upload.file.default.second.dir}")
    private String DEFAULT_SECOND_DIR;

    @Autowired
    public UploadFileController(UploadFileService uploadFileService) {
        this.uploadFileService = uploadFileService;
    }

    @RequestMapping(value = "/single", method = RequestMethod.POST)
    public String uploadFile(@RequestParam("file") MultipartFile multipartFile,
                             @RequestParam(value = "dir", required = false) String dir) {
        if (multipartFile.isEmpty()) {
            return "the file size cannot be 0";
        }
        if (StringUtils.isBlank(dir)) {
            dir = DEFAULT_SECOND_DIR;
        }
        return uploadFileService.uploadFile(multipartFile, dir);
    }
}
