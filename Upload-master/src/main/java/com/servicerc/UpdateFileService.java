package com.servicerc;

import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.springframework.stereotype.Service;


import java.io.File;
import java.util.UUID;

/**
 * @author ：zjh
 * @date ：Created in 2019-05-17 10:23
 * @description：${description}
 * @modified By：
 * @version: $version$
 */
@Service("uploadFileService")
public class UpdateFileService {

    public String  uploadFile(File file,String name) {
    	String path ="";
        String bucket = "file";
        String accessKey = "G735o55yIIyv1wHQcWcUwTphOeg12bPdP0tTvc90";
        String secretKey = "ZUV1jgDrvxxf416av0iAzXfBvg9Oyl4jW9PMwvxH";
        try {
            Configuration cfg = new Configuration(Zone.zone2());
            UploadManager uploadManager = new UploadManager(cfg);
            String localFilePath = file.getPath();

            String key = name;
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);
            Response response = uploadManager.put(localFilePath, key, upToken);
            path = response.bodyString();
            File files=new File(localFilePath);
            if(files.exists()&&files.isFile()){
                files.delete();
        	}
        } catch (QiniuException e) {
            e.printStackTrace();
        }
        return path;
    }
       
}
