package net.smartplan.fitness.entity;

import lombok.*;
import org.hibernate.Hibernate;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author H.D. Sachin Dilshan
 */

@Entity
@Table(name = "file_metadata")
@XmlRootElement
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class FileMetadata implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "file_id")
    private String fileId;
    @Lob
    @Column(name = "metadata")
    private Object metadata;
    @JoinColumn(name = "file_id", referencedColumnName = "file_id", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private FileStorage fileStorage;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        FileMetadata that = (FileMetadata) o;
        return Objects.equals(fileId, that.fileId);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
