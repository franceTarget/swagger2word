package com.servingcloud.synergycloud.controller;

import com.servingcloud.synergycloud.service.DateToWordImpl;
import com.servingcloud.synergycloud.service.WordServiceImpl;
import com.servingcloud.synergycloud.service.dto.InterFaceInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@WebServlet(name = "firstServlet", urlPatterns = "/swagger-ui/export")
public class FirstServlet extends HttpServlet {
    @Autowired
    private WordServiceImpl wordService;
    @Autowired
    private DateToWordImpl dateToWord;
    @Value("${server.port:8080}")
    private String port;
    @Value("${swagger.api.url:/v2/api-docs}")
    private String apiUrl;

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
//        String swaggerUrl = "http://127.0.0.1:" + port + apiUrl;
        String swaggerUrl = "http://dev-spacexcloud-tms.servingcloud.com/v2/api-docs?group=1rest";
        List<InterFaceInfo> list = wordService.tableList(swaggerUrl);
        response.setHeader("content-type", "application/octet-stream");
        response.setContentType("application/octet-stream");
        // 下载文件能正常显示中文
        response.setHeader("Content-Disposition", "attachment;filename=" + "swagger2word.doc");
        OutputStream os = response.getOutputStream();
        try {
            dateToWord.toWord(list, os);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}