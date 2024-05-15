package com.kanfs.omas.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.CannedAccessControlList;
import com.kanfs.omas.service.FileService;
import com.kanfs.omas.utils.ConstantPropertiesUtil;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    /**
     * 上传文件
     * @param file
     * @param avatar
     * @return
     */
    public String uploadFile(MultipartFile file, String avatar) throws IOException {
        return getImgUrl(file, avatar);
    }


    private String getImgUrl(MultipartFile file, String name) throws IOException {
        //判断oss实例是否存在：如果不存在则创建，如果存在则获取
        OSS ossClient = getClient();


        //文件名：uuid.扩展名
        String fileName = UUID.randomUUID().toString().replaceAll("-", "");
        String key = name + "/" + fileName + "-" + file.getOriginalFilename();

        //文件上传至阿里云
        ossClient.putObject(ConstantPropertiesUtil.BUCKET, key, file.getInputStream());

        // 关闭OSSClient。
        ossClient.shutdown();

        //返回url地址
        return "https://" + ConstantPropertiesUtil.BUCKET + "." + ConstantPropertiesUtil.ENDPOINT + "/" + key;
    }

    private OSS getClient() {
        OSS ossClient = getOssClient();
        if (!ossClient.doesBucketExist(ConstantPropertiesUtil.BUCKET)) {
            //创建bucket
            ossClient.createBucket(ConstantPropertiesUtil.BUCKET);
            //设置oss实例的访问权限：公共读
            ossClient.setBucketAcl(ConstantPropertiesUtil.BUCKET, CannedAccessControlList.PublicRead);
        }
        return ossClient;
    }

    private OSS getOssClient() {
        //判断oss实例是否存在：如果不存在则创建，如果存在则获取
        return new OSSClientBuilder().build(
                ConstantPropertiesUtil.ENDPOINT,
                ConstantPropertiesUtil.ACCESS_KEY_ID,
                ConstantPropertiesUtil.SECRECT);
    }

    /**
     * 删除文件
     * @param fileUrl
     */
    public void removeFile(String fileUrl) {
        //判断oss实例是否存在：如果不存在则创建，如果存在则获取
        OSS ossClient = getOssClient();
        String host = "https://" + ConstantPropertiesUtil.BUCKET + "." + ConstantPropertiesUtil.ENDPOINT + "/";

        // 删除文件。
        ossClient.deleteObject(ConstantPropertiesUtil.BUCKET,
                fileUrl.substring(host.length()));

        // 关闭OSSClient。
        ossClient.shutdown();
    }
}

//gcc ./app.c++ -r hello
//argc = 2
//argv[0] = "-r"
//argv[1] = "hello"