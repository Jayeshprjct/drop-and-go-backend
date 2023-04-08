package com.dropandgo.backend.services;

import com.dropandgo.backend.entity.DropAndGoFile;
import com.dropandgo.backend.entity.User;
import com.dropandgo.backend.exceptions.UnauthorizedAccessException;
import com.dropandgo.backend.repository.AccountRepository;
import com.dropandgo.backend.repository.FilesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class FilesServiceImpl implements FilesService {

    @Autowired
    private FileService fileService;
    @Autowired
    private FilesRepository filesRepository;
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public DropAndGoFile uploadFileWithDetails(MultipartFile file, String fileName, Boolean isPrivate, String filePassword, String username) throws IOException {
        DropAndGoFile uploadedFile = fileService.uploadFile(file, fileName);
        uploadedFile.setUploadedBy(username);
        uploadedFile.setIsPrivate(isPrivate);
        uploadedFile.setFilePassword(filePassword);

        Optional<User> optionalFileUploader = accountRepository.findByName(username);
        if (optionalFileUploader.isPresent()) {
            User fileUploader = optionalFileUploader.get();
            fileUploader.setFilesUploaded(fileUploader.getFilesUploaded() + 1);
            accountRepository.save(fileUploader);
        }

        return filesRepository.save(uploadedFile);
    }

    @Override
    public DropAndGoFile getFileDetailsByName(String fileName) throws FileNotFoundException {
        return filesRepository.findByActualName(fileName).orElseThrow(
                () -> new FileNotFoundException("No file named " + fileName + " found in the database!")
        );
    }

    @Override
    public DropAndGoFile deleteFile(DropAndGoFile file) throws Exception {
        if (fileService.deleteFile(file.getActualName())) {
            filesRepository.delete(file);
            return file;
        }
        throw new Exception("Some error occured");
    }

    @Override
    public boolean verifyPassword(DropAndGoFile requestedFile, String filePassword) throws UnauthorizedAccessException {
        if (requestedFile.getFilePassword().equals(filePassword)) {
            return true;
        }
        throw new UnauthorizedAccessException("Incorrect file password");
    }

    @Override
    public List<DropAndGoFile> getFilesByUsername(String username) {
        return filesRepository.findByUploadedBy(username);
    }
}
