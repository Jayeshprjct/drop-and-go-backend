package com.dropandgo.backend.controllers;

import com.dropandgo.backend.constants.AccountConstant;
import com.dropandgo.backend.entity.DropAndGoFile;
import com.dropandgo.backend.entity.User;
import com.dropandgo.backend.exceptions.LoginFailedException;
import com.dropandgo.backend.exceptions.UnauthorizedAccessException;
import com.dropandgo.backend.repository.AccountRepository;
import com.dropandgo.backend.repository.AdminRepository;
import com.dropandgo.backend.responses.FileDeleteResponse;
import com.dropandgo.backend.responses.FileUploadResponse;
import com.dropandgo.backend.responses.FileVerifyResponse;
import com.dropandgo.backend.services.AccountService;
import com.dropandgo.backend.services.AdminService;
import com.dropandgo.backend.services.FileService;
import com.dropandgo.backend.services.FilesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class FilesController {

    @Autowired
    private FilesService filesService;
    @Autowired
    private FileService fileService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AdminService adminService;
    @Autowired
    private AdminRepository adminRepository;

    @PostMapping("/file/upload")
//    @CrossOrigin(methods = {RequestMethod.POST, RequestMethod.OPTIONS}, allowedHeaders = {"Content-Type", "X-Requested-With", "accept", "Origin", "Access-Control-Request-Method", "Access-Control-Request-Headers"}, exposedHeaders = {"Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"})
    public FileUploadResponse uploadFileUsingMultipart(
            @RequestParam("file") MultipartFile file,
            @RequestParam("fileName") String fileName,
            @RequestParam("isPrivate") Boolean isPrivate,
            @RequestParam(value = "filePassword", required = false) String filePassword,
            @RequestHeader("email") String email,
            @RequestHeader("password") String password
    ) throws UnauthorizedAccessException, IOException, LoginFailedException {
        if (email != null && password != null && accountService.verifyUser(email, password)) {
            if (isPrivate && filePassword == null) {
                throw new LoginFailedException("Incomplete details!");
            }
            String uploadedBy = accountService.getUserNameByEmail(email);

            DropAndGoFile uploadedFile = filesService.uploadFileWithDetails(file, fileName, isPrivate, filePassword, uploadedBy);
            return new FileUploadResponse(
                    HttpStatus.OK,
                    uploadedFile.getActualName(),
                    uploadedFile.getDisplayName() + " is uploaded",
                    AccountConstant.getTime()
            );
        }
        throw new UnauthorizedAccessException("Unauthorized access to upload files");
    }

    @GetMapping("/file/download")
    public ResponseEntity<Resource> downloadFileUsingName(
            @RequestParam("fileName") String fileName,
            @RequestHeader(value = "filePassword", required = false) String filePassword,
            @RequestHeader("fileDownloaderEmail") String fileDownloaderEmail
    ) throws FileNotFoundException, UnauthorizedAccessException, LoginFailedException {
        DropAndGoFile requestedFile = filesService.getFileDetailsByName(fileName);
        if (requestedFile.getIsPrivate() && filePassword == null) {
            throw new UnauthorizedAccessException("File password cannot be null for a private file!");
        }
        if (requestedFile.getIsPrivate()) {
            filesService.verifyPassword(requestedFile, filePassword);
        }
        DropAndGoFile file = filesService.getFileDetailsByName(fileName);
        InputStream fileStream = fileService.getInputStreamUsingPath(fileName);

        Optional<User> optionalFileDownloader = accountRepository.findByEmail(fileDownloaderEmail);
        if (optionalFileDownloader.isPresent()) {
            User fileDownloader = optionalFileDownloader.get();
            fileDownloader.setFilesDownloaded(fileDownloader.getFilesDownloaded() + 1);
            accountRepository.save(fileDownloader);
        } else {
            throw new LoginFailedException("No user found!");
        }

        return ResponseEntity.status(
                        HttpStatus.OK
                ).contentType(MediaType.parseMediaType(file.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "file; filename=\"" + file.getDisplayName() + "\"")
                .body(new InputStreamResource(fileStream));

    }

    @GetMapping("/file/verify")
    public FileVerifyResponse verifyFileRequestUsingName(@RequestParam("fileName") String name, @RequestHeader("adminId") String adminId, @RequestHeader("adminPassword") String adminPassword) throws FileNotFoundException, UnauthorizedAccessException {
        if (adminService.isAdmin(adminId, adminPassword)) {
            DropAndGoFile requestedFile = filesService.getFileDetailsByName(name);
            return new FileVerifyResponse(
                    HttpStatus.OK,
                    requestedFile,
                    "Request file exists in the database",
                    AccountConstant.getTime()
            );
        }
        throw new UnauthorizedAccessException("Unauthorized to verify file!");
    }

    @GetMapping("/account/uploadedFiles")
    public List<DropAndGoFile> getUploadedFilesOfUserByName(@RequestParam("email") String email, @RequestHeader("adminId") String adminId, @RequestHeader("adminPassword") String adminPassword) throws UnauthorizedAccessException, LoginFailedException {
        if (adminService.isAdmin(adminId, adminPassword)) {
            String username = accountService.getUserNameByEmail(email);
            List<DropAndGoFile> files = filesService.getFilesByUsername(username);
            return files;
        }
        throw new UnauthorizedAccessException("Unauthorized to access files!");
    }

    @DeleteMapping("/file/delete")
    public FileDeleteResponse deleteFileUsingName(
            @RequestParam("fileName") String fileName,
            @RequestHeader("email") String email,
            @RequestHeader("password") String password
    ) throws Exception {
        DropAndGoFile requiredFile = filesService.getFileDetailsByName(fileName);
        if (accountService.verifyUser(email, password)) {
            if (accountService.getUserNameByEmail(email).equals(requiredFile.getUploadedBy())) {
                DropAndGoFile deletedFile = filesService.deleteFile(requiredFile);
                return new FileDeleteResponse(
                        HttpStatus.OK,
                        deletedFile.getDisplayName(),
                        deletedFile.getActualName() + " was deleted successfully!",
                        true,
                        AccountConstant.getTime()
                );
            }
        }
        throw new UnauthorizedAccessException("Unauthorized access to delete file!");
    }
}
