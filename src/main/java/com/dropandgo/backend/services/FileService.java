package com.dropandgo.backend.services;

import com.dropandgo.backend.constants.AccountConstant;
import com.dropandgo.backend.constants.FileConstant;
import com.dropandgo.backend.entity.DropAndGoFile;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
@Slf4j
public class FileService {

    public DropAndGoFile uploadFile(MultipartFile file, String fileName) throws IOException {
        if (file.getSize() > 209715200) {
            throw new FileSizeLimitExceededException("Cannot upload file greater than 200Mb", file.getSize(), 209715200);
        }
        long timeStamp = AccountConstant.getTime();
        //Name of the image.
        String originalName = file.getOriginalFilename();
        assert originalName != null;
        String changedName = fileName.replace(" ", "_").toLowerCase();
        String usableName = changedName + "_" + timeStamp + originalName.substring(originalName.lastIndexOf("."));
        String extension = originalName.substring(originalName.lastIndexOf("."));

        //Full Path where the image will be saved
        String fullPath = FileConstant.filePath + usableName;

        if (new File(fullPath).exists()) {
            throw new FileAlreadyExistsException(usableName, FileConstant.filePath + fileName, "Provided image already exists.");
        }

        //Creating folder if not present
        File uploadFolder = new File(FileConstant.filePath);
        log.info("Outside folder creation function");
        if (!uploadFolder.exists()) {
            log.info("Inside folder creation function");
            uploadFolder.mkdir();
        }

        //Copying files to the fullPath
        Files.copy(file.getInputStream(), Paths.get(fullPath));
        DropAndGoFile uploadedFile = DropAndGoFile
                .builder()
                .fileType(file.getContentType())
                .displayName(fileName + extension)
                .fileSize(file.getSize())
                .actualName(usableName)
                .path(FileConstant.filePath)
                .uploadedOn(timeStamp)
                .build();
        return uploadedFile;
    }

    public InputStream getInputStreamUsingPath(String fileName) throws FileNotFoundException {
        String fullPath = FileConstant.filePath + fileName;
        InputStream stream = new FileInputStream(fullPath);
        return stream;
    }

    public Boolean deleteFile(String actualName) throws FileNotFoundException {
        String fullPath = FileConstant.filePath + actualName;
        File file = new File(FileConstant.filePath);
        if (file.exists()) {
            File f = new File(file, actualName);
            return f.delete();
        }
        throw new FileNotFoundException("File with name " + actualName + " not found in the database!");
    }
}
