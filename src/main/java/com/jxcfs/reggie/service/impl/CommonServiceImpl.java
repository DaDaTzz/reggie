package com.jxcfs.reggie.service.impl;

import com.jxcfs.reggie.common.R;
import com.jxcfs.reggie.service.CommonService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;


@Service
public class CommonServiceImpl implements CommonService {

    @Value("${reggie.path}")
    private String basePath;
    @Resource
    private HttpServletResponse response;

    /**
     * 文件上传
     * @param file
     * @return
     * @throws IOException
     */
    @Override
    public R<String> upload(MultipartFile file){
        String originalFilename = file.getOriginalFilename(); // 文件原始名
        String suffix = originalFilename.substring(originalFilename.lastIndexOf(".")); // 文件后缀
        String fileName = UUID.randomUUID().toString() + suffix;
        File dir = new File(basePath);
        if(!dir.exists()){
            dir.mkdirs();
        }
        try {
            file.transferTo(new File(basePath + fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return R.success(fileName);
    }

    /**
     * 文件下载
     * @param name
     * @param response
     */
    @Override
    public void download(String name, HttpServletResponse response){
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("image/jpeg");
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = fileInputStream.read(bytes)) != -1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
            outputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
