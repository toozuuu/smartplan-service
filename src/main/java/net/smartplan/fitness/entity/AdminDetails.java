package net.smartplan.fitness.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * @author H.D. Sachin Dilshan
 */

@Entity
@Table(name = "admin_details")
@XmlRootElement
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class AdminDetails {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "created")
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date created;

    @Column(name = "updated")
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date updated;
}
