package com.kanfs.omas.controller;

import com.kanfs.omas.result.Result;
import com.kanfs.omas.result.ResultCodeEnum;
import com.kanfs.omas.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@CrossOrigin
@RestController
public class FileUploadController {

    @Autowired
    private FileService fileService;

    @GetMapping("/mas/upload")
    public ResponseEntity<byte[]> uploadFile() throws IOException {
        // 获取文件资源
        Resource resource = new ClassPathResource("static/area.json");

        // 读取文件内容
        byte[] fileContent = Files.readAllBytes(Path.of(resource.getURI()));

        // 构建响应头
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(fileContent);
    }

    @PostMapping("/mas/admin/file/img/{avatar}")
    public Result<String> uploadFile(
            @RequestParam(name="file") MultipartFile file,
            @PathVariable("avatar") String avatar) {
        try {
            String fileUrl = fileService.uploadFile(file, avatar);
            return Result.ok(fileUrl);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("上传头像失败");
        }
    }

    @DeleteMapping("/mas/admin/file")
    public Result fileDelete(@RequestParam("fileUrl") String fileUrl) {
        fileService.removeFile(fileUrl);

        //返回r对象
        return Result.ok();
    }
}
