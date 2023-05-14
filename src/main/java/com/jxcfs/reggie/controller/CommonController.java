package com.jxcfs.reggie.controller;

import com.jxcfs.reggie.common.R;
import com.jxcfs.reggie.service.CommonService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 文件上传和下载
 */
@RestController
@RequestMapping("/common")
public class CommonController {

    @Resource
    private CommonService commonService;

    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){
        try {
            return commonService.upload(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 文件下载
     * @param name
     * @param response
     * @return
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
        try {
            commonService.download(name, response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
