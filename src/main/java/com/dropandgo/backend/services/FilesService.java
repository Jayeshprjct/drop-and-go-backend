package com.dropandgo.backend.services;

import com.dropandgo.backend.entity.DropAndGoFile;
import com.dropandgo.backend.exceptions.UnauthorizedAccessException;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface FilesService {

    DropAndGoFile uploadFileWithDetails(MultipartFile file, String fileName, Boolean isPrivate, String filePassword, String username) throws IOException;

    DropAndGoFile getFileDetailsByName(String fileName) throws FileNotFoundException;

    DropAndGoFile deleteFile(DropAndGoFile file) throws Exception;

    boolean verifyPassword(DropAndGoFile requestedFile, String filePassword) throws UnauthorizedAccessException;

    List<DropAndGoFile> getFilesByUsername(String username);
}
