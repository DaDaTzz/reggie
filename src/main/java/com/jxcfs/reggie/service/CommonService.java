package com.jxcfs.reggie.service;

import com.jxcfs.reggie.common.R;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface CommonService {
    R<String> upload(MultipartFile file) throws IOException;
    void download(String name, HttpServletResponse response) throws IOException;
}
