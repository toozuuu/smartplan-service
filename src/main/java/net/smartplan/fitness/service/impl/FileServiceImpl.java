package net.smartplan.fitness.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.smartplan.fitness.service.FileService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

@Service
@Transactional(rollbackOn = Exception.class)
@Slf4j
public class FileServiceImpl implements FileService {

    private final Path fileStorageLocation;
    private FileDataPersistService fileDataPersistService;
    private String uploadDirectory;

    @Autowired
    public FileServiceImpl(FileDataPersistService fileDataPersistService,
                           @Value("${fileUploadPath}") String fileUploadLocation) {

        this.fileDataPersistService = fileDataPersistService;
        this.uploadDirectory = fileUploadLocation;
        this.fileStorageLocation = Paths.get(fileUploadLocation)
                .toAbsolutePath().normalize();

    }

    @Override
    public String uploadFile(MultipartFile file) {

        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        if (fileName.contains("..")) {
            log.error("Sorry! Filename contains invalid path sequence!");
            return null;
        }

        try {

            UUID uuid = UUID.randomUUID();
            String fileID = uuid.toString();
            String fileExt = FilenameUtils.getExtension(fileName);
            String fileStoreAlias = fileID.concat(".").concat(fileExt);

            log.info("UPLOADING..");
            Path targetLocation = this.fileStorageLocation.resolve(fileStoreAlias);

            File fileToUpload = targetLocation.toFile();

            FileUtils.copyInputStreamToFile(file.getInputStream(), fileToUpload);

            fileDataPersistService.persistToDB(fileToUpload.getAbsolutePath().substring(uploadDirectory.length()), targetLocation.toString(), resolveMediaTypeByExtension(fileExt), fileName, fileID);
            log.info("File Upload success.");

            return fileID;

        } catch (IOException ex) {
            log.info("Could not store file " + fileName + ". Please try again!", ex);
            return null;
        }
    }

    public Resource loadFileAsResource(String absolutePath) {
        try {
            Path filePath = this.fileStorageLocation.resolve(absolutePath).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                log.info("FILE NOT FOUND!");
                return null;
            }
        } catch (MalformedURLException ex) {
            log.info("FILE PATH IS INVALID!");
            return null;
        }
    }

    /**
     * Method to resolve {@link org.springframework.http.MediaType} from the file extension
     *
     * @param ext Extension of the uploaded file
     * @return The media type from the associated with the extention
     */
    private String resolveMediaTypeByExtension(String ext) {
        switch (ext) {
            case "png":
                return "image/png";
            case "jpeg":
            case "jpg":
                return "image/jpeg";
            case "pdf":
                return "application/pdf";
            case "txt":
                return "text/plain";
            default:
                return "application/octet-stream";
        }
    }

    public boolean deleteFile(String uuid) {
        File file = new File(fileDataPersistService.getImageAbsolutePath(uuid));
        if (file.delete()) {
            log.info("file.txt File deleted from Project root directory");
            fileDataPersistService.deleteImage(uuid);
            return true;
        } else {
            log.info("File file.txt doesn't exist in the project root directory");
            return false;
        }
    }
}
