package net.smartplan.fitness.service.impl;

import net.smartplan.fitness.entity.FileStorage;
import net.smartplan.fitness.repository.FileStorageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional(rollbackFor = Exception.class)
public class FileDataPersistService {

    private final FileStorageRepository fileStorageRepository;


    @Autowired
    public FileDataPersistService(FileStorageRepository fileStorageRepository) {
        this.fileStorageRepository = fileStorageRepository;
    }

    void persistToDB(String relativePath, String absolutePath, String fileType, String fileName, String fileID) {
        FileStorage fileStorage = new FileStorage();
        fileStorage.setCreatedTime(new Date());
        fileStorage.setUpdatedTime(new Date());
        fileStorage.setAbsolutePath(absolutePath);
        fileStorage.setFileType(fileType);
        fileStorage.setRelativePath(relativePath);
        fileStorage.setOriginalName(fileName);
        fileStorage.setFileId(fileID);

        fileStorageRepository.save(fileStorage);

    }

}
