package com.controller;


import com.servicerc.DeleteFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import com.servicerc.UpdateFileService;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@RestController
public class FileUploadLogController {

	@Autowired
	UpdateFileService updateFileService;
	@Autowired
	DeleteFileService deleteFileService;

    @RequestMapping( value = "/fileUpload.do",consumes = "multipart/form-data", method = RequestMethod.POST)
    public String uploadFile(HttpServletRequest request) {
    	String value = "";
        try {
            String limitStr = "5000";
            if(request.getParameter("limitSize")!=null) {
                limitStr = request.getParameter("limitSize");
            }
            long limitSize = 5000;

            limitSize = Long.parseLong(limitStr);

            String savePath=request.getParameter("savePath");
	            CommonsMultipartResolver MultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
	            
	            if (MultipartResolver.isMultipart(request)) {
	                MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
	                Iterator<String> iter = multiRequest.getFileNames();
	                if(iter.hasNext()) {
	                    List<MultipartFile> theList = multiRequest.getFiles(iter.next().toString());
	                    for(MultipartFile file:theList) {
		                    //MultipartFile file = multiRequest.getFile(iter.next().toString());
		                    if (file!=null) {
		                        String fileName = next();
		                        String origFileName = file.getOriginalFilename();
		                        if (origFileName.indexOf(".")<=0){

		                        }else {
		                            String fileExt=file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
		                            String path = "tmp";
		                            File theLocalTmpPath=new File(path);
		                            if(!theLocalTmpPath.exists()){
		                                theLocalTmpPath.mkdirs();
		                            }
		                            File theUploadedFile=new File(theLocalTmpPath,fileName+fileExt);
		                           saveFile(file.getInputStream(),theUploadedFile);
									if(theUploadedFile.length()/1024<limitSize){
										//file.getOriginalFilename();
										  value  = updateFileService.uploadFile(theUploadedFile,fileName+fileExt);
									}else{
										//("上传文件超过限制大小"+limitSize+"K");
									}
		                        }
		                        //resultList.add(theVo);
		                    }
	                    }
	                }
	            }
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return value;
    }
	@GetMapping(value = "/fileDelete.do")
	public String deleteFile(HttpServletRequest request){
		String value = "";
		try {
			String key = request.getParameter("key");
			value = deleteFileService.deleteFile(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}

	public static String saveFile(InputStream inputStream, File file) throws IOException {
		FileOutputStream outputStream = null;
		byte[] buffer = new byte[10240];// 10K
		int len;
		try {
			if (!file.exists()){
				file.getParentFile().mkdirs();
			}
			outputStream = new FileOutputStream(file);

			while ((len = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, len);
			}
		} finally {
			if (outputStream != null) {
				outputStream.flush();
				outputStream.close();
			}
			if (buffer != null){
				buffer = null;
			}
		}
		return file.getAbsolutePath();
	}
	public static String next() {
		Date date = new Date();
		int seq = 0;
		int ROTATION = 99999;
		if (seq > ROTATION)
			seq = 0;
		//buf.delete(0, buf.length());  
		date.setTime(System.currentTimeMillis());
		String str = String.format("%1$tY%1$tm%1$td%1$tH%1$tM%1$tS%2$05d", date, seq++);
		return str;
	}
}
