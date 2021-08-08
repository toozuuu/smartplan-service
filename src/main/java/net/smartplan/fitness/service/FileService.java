package net.smartplan.fitness.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    String uploadFile(MultipartFile file);

    Resource loadFileAsResource(String absolutePath);
}
