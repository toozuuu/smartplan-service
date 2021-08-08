/*
 * Copyright (c) 2020. Property of EPIC Lanka Technologies Pvt. Ltd.
 */

package net.smartplan.fitness.service.impl;

import net.smartplan.fitness.entity.FileStorage;
import net.smartplan.fitness.repository.FileStorageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * File Upload Database Layer Service
 *
 * @author Sachin Dilshan
 * @version 1.0.0
 * @since Version 1.0.0
 */
@Service
@Transactional(rollbackFor = Exception.class, transactionManager = "tenantTransactionManager")
public class FileDataPersistService {

    private final FileStorageRepository fileStorageRepository;

    /**
     * Constructor for Spring Autowired dependencies
     *
     * @param fileStorageRepository CRUDRepository for FileStorage Entity
     */
    @Autowired
    public FileDataPersistService(FileStorageRepository fileStorageRepository) {
        this.fileStorageRepository = fileStorageRepository;
    }

    /**
     * Save file Details to DB
     *
     * @param relativePath Relative path of the uploaded file
     * @param absolutePath Absolute path in the File System
     * @param fileType     Type of the file. (pdf, jpg etc)
     * @param fileName     Original File Name
     * @param fileID       ID of the file generated as UUID
     * @return ID of the file after saving to DB (Same ID which was submitted as file ID above!)
     */
    String persistToDB(String relativePath, String absolutePath, String fileType, String fileName, String fileID) {
        FileStorage fileStorage = new FileStorage();
        fileStorage.setCreatedTime(new Date());
        fileStorage.setUpdatedTime(new Date());
        fileStorage.setAbsolutePath(absolutePath);
        fileStorage.setFileType(fileType);
        fileStorage.setRelativePath(relativePath);
        fileStorage.setOriginalName(fileName);
        fileStorage.setFileId(fileID);

        fileStorageRepository.save(fileStorage);

        return fileID;
    }

}
