package com.webapp.onlineelectronicstore.services.Impl;

import com.webapp.onlineelectronicstore.exceptions.BadApiRequest;
import com.webapp.onlineelectronicstore.services.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);


    @Override
    public String uploadFile(MultipartFile file, String path) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFileName = UUID.randomUUID().toString();
        String fileName = newFileName+ fileExtension;

        String fullPathWithFileName = path + fileName;

        logger.info("Uploading file to: " + fullPathWithFileName);
        if (fileExtension.equalsIgnoreCase(".jpg")
                || fileExtension.equalsIgnoreCase(".jpeg")
                || fileExtension.equalsIgnoreCase(".png")) {

            // Save file
            logger.info("File extension is {}", fileExtension);
            File folder = new File(path);

            if (!folder.exists()) {
                folder.mkdirs();
            }

            Files.copy(
                    file.getInputStream(),
                    Paths.get(fullPathWithFileName),
                    StandardCopyOption.REPLACE_EXISTING);

        } else {
            throw new BadApiRequest(
                    "File with this " + fileExtension + " is not allowed");
        }


        return fileName;
    }

    @Override
    public InputStream getFile(String path, String fileName) throws FileNotFoundException {
        String fullPath = path +File.separator+ fileName;
        InputStream inputStream = new FileInputStream(fullPath);
        return inputStream;
    }
}
