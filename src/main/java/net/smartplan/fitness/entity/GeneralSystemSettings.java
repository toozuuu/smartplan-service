package net.smartplan.fitness.entity;

import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * @author H.D. Sachin Dilshan
 */

@Entity
@Table(name = "general_system_settings")
@XmlRootElement
@Data
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

}
