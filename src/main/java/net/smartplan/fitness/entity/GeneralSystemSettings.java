package net.smartplan.fitness.entity;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import java.util.Objects;

/**
 * @author H.D. Sachin Dilshan
 */

@Entity
@Table(name = "general_system_settings")
@XmlRootElement
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class GeneralSystemSettings {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "code")
    private String code;
    @Column(name = "value")
    private String value;
    @Column(name = "updated")
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        GeneralSystemSettings that = (GeneralSystemSettings) o;
        return Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
