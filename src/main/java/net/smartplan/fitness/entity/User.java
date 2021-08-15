
package net.smartplan.fitness.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author H.D. Sachin Dilshan
 */
@Entity
@Table(name = "user")
@XmlRootElement
@Data
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "gender")
    private String gender;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "age")
    private Double age;

    @Column(name = "weight")
    private Double weight;

    @Column(name = "goal_type")
    private String goalType;

    @Column(name = "goal_time")
    private Double goalTime;

    @Column(name = "height")
    private Double height;

    @Column(name = "body_fat")
    private Double bodyFat;

    @Column(name = "fat_free_mass")
    private Double fatFreeMass;

    @Column(name = "estimated_bmr")
    private Double estimatedBmr;

    @Column(name = "activity_level")
    private String activityLevel;

    @Column(name = "calories_to_add")
    private Double caloriesToAdd;

    @Column(name = "daily_req")
    private Double dailyReq;

    @Column(name = "daily_req_non")
    private Double dailyReqNon;

    @Column(name = "consulter")
    private String consulter;

    @Column(name = "status")
    private String status;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date updated;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId")
    private List<MacronutrientFood> macronutrientFoodCollection;

    @OneToMany(mappedBy = "userId")
    private List<CaloriePlan> caloriePlanCollection;

    @OneToMany(mappedBy = "userId")
    private List<UserAddress> userAddressCollection;

}
