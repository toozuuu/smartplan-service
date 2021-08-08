package net.smartplan.fitness.repository;

import net.smartplan.fitness.entity.FileStorage;
import org.springframework.data.repository.CrudRepository;

public interface FileStorageRepository extends CrudRepository<FileStorage, String> {

    FileStorage findByFileId(String fileId);
}
