package com.servicerc;

import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.util.Auth;
import org.springframework.stereotype.Service;

@Service("deleteFileService")
public class DeleteFileService {

    public String deleteFile(String key) {
        String bucket = "file";
        String accessKey = "G735o55yIIyv1wHQcWcUwTphOeg12bPdP0tTvc90";
        String secretKey = "ZUV1jgDrvxxf416av0iAzXfBvg9Oyl4jW9PMwvxH";
        String isSuccess;
        try {
            Configuration cfg = new Configuration(Zone.zone2());
            Auth auth = Auth.create(accessKey, secretKey);
            BucketManager bucketManager = new BucketManager(auth, cfg);
            bucketManager.delete(bucket, key);
            isSuccess = "true";
        } catch (QiniuException ex) {
            //如果遇到异常，说明删除失败
            System.err.println(ex.code());
            System.err.println(ex.response.toString());
            isSuccess = "false";
        }
        return isSuccess;
    }
}
