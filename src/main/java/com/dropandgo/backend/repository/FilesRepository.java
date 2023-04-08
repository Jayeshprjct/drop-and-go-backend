package com.dropandgo.backend.repository;

import com.dropandgo.backend.entity.DropAndGoFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FilesRepository extends JpaRepository<DropAndGoFile, Long> {
    Optional<DropAndGoFile> findByActualName(String actualName);
    List<DropAndGoFile> findByUploadedBy(String username);
}
