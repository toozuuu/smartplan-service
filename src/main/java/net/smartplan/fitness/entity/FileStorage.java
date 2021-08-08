package net.smartplan.fitness.entity;

import lombok.Data;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "file_storage")
@XmlRootElement
@Data
public class FileStorage implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "file_id")
    private String fileId;
    @Column(name = "original_name")
    private String originalName;
    @Column(name = "file_type")
    private String fileType;
    @Lob
    @Column(name = "absolute_path")
    private String absolutePath;
    @Lob
    @Column(name = "relative_path")
    private String relativePath;
    @Column(name = "created_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTime;
    @Column(name = "updated_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedTime;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "fileStorage")
    private FileMetadata fileMetadata;
}
