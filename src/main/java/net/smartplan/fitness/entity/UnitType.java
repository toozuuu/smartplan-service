package net.smartplan.fitness.entity;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import java.util.Objects;

/**
 * @author H.D. Sachin Dilshan
 */

@Entity
@Table(name = "unit_type")
@XmlRootElement
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class UnitType {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Column(name = "description")
    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date created;

    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date updated;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UnitType unitType = (UnitType) o;
        return Objects.equals(id, unitType.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
