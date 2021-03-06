package com.pinyougou.shop.controller;

import com.pinyougou.entity.Result;
import com.pinyougou.utils.FastDFSClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UploadController {

    @Value("${FILE_SERVER_URL}")
    private String FILE_SERVER_URL;

    @RequestMapping("/upload")
    public Result upload(MultipartFile file){
        String filename = file.getOriginalFilename();
        String extname = filename.substring(filename.lastIndexOf(".") + 1);
        try{
            FastDFSClient fastDFSClient = new FastDFSClient("classpath:config/fdfs_client.conf");
            //执行上传处理
            String path = fastDFSClient.uploadFile(file.getBytes(), extname);

            //拼接返回的URL和ip地址

            String url = FILE_SERVER_URL + path;
            return new Result(true,url);
        }catch (Exception e){

            e.printStackTrace();
            return new Result(false,"上传失败");
        }
    }
}
