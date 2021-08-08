package net.smartplan.fitness.controller;

import lombok.extern.slf4j.Slf4j;
import net.smartplan.fitness.entity.FileStorage;
import net.smartplan.fitness.repository.FileStorageRepository;
import net.smartplan.fitness.response.CommonResponse;
import net.smartplan.fitness.service.FileService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin
@RequestMapping("/file")
@Slf4j
public class FileController {

    private final FileService fileService;
    private final FileStorageRepository fileStorageRepository;

    public FileController(FileService fileService,
                          FileStorageRepository fileStorageRepository) {
        this.fileService = fileService;
        this.fileStorageRepository = fileStorageRepository;
    }

    @PostMapping("/upload/")
    public ResponseEntity singleFileUpload(@RequestParam("file") MultipartFile file) {
        String fileID = fileService.uploadFile(file);
        log.info(fileID);
        return new ResponseEntity<>(new CommonResponse(true, fileID), HttpStatus.OK);

    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> fetchFile(@PathVariable("fileId") String fileId) {
        FileStorage fileDetails = fileStorageRepository.findByFileId(fileId);
        if (fileDetails == null) {
            log.info("Requested resource is not found! {}", fileId);
            return null;
        }
        Resource resource = fileService.loadFileAsResource(fileDetails.getAbsolutePath());

        if (resource == null) {
            log.info("RESOURCE 404");
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(fileDetails.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDetails.getOriginalName() + "\"")
                .body(resource);

    }
}
