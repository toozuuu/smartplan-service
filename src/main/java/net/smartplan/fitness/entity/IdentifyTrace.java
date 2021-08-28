package net.smartplan.fitness.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * @author H.D. Sachin Dilshan
 */

@Entity
@Table(name = "identify_trace")
@XmlRootElement
@Data
public class IdentifyTrace {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Column(name = "email")
    private String email;

    @Column(name = "goalDays")
    private Double goalDays;

    @Column(name = "goalExpiredDate")
    private String goalExpiredDate;

    @Column(name = "status")
    private String status;

    @Column(name = "dailyStatus")
    private Boolean dailyStatus;

    @Column(name = "created")
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date created;

    @Column(name = "updated")
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date updated;

}
