package com.harbor.ossservice.controller;

import com.harbor.common.result.Result;
import com.harbor.ossservice.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/oss")
public class CommonController {
    @Autowired
    private AliOssUtil aliOssUtil;

    @RequestMapping("/upload")
    public Result<String> upload(MultipartFile file) {
        log.info("文件上传：{}", file.getOriginalFilename());
        try {
            String fileName = file.getOriginalFilename();
            fileName = fileName.substring(fileName.lastIndexOf("."));
            fileName = "harbor/harbor_" + UUID.randomUUID().toString() + fileName;
            String url = aliOssUtil.upload(file.getBytes(), fileName);
            log.info("文件上传成功：{}", url);
            return Result.success(url);
        } catch (IOException e) {
            log.error("文件上传失败：{}", e.getMessage());
            e.printStackTrace();
        }
        return Result.error(500, "上传失败");
    }

}
