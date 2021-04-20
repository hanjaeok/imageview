package com.hanker.imageview.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;

@RestController
public class ImageController {

    // 이미지 파일은 각 날짜에 맞는 폴더에 저장
    // 파일명은 시간
    // 특정시간에 생성된 이미지파일 불러오기 (* 파일명 = yyyymmdd_HHmmssZ_A.png)
    // (파일명을 알 경우)
    @GetMapping(value="/image/view", produces= MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody byte[] getImage(@RequestParam("file_time") String fileTime, // yyyymmdd_HHmmssZ
                                         @RequestParam("value") String value) // A
            throws IOException{
        FileInputStream fis = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        String[] fileAr = fileTime.split("_");
        String filePath = fileAr[0];

        String fileDir = "D:\\Han\\sample\\" + filePath + "\\" + fileTime + "_" + value + ".png";

        try{
            fis = new FileInputStream(fileDir);
        } catch(FileNotFoundException e){
            e.printStackTrace();
        }

        int readCount = 0;
        byte[] buffer = new byte[1024];
        byte[] fileArray = null;

        try{
            while((readCount = fis.read(buffer)) != -1){
                baos.write(buffer, 0, readCount);
            }
            fileArray = baos.toByteArray();
            fis.close();
            baos.close();
        } catch(IOException e){
            throw new RuntimeException("File Error");
        }
        return fileArray;
    }

    // 특정 시간 이미지파일 불러오기
    // ( 파일명을 모를경우 )
    @GetMapping(value="/image/specView", produces=MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody byte[] getSpecImage(@RequestParam("specific_date") String specDt, // yyyymmdd_HHmmss
                                             @RequestParam("value") String value) // A or B
            throws IOException{

        FileInputStream fis = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        String[] fileAr = specDt.split("_");
        String filePath = fileAr[0];
        String tmpFile = fileAr[1].substring(0, 4); // HHmm

        String path = "D:\\Han\\sample\\" + filePath;
        // path 경로에서 "filePath_tmpFile"명과 일치하는 파일 찾기
        File directory = new File(path);
        FilenameFilter filter = new FilenameFilter(){
            public boolean accept(File f, String name){
                return name.startsWith(filePath + "_" + tmpFile);
            }
        };

        String[] sameFile = directory.listFiles(filter)[directory.listFiles(filter).length - 1].toString().split(("/".equals(File.separator))? File.separator : "\\\\");
        String fileName = sameFile[sameFile.length-1];

        String fileDir = "D:\\han\\sample\\" + filePath + "\\" + fileName;
        try{
            fis = new FileInputStream(fileDir);
        } catch(FileNotFoundException e){
            e.printStackTrace();
        }

        int readCount = 0;
        byte[] buffer = new byte[1024];
        byte[] fileArray = null;

        try{
            while((readCount = fis.read(buffer)) != -1){
                baos.write(buffer, 0, readCount);
            }
            fileArray = baos.toByteArray();
            fis.close();
            baos.close();
        } catch(IOException e){
            throw new RuntimeException("File Error");
        }

        return fileArray;
    }
}
