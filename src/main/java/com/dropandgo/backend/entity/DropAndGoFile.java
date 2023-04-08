package com.dropandgo.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "file")
@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DropAndGoFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileType;
    private String path;
    private Boolean isPrivate;
    private String filePassword;
    private Long fileSize;
    private String displayName;
    private String actualName;
    private String uploadedBy;
    private Long uploadedOn;


}
