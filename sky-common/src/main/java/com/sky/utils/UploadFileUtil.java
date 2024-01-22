package com.sky.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

public class UploadFileUtil {
    /*
     * 获取文件保存路径
     *
     * @return File
     * @throws FileNotFoundException
     */
    static List<File> getUploadDirectory() throws FileNotFoundException {

        File targetPath = new File(ResourceUtils.getURL("classpath:").getPath());

        if (!targetPath.exists()) {
            targetPath = new File("");
        }

        String resourcesPath = System.getProperty("user.dir") + "/sky-server/src/main/resources";
        File path = new File(resourcesPath);
        File upload = new File(path.getAbsolutePath(), "upload");
        File uploadTarget = new File(targetPath.getAbsolutePath(), "upload");

        if (!upload.exists()) {
            upload.mkdirs();
        }
        if (!uploadTarget.exists()) {
            uploadTarget.mkdirs();
        }

        List<File> files = new ArrayList<>();
        files.add(upload);
        files.add(uploadTarget);
        return files;
    }

    public static String upload(MultipartFile myFile, String dir) throws IOException {
        String filePath = "";

        if (!myFile.isEmpty()) {
            try {
                String filename = myFile.getOriginalFilename();
                filename = UUID.randomUUID() + filename.substring(filename.lastIndexOf("."));
                List<File> files = getUploadDirectory();
                File curFile = new File(files.get(0), filename);
                myFile.transferTo(curFile);
                FileCopyUtils.copy(curFile, new File(files.get(1), filename));
                filePath = "http://localhost:8080/upload/" + filename;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return filePath;
    }
}
